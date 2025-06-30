// On importe le décorateur @Injectable depuis le cœur du framework Angular.
// Il sert à marquer une classe comme un service, c’est-à-dire qu’elle pourra être injectée automatiquement dans d’autres composants.
import { Injectable } from '@angular/core';
//➡️ On importe HttpClient, c’est un outil fourni par Angular pour faire des requêtes HTTP (comme GET, POST, etc.) à un serveur (ex : ton backend Spring Boot).
import { HttpClient } from '@angular/common/http';
/**➡️ On importe le type Observable de la bibliothèque rxjs.
Un Observable est un objet qui émet des données dans le temps (comme un flux). Ici, il sert à gérer les réponses asynchrones du backend (car le backend répond après quelques millisecondes). */
import { Observable } from 'rxjs';


/**
 Ici, on définit une interface TypeScript appelée Category.
Une interface, c’est un contrat qui décrit la forme des données qu’on attend.
Cela permet à TypeScript de t’aider à éviter les erreurs.

👉 Cela signifie que chaque catégorie doit avoir :

un id de type number,

un name de type string,

un imageUrl de type string.
 */
export interface Category{
  id: number;
  name: String;
  imageUrl: String;
}

/**
 Le décorateur @Injectable dit à Angular que cette classe est un service injectable.

providedIn: 'root' signifie que le service sera disponible dans toute l'application, une seule instance sera créée automatiquement
 au lancement. C’est ce qu’on appelle le singleton.
 */
@Injectable({
  providedIn: 'root'
})

/**➡️ On définit une classe appelée CategoryService.
C’est dans cette classe qu’on va écrire toutes les fonctions pour aller chercher les catégories dans le backend. */
export class CategoryService {
  
  /**➡️ On crée une propriété privée (accessible seulement dans ce fichier) qui contient l’URL du backend Spring Boot.
Cette URL doit pointer vers l’endpoint qui renvoie la liste des catégories. */
  private baseUrl ="http://localhost:8080/categories";

  /**
    Le constructor est appelé automatiquement quand le service est créé.
On utilise l’injection de dépendance pour que Angular fournisse automatiquement une instance de HttpClient.
 👉 Grâce à ça, on peut faire : this.http.get(...)
   */
  constructor(private http: HttpClient) { }

  getCategories(): Observable<Category[]>{
    
    /**getCategories() : nom de la méthode publique. On pourra l'appeler depuis un composant Angular pour récupérer les données.

(): Observable<Category[]> : cette méthode retourne un flux de données de type Observable contenant une liste de catégories (Category[]).

this.http.get<Category[]>(this.baseUrl) :

http.get() → fait une requête GET vers l’URL du backend.

<Category[]> → on dit à TypeScript : « La réponse sera une liste de catégories ».

this.baseUrl → utilise l’URL qu’on a définie plus haut (http://localhost:8080/categories).

💡 Cela permet de récupérer les données en arrière-plan, sans bloquer l’application. */
    return this.http.get<Category[]>(this.baseUrl)
  }
}
