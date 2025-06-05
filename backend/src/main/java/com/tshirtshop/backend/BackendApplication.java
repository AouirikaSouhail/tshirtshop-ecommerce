package com.tshirtshop.backend;

import com.tshirtshop.backend.model.Category;
import com.tshirtshop.backend.model.Product;
import com.tshirtshop.backend.repository.ProductRepository;
import java.util.List;
import com.tshirtshop.backend.repository.CategoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BackendApplication {
  // Ce code permet de remplir automatiquement la base de données avec 3 catégories dès que tu lances ton application Spring Boot.
	@Bean // Dit à Spring : “Je veux que ce code soit exécuté automatiquement au démarrage.”
	//C’est une fonction spéciale qui s’exécute juste après que l’application se lance.
	// Spring va te fournir automatiquement l’accès à la base de données des catégories.
	CommandLineRunner innitDatabase(CategoryRepository categoryRepository, ProductRepository productRepository) {
		//C’est une façon de dire :
		//“Quand tu démarres l’appli, exécute ce qui est entre { ... }”.
		return args -> {

			if(categoryRepository.count() == 0 && productRepository.count() == 0) {
			//C’est une insertion en base de données.
				Category tshirtCat = new Category(null, "T-Shirts", "tshirt.jpg",null);
				Category sweatCat = new Category(null, "Sweats", "sweat.jpg",null);
				Category casquetteCat = new Category(null, "Casquettes", "casquette.jpg",null);

				categoryRepository.save(tshirtCat);
				categoryRepository.save(sweatCat);
				categoryRepository.save(casquetteCat);

				// ⚠️ Important : les produits ont besoin d'une catégorie déjà enregistrée.
				productRepository.save(new Product(null, "T-Shirt rouge", "Nike", 19.99, "tshirt-rouge.jpg","T-shirt confortable en coton bio, coupe classique, logo Nike brodé.", tshirtCat));
				productRepository.save(new Product(null, "Sweat capuche", "Adidas", 39.99, "sweat.jpg","Sweat à capuche chaud et doux, idéal pour le sport ou la détente. Logo Adidas imprimé.", sweatCat));
				productRepository.save(new Product(null, "Casquette noire", "Puma", 14.99, "casquette.jpg","Casquette unisexe noire ajustable avec logo Puma brodé. Style urbain et léger.", casquetteCat));
			}
			};
	}
	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

}
