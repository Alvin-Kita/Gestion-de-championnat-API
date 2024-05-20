package com.ipi.gestiondechampionnatapi.controller;

import com.ipi.gestiondechampionnatapi.models.Championship;
import com.ipi.gestiondechampionnatapi.models.User;
import com.ipi.gestiondechampionnatapi.repository.ChampionshipRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping(value = "/api/championships")
public class ChampionshipController {

    private final ChampionshipRepository championshipRepository;

    public ChampionshipController(ChampionshipRepository championshipRepository) {
        this.championshipRepository = championshipRepository;
    }

    /*
     * Ping de test
     */
    @GetMapping("ping")
    public String ping() {
        return "championship pong";
    }

    /*
     * Récupérer la liste des championnats
     */
    @GetMapping(value = "/")
    public List<Championship> all() {

        return championshipRepository.findAll();
    }

    /*
     * Récupérer un championnat par son ID
     */
    @GetMapping(value = "/{championship}")
    public Championship getOne(@PathVariable(name = "championship", required = false) Championship championship) {
        if (championship == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Championnat introuvable"
            );
        }

        return championship;
    }

    /*
     * Post d'un championnat
     */
    @PostMapping(value = "/")
    public ResponseEntity<Championship> saveChampionship(@Valid @RequestBody Championship championship, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, bindingResult.toString());
        } else {
            championshipRepository.save(championship);
            return new ResponseEntity<>(championship, HttpStatus.CREATED);
        }
    }

    /*
     * Mettre à jour un championnat
     */
    @PutMapping(value = "/{championship}")
    public ResponseEntity<Championship> updateChampionship(@PathVariable(name = "championship", required = true) Championship championship,
                                           @Valid @RequestBody Championship championshipUpdate, BindingResult bindingResult) {
        if (championship == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Championat introuvable"
            );
        } else {
            if (bindingResult.hasErrors()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, bindingResult.toString());
            } else {
                championshipUpdate.setId(championship.getId());
                championshipRepository.save(championshipUpdate);
                return new ResponseEntity<>(championshipUpdate, HttpStatus.CREATED);
            }
        }
    }

    /*
     * Supprimer un championnat
     */
    @DeleteMapping(value = "{championship}")
    public ResponseEntity<String> deleteChampionship(@PathVariable(name = "championship", required = true) Championship championship) {
        if (championship == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Championnat introuvable"
            );
        } else {
            championshipRepository.delete(championship);
            return ResponseEntity.ok("Le championnat "+ championship.getId() + " " + championship.getName() + " a été supprimé avec succès");
        }
    }

}
