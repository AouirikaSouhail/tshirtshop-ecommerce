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
}
