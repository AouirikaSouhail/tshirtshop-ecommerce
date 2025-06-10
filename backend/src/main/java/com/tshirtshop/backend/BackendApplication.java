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
				Category persoCat = new Category(null, "T-shirts personnalisés", "assets/tshirt-perso.jpg", null);
				Category humourCat = new Category(null, "T-shirts humour", "assets/tshirt-humour.jpg", null);
				Category popCat = new Category(null, "T-shirts culture pop", "assets/tshirt-pop.jpg", null);
				Category geekCat = new Category(null, "T-shirts geek / gaming", "assets/tshirt-geek.jpg", null);
				Category engagedCat = new Category(null, "T-shirts engagés", "assets/tshirt-engage.jpg", null);
				Category eventCat = new Category(null, "T-shirts événementiels", "assets/tshirt-event.jpg", null);

				// Sauvegarde dans la base
				categoryRepository.save(persoCat);
				categoryRepository.save(humourCat);
				categoryRepository.save(popCat);
				categoryRepository.save(geekCat);
				categoryRepository.save(engagedCat);
				categoryRepository.save(eventCat);

				//  Important : les produits ont besoin d'une catégorie déjà enregistrée.

				// Les produits doivent être liés à des catégories existantes

				// 🔹 Produits : T-shirts personnalisés
				productRepository.save(new Product(null, "T-shirt texte personnalisé", "YourStyle", 17.99, "assets/tshirt-texte.jpg", "Choisissez votre message et couleur. Impression recto ou recto/verso.", persoCat));
				productRepository.save(new Product(null, "T-shirt photo imprimée", "PicWear", 19.99, "assets/tshirt-photo.jpg", "Téléchargez votre photo, effet premium garanti.", persoCat));

				// 🔹 Produits : T-shirts humour
				productRepository.save(new Product(null, "T-shirt \"J'peux pas j’ai code\"", "GeekFun", 14.99, "assets/tshirt-code.jpg", "Idéal pour les développeurs débordés.", humourCat));
				productRepository.save(new Product(null, "T-shirt \"Chargement de motivation...\"", "Sarcastik", 15.00, "assets/tshirt-charge.jpg", "Barre de chargement imprimée.", humourCat));

				// 🔹 Produits : T-shirts culture pop
				productRepository.save(new Product(null, "T-shirt Star Wars Dark Vador", "RebelShop", 19.99, "assets/tshirt-vador.jpg", "\"Je suis ton t-shirt.\"", popCat));
				productRepository.save(new Product(null, "T-shirt Marvel Avengers Endgame", "HeroLine", 23.99, "assets/tshirt-avengers.jpg", "Impression grand format, logo officiel.", popCat));

				// 🔹 Produits : T-shirts geek / gaming
				//productRepository.save(new Product(null, "T-shirt \"I Paused My Game\"", "GameRush", 15.99, "assets/tshirt-pause.jfif", "Message gamer imprimé en blanc.", geekCat));
				//productRepository.save(new Product(null, "T-shirt Zelda Triforce", "NerdZone", 21.50, "assets/tshirt-zelda.jfif", "Imprimé doré du symbole Triforce.", geekCat));

				// 🔹 Produits : T-shirts engagés
				productRepository.save(new Product(null, "T-shirt \"Recycle or Die\"", "EcoLife", 16.50, "assets/tshirt-recycle.jpg", "Coton recyclé, message fort.", engagedCat));
				productRepository.save(new Product(null, "T-shirt \"Planète B : introuvable\"", "GreenWorld", 18.99, "assets/tshirt-planete.jfif", "Texte imprimé sur fond vert forêt.", engagedCat));

				// 🔹 Produits : T-shirts événementiels
				productRepository.save(new Product(null, "T-shirt \"Team Marié(e)\"", "WeddingFun", 16.00, "assets/tshirt-mariee.jpg", "Idéal pour EVG/EVJF.", eventCat));
				productRepository.save(new Product(null, "T-shirt \"Diplômé 2025\"", "GradZone", 18.00, "assets/tshirt-diplome.jfif", "Pour célébrer votre réussite.", eventCat));






			}
			};
	}
	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

}
