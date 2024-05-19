package com.ipi.gestiondechampionnatapi;

import com.ipi.gestiondechampionnatapi.models.User;
import com.ipi.gestiondechampionnatapi.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.expression.ParseException;

import java.time.LocalDate;

@Configuration
public class LoadData {
    private final Logger log = LoggerFactory.getLogger(LoadData.class);

    @Bean
    CommandLineRunner initDatabase(UserRepository repository) throws ParseException {
        log.info("Chargement des données utilisateurs");

        if (repository.count() == 0) {

            // Objet Date
            LocalDate dateCreation = LocalDate.parse("2024-04-02");

            // Création de l'objet User
            User userTest = new User(dateCreation, "password", "test@email.fr", "toto", "test");

            // Sauvegarde de l'utilisateur dans la base donnée
            return args -> log.info("Chargement de l'utilisateur : {}", repository.save(userTest));
        } else {
            return args -> log.info("Données déjà chargées");
        }
    }
}