/**
 * DTO = Data Transfer Object (traduction : objet de transfert de données)
 C’est une classe intermédiaire utilisée pour transporter des données entre : le frontend (Angular/Postman) ⟷ le backend (Spring Boot)
 */
package com.tshirtshop.backend.dto;

import lombok.*;

@Getter
@Setter
public class LoginRequest {
    private String email;
    private String password;
}

/**
 * “J’ai créé un DTO LoginRequest dans un package dto pour transporter les données du formulaire de connexion.
 * Il contient uniquement les champs utiles pour se connecter : email et password.
 * Cela me permet de garder mon entité User séparée des échanges avec le frontend, ce qui améliore la sécurité, la lisibilité
 * et l’organisation du code.”
 */