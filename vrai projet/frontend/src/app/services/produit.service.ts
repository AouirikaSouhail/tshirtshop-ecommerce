// On importe le décorateur @Injectable, qui permet de déclarer un service Angular.
import { Injectable } from '@angular/core';
//HttpClient est le service fourni par Angular pour envoyer des requêtes HTTP (GET, POST, etc.).
import { HttpClient } from '@angular/common/http';  
//Observable est utilisé pour attendre les réponses du serveur (asynchrones) — comme un flux de données qui arrive plus tard.
import { Observable } from 'rxjs';

/**Définition de l’interface Produit
 * ➡️ Une interface en TypeScript sert à décrire la forme d’un objet.
Ici, on dit :
"Un produit est un objet avec un id, un name, une brand, un price, un imageUrl, et une category."
La category est elle-même un objet imbriqué, avec un id et un name.
 */
export interface Produit {
  id: number;
  name: string;
  brand: string;
  price: number;
  imageUrl: string;
  description: string;
  category: {
    id: number;
    name: string;
  };
  quantiteStock: number; 
}

export interface ProductDetails {
  id: number;
  name: string;
  brand: string;
  price: number;
  imageUrl: string;
  description: string;
  categoryName: string; // <--- string seul ici, pas un objet
  quantiteStock: number;
}


/**Déclaration du service
 * ➡️ Ce décorateur indique à Angular :
"Ce service sera disponible partout dans l'application (injection globale)."
Tu pourras l’utiliser dans n’importe quel composant sans avoir à le déclarer dans un module.
 */
@Injectable({
  providedIn: 'root'
})

/**On déclare une classe ProduitService.
La variable baseUrl contient l’URL de base du backend pour les produits. */
export class ProduitService {
 
  private baseUrl = "http://localhost:8080/products";

  /**
    Tu dis à Angular : « Donne-moi automatiquement un objet HttpClient quand tu construis ce service. » 
    Et Angular s’en charge, grâce à l’injection de dépendance.
   */
  constructor(private http: HttpClient) { }

/**Cette fonction ne retourne pas immédiatement les données, mais un objet spécial appelé Observable, 
 *qui contiendra plus tard une liste de produits.
 Pourquoi ? Parce que la réponse vient d’un serveur, donc il faut attendre un peu → elle est asynchrone. */
   // Récupère les produits d’une catégorie spécifique

  getProduitsParCategorie(idCategorie: number) : Observable<Produit[]>{
    return this.http.get<Produit[]>(`${this.baseUrl}/by-category/${idCategorie}`); 
  }
//Cette ligne dit à Angular :
//« Envoie une requête HTTP GET à l’URL construite dynamiquement selon la catégorie demandée,
// et dis-moi que je recevrai une liste de produits en réponse. »


 
 // Récupère un produit précis par son ID (pour US03)
 // pour (US10) Tu récupéreras maintenant un objet complet avec tous les détails du produit + la quantité en stock.
   
   getProduitParId(id: number): Observable<ProductDetails> {
  return this.http.get<ProductDetails>(`${this.baseUrl}/${id}`);
}



   // ✅ Tu ajoutes cette méthode pour la suppression :
  deleteProduct(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }

   
  }




/**Un décorateur est un mot-clé qui commence par @, placé au-dessus d’une classe, d’une méthode ou d’une propriété, pour donner des informations supplémentaires à Angular.
C’est comme coller une étiquette sur un objet pour dire :
« Ce truc, c’est un composant ! »
« Cette classe, c’est un service ! »
« Cette fonction, injecte-lui des dépendances ! »

@Injectable() est un décorateur Angular qui dit :
« Cette classe peut recevoir et fournir des dépendances grâce au système d’injection de dépendance. »
En clair, ça permet à Angular de :
Fournir automatiquement des objets dans le constructeur (comme HttpClient)
Partager ce service dans toute l’appli si on le met providedIn: 'root'
 */