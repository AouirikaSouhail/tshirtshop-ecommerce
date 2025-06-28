package com.tshirtshop.backend.model;

// Tu importes les outils JPA pour que Java sache que cette classe va interagir avec la base de données (ex. @Entity, @Id, etc.).
import jakarta.persistence.*;

// Tu importes la librairie Lombok.
//Elle t’évite d’écrire du code répétitif comme les getters, setters, ou les constructeurs.
import lombok.*;


// Tu dis à Spring Boot : “Cette classe est une entité JPA”, donc elle correspond à une table dans la base de données.
@Entity
//Ce sont des annotations Lombok
@Getter
@Setter
@Data  //@Data est une super-annotation Lombok : Elle inclut automatiquement :
       //@Getter, @Setter ,@ToString , @EqualsAndHashCode @RequiredArgsConstructor.Tu n’as pas besoin de mettre @Getter et @Setter si tu mets déjà @Data.
@NoArgsConstructor
@AllArgsConstructor

public class User {

    @Id // Tu indiques que ce champ id est la clé primaire de ta table.
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Tu dis à Spring Boot : “Laisse la base de données générer l’id automatiquement (auto-incrément).”
    private Long id; // C’est le champ id, de type Long, qui va contenir le numéro unique de chaque utilisateur.

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String adresse;

    @Column(nullable = false)
    private String codePostal;

    @Column(nullable = false)
    private String ville;


    @Column(nullable = false, unique = true) // le champ ne peut pas être vide et le champ doit être unique (ex. 2 personnes ne peuvent pas avoir le même email)
    private String email;

    @Column(nullable = false)
    private String password;

    /**
     Explication :
     role est un champ texte qui contiendra "ROLE_USER" ou "ROLE_ADMIN".
     On lui donne "ROLE_USER" par défaut à l’inscription.
     Tu peux manuellement définir "ROLE_ADMIN" dans la BDD ou via un endpoint admin.
     */

    @Column(nullable = false)
    private String role = "ROLE_USER"; // Par défaut, tous les utilisateurs ont ce rôle
}

/**
 * Cette classe User est une entité JPA qui représente la table user dans la base de données. Elle contient 4 champs : id (clé primaire générée automatiquement), fullName, email (unique), et password.
 * Grâce à @Entity, Spring Boot crée automatiquement la table. Lombok génère les méthodes comme getId(), setEmail(), etc.
 * */