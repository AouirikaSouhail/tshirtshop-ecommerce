package com.tshirtshop.backend;

import com.tshirtshop.backend.model.Category;
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
	CommandLineRunner innitDatabase(CategoryRepository categoryRepository) {
		//C’est une façon de dire :
		//“Quand tu démarres l’appli, exécute ce qui est entre { ... }”.
		return args -> {

			if(categoryRepository.count() == 0) {
			//C’est une insertion en base de données.
			categoryRepository.save(new Category(null, "T-Shirts", "tshirt.jpg"));
			categoryRepository.save(new Category(null, "Sweats", "sweat.jpg"));
			categoryRepository.save(new Category(null, "Casquettes", "casquette.jpg"));
			}
			};
	}
	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

}
