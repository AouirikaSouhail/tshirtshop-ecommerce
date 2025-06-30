package com.tshirtshop.backend.controller;


/**
On dit à Java : “Je vais utiliser :

ma classe Category

mon repository (la connexion à la base de données)

des outils pour créer des routes web (@RestController, @GetMapping…)”
* */
import com.tshirtshop.backend.model.Category;
import com.tshirtshop.backend.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

//On dit : "Ce fichier est un contrôleur web REST."
//REST = style d’API basé sur HTTP (GET, POST, PUT, DELETE)
@RestController

// On dit : "Toutes les routes dans ce fichier commenceront par /categories."
@RequestMapping("/categories")
@CrossOrigin(origins = "http://localhost:4200") // ⬅️ Autorise Angular à accéder au backend
public class CategoryController {
    //On dit à Spring Boot :
    //“Je veux utiliser le repository pour aller lire dans la base de données.”
    //Spring s’en occupe automatiquement : c’est ça la magie de @Autowired.
    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping
    public List<Category> getAllCategories(){
        return categoryRepository.findAll();
    }
}
