package com.tshirtshop.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.tshirtshop.backend.model.Product;
import com.tshirtshop.backend.model.Category;
import java.util.List;

// // Permet d’accéder aux produits dans la base de données
public interface ProductRepository extends JpaRepository<Product, Long> {
  // // Requête personnalisée pour trouver tous les produits d'une catégorie
  // Requête personnalisée pour trouver tous les produits d'une catégorie
   List<Product> findByCategory(Category category);
}
