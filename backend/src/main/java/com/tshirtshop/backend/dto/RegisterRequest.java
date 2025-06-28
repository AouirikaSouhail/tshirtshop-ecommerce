//Ce fichier fait partie du package dto :
//DTO signifie Data Transfer Object = un petit objet utilisé pour transporter des données entre le frontend (formulaire)
// et le backend (contrôleur).
package com.tshirtshop.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {
    @NotBlank(message = "Le prénom est obligatoire.")
    private String firstName;

    @NotBlank(message = "Le nom est obligatoire.")
    private String lastName;

    @NotBlank(message = "L'adresse est obligatoire.")
    private String adresse;

    @NotBlank(message = "Le code postal est obligatoire.")
    private String codePostal;

    @NotBlank(message = "La ville est obligatoire.")
    private String ville;

    @NotBlank(message = "L'email est obligatoire.")
    @Email(message = "L'adresse email est invalide.")
    private String email;

    @NotBlank(message = "Le mot de passe est obligatoire.")
    @Size(min = 6, message = "Le mot de passe doit contenir au moins 6 caractères.")
    private String password;
}

//Ces 3 champs correspondent aux inputs du formulaire d’inscription HTML (ou Angular/React/Vue).
//Spring va automatiquement remplir un objet RegisterRequest avec ces valeurs grâce à @RequestBody.