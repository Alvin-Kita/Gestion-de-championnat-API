package com.ipi.gestiondechampionnatapi.controller;

import com.ipi.gestiondechampionnatapi.models.User;
import com.ipi.gestiondechampionnatapi.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping(value ="/api/users")
public class UserController {


    /*
     * Repository
     */

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    /*
     * Controller
     */

    public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    /* *********************
     * Mapping
     ********************* */


    /*
     * Ping de test
     */

    @GetMapping("/ping")
    public String ping() {

        return "user pong";
    }


    /*
     * Récupérer la liste des utilisateurs
     */

    @GetMapping(value ="/")
    public List<User> all() {

        return userRepository.findAll();
    }


    /*
     * Récupérer un utilisateur par son ID
     */

    @GetMapping(value = "/{user}")
    public User getOne(@PathVariable(name = "user", required = false) User user) {
        if (user == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Utilisateur introuvable"
            );
        }

        return user;
    }


    /*
     * Récupérer un utilisateur en fonction de son user et password
     */

    @GetMapping(value = "/getUserByEMailAndPassword")
    public ResponseEntity<User> getUserByEMailAndPassword(@RequestParam String email, @RequestParam String password) {
        List<User> users = userRepository.findAll()
                .stream()
                .filter(user -> user.getEmail().equals(email))
                .filter(user -> passwordEncoder.matches(password, user.getPasswordHash()))
                .toList();
        if (users.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilisateur introuvable avec cette ID / mot de passe");
        }
        return new ResponseEntity<>(users.get(0), HttpStatus.OK);
    }


    /*
     * Post d'un utilisateur
     */

    @PostMapping(value = "/")
    public ResponseEntity<User> saveUser(@Valid @RequestBody User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, bindingResult.toString());
        } else {
            // Fix pour empêcher la création d'un utilisateur par-dessus un utilisateur déjà existant avec l'ID
            Optional<User> existingUser = userRepository.findById(user.getId());
            if(existingUser.isPresent()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Un utilisateur avec cet ID existe déjà. " +
                        "L'id est automatiquement géré et n'est pas attendu dans cette request");

            } else {
                String hashedPassword = passwordEncoder.encode(user.getPasswordHash());
                user.setPassword(hashedPassword);

                userRepository.save(user);

                return new ResponseEntity<>(user, HttpStatus.CREATED);
            }
        }
    }


    /*
     * Mettre à jour un utilisateur
     */
    @PutMapping(value = "/{user}")
    public ResponseEntity<User> updateUser(@PathVariable(name = "user") User user,
                                           @Valid @RequestBody User userUpdate, BindingResult bindingResult) {
        if (user == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Utilisateur à modifier introuvable"
            );
        } else {
            if (bindingResult.hasErrors()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, bindingResult.toString());
            } else {
                userUpdate.setId(user.getId());

                if (!user.getPasswordHash().equals(userUpdate.getPasswordHash())) {
                    String hashedPassword = passwordEncoder.encode(userUpdate.getPasswordHash());
                    userUpdate.setPassword(hashedPassword);
                }
                userRepository.save(userUpdate);

                return new ResponseEntity<>(userUpdate, HttpStatus.CREATED);
            }
        }
    }


    /*
     * Supprimer un utilisateur
     */

    @DeleteMapping(value = "{user}")
    public ResponseEntity<String> deleteUser(@PathVariable(name = "user") User user) {
        if (user == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Utilisateur à supprimer introuvable"
            );
        } else {
            userRepository.delete(user);

            return ResponseEntity.ok("L'utilisateur "+ user.getId() + " " + user.getEmail() + " a été supprimé avec succès");
        }
    }

}
