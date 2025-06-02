package com.tshirtshop.backend.model;


import jakarta.persistence.*;


// Ces annotations viennent de la bibliothèque Lombok.
// Elles évitent d’écrire tout le code “inutile” comme les getters/setters.

// Cette classe représente la table "product" dans la base de données
@Entity
public class Product {

    @Id    // Déclare l'attribut comme clé primaire
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-incrémentation
    private Long id;

    private String name; //Nom du produit
    private String brand; //Marque du produit
    private double price; //prix
    private String imageUrl; //Url de l'image

    //On fait le lien entre un produit et sa catégorie (ManyToOne = plusieurs produits pour une catégorie).

    @ManyToOne
    @JoinColumn(name = "category_id") // Crée une colonne dans la base de données qui relie les produits à leur catégorie.
    private Category category;

    // Constructeur vide (requis par JPA)
    public Product() {}

    //Constructeur avec paramètres
    public Product(Long id, String name, String brand, double price, String imageUrl, Category category) {
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.price = price;
        this.imageUrl = imageUrl;
        this.category = category;

    }
    // Getters et setters (Spring en a besoin pour manipuler les données)
    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}
    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    public String getBrand() {return brand;}
    public void setBrand(String brand) {this.brand = brand;}
    public double getPrice() {return price;}
    public void setPrice(double price) {this.price = price;}
    public String getImageUrl() {return imageUrl;}
    public void setImageUrl(String imageUrl) {this.imageUrl = imageUrl;}
    public Category getCategory() {return category;}
    public void setCategory(Category category) {this.category = category;}


}


