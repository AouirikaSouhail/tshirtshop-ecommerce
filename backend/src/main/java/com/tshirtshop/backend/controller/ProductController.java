// Ce fichier est un contrôleur (controller) qui permet de gérer les routes liées aux produits
package com.tshirtshop.backend.controller;

// On importe les classes nécessaires
import com.tshirtshop.backend.model.Product;
import com.tshirtshop.backend.model.Category;
import com.tshirtshop.backend.repository.ProductRepository;
import com.tshirtshop.backend.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

//// Cette annotation indique que ce fichier est un "REST Controller", c’est-à-dire qu’il va gérer les requêtes HTTP (comme GET, POST…).
@RestController

// // Toutes les routes dans ce fichier commenceront par /products dans l’URL
@RequestMapping("/products")
@CrossOrigin(origins = "http://localhost:4200") // ⬅️ Autorise Angular à accéder au backend
public class ProductController {

    @Autowired // Permet au controller un accès a la BDD
    // On injecte automatiquement l’objet productRepository (accès à la base de données des produits)
    private ProductRepository productRepository;

    @Autowired
    // On injecte automatiquement l’objet categoryRepository (accès à la base de données des catégories)
    private CategoryRepository categoryRepository;

    // Route GET : /products/by-category/{categoryId}
    // Elle permet d’afficher les produits d’une catégorie spécifique
    @GetMapping("/by-category/{categoryId}")
    public List<Product> findByCategory(@PathVariable Long categoryId){
        Optional<Category> optionalCategory = categoryRepository.findById(categoryId);

        if(optionalCategory.isPresent()){
            // Si la catégorie existe, on récupère l’objet category
            Category category = optionalCategory.get();
            // Puis on cherche tous les produits liés à cette catégorie
            return productRepository.findByCategory(category);
        }
        else{
            // Si la catégorie n’existe pas (id incorrect), on renvoie une liste vide
            return List.of();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> findById(@PathVariable Long id){
       Optional<Product> optionalProduct = productRepository.findById(id);
       if(optionalProduct.isPresent()){
             Product product = optionalProduct.get();
           // Si le produit existe → on le renvoie avec un code 200 OK
           return ResponseEntity.ok(product);
       }
       else{
           // Sinon → on renvoie une erreur 404 Not Found
           return ResponseEntity.notFound().build();
       }
    }

}
