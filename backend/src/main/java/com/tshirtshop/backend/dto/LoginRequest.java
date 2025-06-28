/**
 * DTO = Data Transfer Object (traduction : objet de transfert de données)
 C’est une classe intermédiaire utilisée pour transporter des données entre : le frontend (Angular/Postman) ⟷ le backend (Spring Boot)
 */
package com.tshirtshop.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
public class LoginRequest {
    @NotBlank(message = "L'email est obligatoire.")
    @Email(message = "L'adresse email est invalide.")
    private String email;

    @NotBlank(message = "Le mot de passe est obligatoire.")
    @Size(min = 6, message = "Le mot de passe doit contenir au moins 6 caractères.")
    private String password;
}

/**
 * “J’ai créé un DTO LoginRequest dans un package dto pour transporter les données du formulaire de connexion.
 * Il contient uniquement les champs utiles pour se connecter : email et password.
 * Cela me permet de garder mon entité User séparée des échanges avec le frontend, ce qui améliore la sécurité, la lisibilité
 * et l’organisation du code.”
 */