/**Component permet de créer un composant Angular.
OnInit est une interface qu'on utilise pour exécuter du code au moment du chargement du composant (avec ngOnInit()). */
import { Component,OnInit } from '@angular/core';

/**On importe ActivatedRoute, un service d’Angular.
Il permet de lire les paramètres de l’URL (par exemple ici, :id dans /categorie/3). */
import { ActivatedRoute } from '@angular/router';

/**On importe :
Le service ProduitService pour faire des appels vers l’API (le backend).
Le type Produit pour dire que notre tableau produits contiendra des objets de type Produit. */
import { ProduitService,Produit } from '../../services/produit.service';

/**On déclare que ce fichier est un composant Angular.
 selector: nom du composant à utiliser dans le HTML (<app-produit-list>).
 templateUrl: fichier HTML qui sera affiché.
styleUrls: fichier CSS lié à ce composant. */

@Component({
  selector: 'app-produit-list',
  templateUrl: './produit-list.component.html',
  styleUrls: ['./produit-list.component.css']
})

/**On crée une classe TypeScript qui s’appelle ProduitListComponent.
Elle implémente OnInit, donc elle aura une méthode ngOnInit() qui sera appelée au chargement du composant. */
export class ProduitListComponent implements OnInit {
    
  
    produits : Produit[] = [];

    /**On déclare une variable idCategorie de type number.
     Le ! dit à TypeScript :
     “Ne t’inquiète pas, je vais l’initialiser avant de l’utiliser.” */
    idCategorie! : number;
     
    /**Cette variable est un booléen (vrai/faux).
    Elle permet d'afficher un message si aucun produit n’est retourné. */
    aucunProduit = false;
     
    /** Que fait le constructeur ?
       Angular injecte deux services :
       ActivatedRoute pour lire l’URL.
       ProduitService pour appeler le backend et récupérer les produits. */
    constructor(private route: ActivatedRoute, private produitService: ProduitService) {}


    /**Méthode exécutée automatiquement au chargement du composant. C’est ici qu’on déclenche nos actions. */
      ngOnInit(): void {
        
        /**Cette ligne récupère la valeur du paramètre id présent dans l’URL (défini dans le path: 'categorie/:id' du routeur), 
         *le convertit en nombre, puis le stocke dans la variable idCategorie pour l’utiliser dans le composant. */
         this.route.paramMap.subscribe(params =>
         {
          this.idCategorie = Number(params.get('id'));
         
        

        // Appelle le backend pour récupérer les produits selon l’ID de catégorie
       // puis les stocke dans `produits` une fois reçus.
      // Gère aussi le cas où la liste est vide ou s’il y a une erreur.

      this.produitService.getProduitsParCategorie(this.idCategorie).subscribe(
        (data)=>{
          this.produits = data;
          this.aucunProduit = data.length === 0;
        },
        (error)=>{
          console.error('Erreur lors du chargement des produits :', error);
          this.aucunProduit = true;
        }//Le error dans console.error(..., error) est l’objet contenant les détails de l’échec de la requête HTTP.
         //Il est automatiquement transmis par Angular quand la requête échoue.
      );
    }
  );
}
// ✅ Méthode bien à l’intérieur de la classe
  deleteProduct(id: number): void {
    if (confirm("Es-tu sûr de vouloir supprimer ce produit ?")) {
      this.produitService.deleteProduct(id).subscribe(() => {
        this.produits = this.produits.filter(p => p.id !== id);
      });
    }
  }
}
