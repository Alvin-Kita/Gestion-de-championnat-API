package com.ipi.gestiondechampionnatapi.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;


@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "firstname")
    @NotNull(message = "Le champ firstname de l'utilisateur ne peut pas être null")
    @NotBlank(message = "Le champ firstname de l'utilisateur ne peut pas être vide")
    private String firstname;

    @Column(name = "lastname")
    @NotNull(message = "Le champ lastname de l'utilisateur ne peut pas être null")
    @NotBlank(message = "Le champ lastname de l'utilisateur ne peut pas être vide")
    private String lastname;

    @Column(name = "email", unique = true)
    @NotNull(message = "Le champ email de l'utilisateur ne peut pas être null")
    @NotBlank(message = "Le champ email de l'utilisateur ne peut pas être vide")
    private String email;

    @Column(name = "password")
    @NotNull(message = "Le champ password de l'utilisateur ne peut pas être null")
    @NotBlank(message = "Le champ password de l'utilisateur ne peut pas être vide")
    private String passwordHash;

    @Column(name = "creationDate")
    @NotNull(message = "Le champ date de création de l'utilisateur ne peut pas être null")
    @Temporal(value = TemporalType.DATE)
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate creationDate;


    public User() {}

    /**
     * Objet Utilisateur
     * @param creationDate date de création
     * @param passwordHash mot de passe hashé
     * @param email mail de l'utilisateur (unique)
     * @param lastname nom de l'utilisateur
     * @param firstname prénom de l'utilisateur
     */
    public User(LocalDate creationDate, String passwordHash, String email, String lastname, String firstname) {
        this.creationDate = creationDate;
        this.passwordHash = passwordHash;
        this.email = email;
        this.lastname = lastname;
        this.firstname = firstname;
    }

    /*
     * **********************
     * GETTER & SETTER
     * **********************
     */

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPassword(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

}
