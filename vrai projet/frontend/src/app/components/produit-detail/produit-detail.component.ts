import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Produit, ProductDetails, ProduitService } from '../../services/produit.service';
import { PanierService } from '../../services/panier.service';

@Component({
  selector: 'app-produit-detail',
  templateUrl: './produit-detail.component.html',
// "s" a styleUrl Parce que Angular attend un tableau de chemins vers des fichiers CSS → même s’il y en a un seul !
  styleUrls: ['./produit-detail.component.css'] 
})
export class ProduitDetailComponent implements OnInit {

  produit!: ProductDetails; // Variable pour stocker le produit chargé
  id!: number;       // L’ID récupéré depuis l’URL

  constructor(
    private route: ActivatedRoute,
    private produitService: ProduitService,
    private panierService: PanierService
  ) {}

  ngOnInit(): void {
    // 🔄 1. Récupérer l'ID du produit depuis l'URL
    this.route.paramMap.subscribe(params => {
      this.id = Number(params.get('id'));

      // 📡 2. Appeler le backend via le service Angular
      this.produitService.getProduitParId(this.id).subscribe(
        (data) => {
         // ✅ Vérifie s’il y a déjà ce produit dans le panier
        const itemDansPanier = this.panierService.getItems()
          .find(item => item.produit.id === data.id);

        const quantiteDansPanier = itemDansPanier ? itemDansPanier.quantite : 0;

        // ✅ Réduction visuelle du stock
        this.produit = {
          ...data,
          quantiteStock: data.quantiteStock - quantiteDansPanier
        };
      },
        (error) => {
          console.error('Erreur lors du chargement du produit :', error);
        }
      );
    });
  }
 ajouterAuPanier(): void {
  if (this.produit) {
    if (this.produit.quantiteStock <= 0) {
      alert("Ce produit est en rupture de stock !");
      return;
    }
    const produitConverti: Produit = {
      id: this.produit.id,
      name: this.produit.name,
      brand: this.produit.brand,
      price: this.produit.price,
      imageUrl: this.produit.imageUrl,
      description: this.produit.description,
      category: {
        id: 0, // 💡 On ne connaît pas l'id ici, donc on met 0 ou -1
        name: this.produit.categoryName
      },
      quantiteStock: this.produit.quantiteStock
    };

    this.panierService.ajouterProduit(produitConverti);
    this.produit.quantiteStock--; // 🟡 Mise à jour visuelle.  Cela ne modifie pas la base de données, juste l’affichage temporaire du stock.
    alert('Produit ajouté au panier !');
  }
}
/**Pourquoi ça fonctionne ?
ProductDetails est le type reçu du backend avec juste categoryName.

Produit est le type utilisé pour le panier, qui attend un vrai objet category (avec un id et un name).

Ici, on crée manuellement un objet conforme à Produit, et tout fonctionne parfaitement.

 */

}
