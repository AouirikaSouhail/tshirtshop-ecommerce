import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ProduitService, Produit } from '../../services/produit.service';
import { PanierService } from '../../services/panier.service';

@Component({
  selector: 'app-produit-list',
  templateUrl: './produit-list.component.html',
  styleUrls: ['./produit-list.component.css']
})
export class ProduitListComponent implements OnInit {

  produits: Produit[] = [];
  idCategorie!: number;
  aucunProduit = false;

  constructor(
    private route: ActivatedRoute,
    private produitService: ProduitService,
    private panierService: PanierService
  ) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      this.idCategorie = Number(params.get('id'));
      this.produitService.getProduitsParCategorie(this.idCategorie).subscribe({
        next: (data) => {
          this.produits = data;
          this.aucunProduit = data.length === 0;
        },
        error: (error) => {
          console.error('Erreur lors du chargement des produits :', error);
          this.aucunProduit = true;
        }
      });
    });
  }

  deleteProduct(id: number): void {
    if (confirm("Es-tu sûr de vouloir supprimer ce produit ?")) {
      this.produitService.deleteProduct(id).subscribe(() => {
        this.produits = this.produits.filter(p => p.id !== id);
      });
    }
  }

  ajouterAuPanier(produit: Produit): void {
    this.panierService.ajouterProduit(produit);
    alert(`Produit "${produit.name}" ajouté au panier !`);
  }
}
