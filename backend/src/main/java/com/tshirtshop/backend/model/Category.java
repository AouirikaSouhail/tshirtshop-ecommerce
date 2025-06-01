// Déclare que ce fichier appartient au dossier model
package com.tshirtshop.backend.model;
// On importe des outils de Spring JPA pour faire le lien avec MySQL
import jakarta.persistence.*;

// Cette classe est une entité → elle sera automatiquement convertie en table category dans MySQL

@Entity

public class Category {
  // @Id : clé primaire
  //@GeneratedValue : la base de données choisit automatiquement l’id (auto-incrément)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String name;   // Nom de la catégorie (ex: “T-Shirts Homme”)
    private String imageUrl; // URL de l’image (ex: "https://image.com/tshirt.png")

   // Constructeur
   public Category() {}
  public Category(Long id, String name, String imageUrl) {
    this.id = id;
    this.name = name;
    this.imageUrl = imageUrl;
  }


  // Getters et Setters Permettent à Spring (et toi) de lire et modifier les données

    public Long getId(){return id;}
    public void setId(Long id){this.id = id;}
    public String getName(){return name;}
    public void setName(String name){this.name = name;}
    public String getImageUrl(){return imageUrl;}
    public void setImageUrl(String imageUrl){this.imageUrl = imageUrl;}
}
