// Déclare que ce fichier appartient au dossier model
package com.tshirtshop.backend.model;
// On importe des outils de Spring JPA pour faire le lien avec MySQL
import jakarta.persistence.*;
import com.tshirtshop.backend.model.Product;
import java.util.List;

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

    //Cette ligne établit une relation entre une catégorie et plusieurs produits.
    // mappedBy = "category" fait référence à l'attribut  'category' dans la classe Product.
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<Product> products;

   // Constructeur
   public Category() {}
  public Category(Long id, String name, String imageUrl) {
    this.id = id;
    this.name = name;
    this.imageUrl = imageUrl;
  }
  public Category(Long id, String name, String imageUrl,List<Product> products) {
    this.id = id;
    this.name = name;
    this.imageUrl = imageUrl;
    this.products = products;
  }


  // Getters et Setters Permettent à Spring (et toi) de lire et modifier les données

    public Long getId(){return id;}
    public void setId(Long id){this.id = id;}
    public String getName(){return name;}
    public void setName(String name){this.name = name;}
    public String getImageUrl(){return imageUrl;}
    public void setImageUrl(String imageUrl){this.imageUrl = imageUrl;}
    public List<Product> getProducts(){return products;}
   public void setProducts(List<Product> products){this.products = products;}
}
/** Annotation	Explication
@OneToMany	Une catégorie peut avoir plusieurs produits.
mappedBy="category"	Dit à Spring que la relation est déjà définie côté Product dans le champ category.
cascade = CascadeType.ALL	Si tu supprimes une catégorie, tous les produits associés sont supprimés aussi.
List<Product> products	On crée une liste qui contient tous les produits liés à cette catégorie.

**/