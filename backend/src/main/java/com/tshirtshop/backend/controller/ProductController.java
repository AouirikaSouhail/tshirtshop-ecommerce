// Ce fichier est un contrôleur (controller) qui permet de gérer les routes liées aux produits
package com.tshirtshop.backend.controller;

// On importe les classes nécessaires
import com.tshirtshop.backend.dto.ProductDetails;
import com.tshirtshop.backend.model.Product;
import com.tshirtshop.backend.model.Category;
import com.tshirtshop.backend.model.Stock;
import com.tshirtshop.backend.repository.ProductRepository;
import com.tshirtshop.backend.repository.CategoryRepository;
import com.tshirtshop.backend.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @Autowired
    // On injecte automatiquement l’objet stockRepository (accès à la base de données des stocks)
    private StockRepository stockRepository;
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
    public ResponseEntity<ProductDetails> findById(@PathVariable Long id) {
        Optional<Product> optionalProduct = productRepository.findById(id);

        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();

            // On va chercher le stock du produit
            // var stock est possible depuis java 10 le compilateur peut reconnaitre automatiquement le type
            Stock stock = stockRepository.findByProduitId(product.getId());

            // ✨ On prépare le DTO avec toutes les infos, y compris le stock
            ProductDetails dto = new ProductDetails();
            dto.setId(product.getId());
            dto.setName(product.getName());
            dto.setBrand(product.getBrand());
            dto.setPrice(product.getPrice());
            dto.setImageUrl(product.getImageUrl());
            dto.setDescription(product.getDescription());
            dto.setCategoryName(product.getCategory().getName());
            dto.setQuantiteStock(stock != null ? stock.getQuantiteDisponible() : 0); // ✅

            return ResponseEntity.ok(dto); // ✅ On renvoie le DTO
        }

        return ResponseEntity.notFound().build();
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id,@RequestBody Product updatedProduct){
        //On cherche dans la base de données si un produit avec l’ID donné existe.
        Optional<Product> existingProduct = productRepository.findById(id);
        if(existingProduct.isPresent()){
            Product product = existingProduct.get();
            //Ces lignes mettent à jour le produit :
            // On prend les nouvelles valeurs que le frontend a envoyées et on les copie dans le produit existant.
            product.setName(updatedProduct.getName());
            product.setPrice(updatedProduct.getPrice());
            product.setDescription(updatedProduct.getDescription());
            product.setImageUrl(updatedProduct.getImageUrl());
            //On sauvegarde le produit modifié dans la base de données.
            productRepository.save(product);
            // On renvoie une réponse HTTP 200 (OK) avec le produit modifié.
            return ResponseEntity.ok(product);

        }
        //Sinon (produit pas trouvé)
        //Si aucun produit n’existe avec cet ID, on renvoie une réponse HTTP 404 (Not Found).
        return ResponseEntity.notFound().build();

        /**Résumé simple :
         Étape	Ce que fait ton code
         1️⃣	Reçoit un ID + un produit mis à jour
         2️⃣	Cherche si le produit existe
         3️⃣	S’il existe → met à jour les champs et enregistre
         4️⃣	Sinon → renvoie une erreur 404*/
    }
    //Ajouter un produit
    @PostMapping("/category/{categoryId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addProductToCategory(
            @PathVariable Long categoryId,
            @RequestBody Product newProduct) {

        Optional<Category> categoryOptional = categoryRepository.findById(categoryId);
        if (categoryOptional.isPresent()) {
            newProduct.setCategory(categoryOptional.get());
            Product savedProduct = productRepository.save(newProduct);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Catégorie avec ID " + categoryId + " non trouvée.");
        }
        /**
         * @PostMapping("/products") = Reçoit une requête POST (ajout) à l'URL /products
         @RequestBody Product newProduct = Récupère les données du nouveau produit envoyées par le frontend (en JSON)
         productRepository.save(newProduct) = Enregistre le produit en base de données
         ResponseEntity.status(HttpStatus.CREATED).body(...) = Retourne le produit avec le code 201 Created
         pour indiquer que tout s’est bien passé.*/
    }



    //Supprimer un produit (@DeleteMapping)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        Optional<Product> product = productRepository.findById(id);

        if (product.isPresent()) {
            productRepository.deleteById(id); // 🗑️ Supprime le produit avec cet ID
            return ResponseEntity.noContent().build(); // ✅ Réponse 204 : succès mais pas de contenu à renvoyer
        } else {
            return ResponseEntity.notFound().build(); // ❌ Produit non trouvé → erreur 404
        }
        /**@DeleteMapping("/products/{id}") = Reçoit une requête DELETE pour un produit avec un ID spécifique

         @PathVariable Long id = Récupère l'ID dans l'URL

         productRepository.findById(id) = Vérifie si le produit existe

         deleteById(id) = Supprime le produit s’il existe

         noContent() = Retourne 204 (succès sans message)

         notFound() = Retourne 404 si l’ID n’existe pas */
    }


}
