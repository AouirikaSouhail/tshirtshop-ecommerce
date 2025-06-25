import { Component, OnInit } from '@angular/core';
import { PanierService, PanierItem } from '../../services/panier.service';
import { Produit } from '../../services/produit.service';

@Component({
  selector: 'app-panier',
  templateUrl: './panier.component.html',
  styleUrls: ['./panier.component.css']
})
export class PanierComponent implements OnInit {

  produitsDansPanier: PanierItem[] = []; // ✅ utilise l'interface bien exportée
  totalPanier: number = 0;

  constructor(private panierService: PanierService) {}

  ngOnInit(): void {
    this.produitsDansPanier = this.panierService.getItems();
    this.totalPanier = this.panierService.getTotal();
  }

  augmenter(id: number): void {
  this.panierService.augmenterQuantite(id);
  this.recalculerPanier();
}

diminuer(id: number): void {
  const item = this.produitsDansPanier.find(p => p.produit.id === id);
  if (item && item.quantite === 1) {
    const confirmation = confirm("La quantité va tomber à zéro. Supprimer ce produit du panier ?");
    if (!confirmation) {
      return;
    }
  }

  this.panierService.diminuerQuantite(id);
  this.recalculerPanier();
}


private recalculerPanier(): void {
  this.produitsDansPanier = this.panierService.getItems();
  this.totalPanier = this.panierService.getTotal();
}

}
