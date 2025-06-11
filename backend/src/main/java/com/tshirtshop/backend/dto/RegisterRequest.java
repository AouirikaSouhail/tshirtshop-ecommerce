//Ce fichier fait partie du package dto :
//DTO signifie Data Transfer Object = un petit objet utilisé pour transporter des données entre le frontend (formulaire)
// et le backend (contrôleur).
package com.tshirtshop.backend.dto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {
    private String fullName;
    private String email;
    private String password;
}

//Ces 3 champs correspondent aux inputs du formulaire d’inscription HTML (ou Angular/React/Vue).
//Spring va automatiquement remplir un objet RegisterRequest avec ces valeurs grâce à @RequestBody.