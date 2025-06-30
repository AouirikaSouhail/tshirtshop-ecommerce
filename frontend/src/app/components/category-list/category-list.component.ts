/**Component → permet de transformer une classe en composant Angular.

OnInit → une interface qui oblige à définir une méthode ngOnInit() qui s’exécute automatiquement au démarrage du composant. */

import { Component,OnInit } from '@angular/core';

/**On importe le service CategoryService qui contient la méthode getCategories() pour aller chercher les données.
On importe le modèle Category, c’est une interface TypeScript qui décrit à quoi ressemble une catégorie 
(elle a un id, un nom, une image). */
import { CategoryService, Category } from '../../services/category.service';

/**C’est ce qu’on appelle un décorateur Angular.
Il donne des informations à Angular sur le composant :
Élément	Rôle
selector	Nom de la balise HTML personnalisée à utiliser pour ce composant (<app-category-list>)
templateUrl	Chemin vers le fichier HTML qui affiche les données
styleUrls	Chemin vers le fichier CSS pour le style visuel */
@Component({
  selector: 'app-category-list',
  templateUrl: './category-list.component.html',
  styleUrls: ['./category-list.component.css']  // ← ici le "s" à "styleUrls"
})

/**export → permet d’utiliser cette classe ailleurs dans Angular.
CategoryListComponent → c’est le nom de la classe (elle représente notre composant).
implements OnInit → on dit qu’on va utiliser la méthode ngOnInit() qui sera appelée automatiquement au lancement du composant. */
export class CategoryListComponent implements OnInit{

  /**categories est une variable publique (on peut l’utiliser dans le HTML).
Elle est de type Category[], c’est-à-dire un tableau de catégories.
Elle est initialisée vide [].🧠 On va y stocker les données reçues du backend. */
 categories : Category[] = []; // On crée une variable pour stocker les données

 /**constructor(...) → c’est une fonction spéciale appelée au moment où le composant est créé.
 private categoryService: CategoryService → on demande à Angular d’injecter le service automatiquement. */
 //Grâce à cela, on pourra écrire dans cette classe : this.categoryService.getCategories()
 constructor(private categoryService: CategoryService){
   
 }
/**ngOnInit() est une méthode spéciale d’Angular.
Elle est appelée automatiquement quand le composant est affiché pour la première fois.
C’est ici qu’on met le code d’initialisation, comme les appels à un serveur. */
 ngOnInit(): void {


  /**➡️ C’est le cœur du composant.
this.categoryService.getCategories() → appelle la méthode du service qui fait une requête HTTP GET vers ton backend Spring Boot.
.subscribe(...) → on s’abonne à la réponse. Comme la réponse arrive asynchronement (quelques millisecondes plus tard), on dit à Angular :
Quand les données sont prêtes, fais ce qu’il y a dans les { ... }.
 data => { this.categories = data; } :
 data contient la liste des catégories (venue du backend).
this.categories = data; → on les stocke dans notre variable pour pouvoir les afficher dans le HTML. */

     this.categoryService.getCategories().subscribe(data =>{
      this.categories = data;
      console.log(this.categories);
     });
 }
}
