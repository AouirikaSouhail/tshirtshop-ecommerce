import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { PanierService, PanierItem } from '../../services/panier.service';

@Component({
  selector: 'app-checkout',
  templateUrl: './checkout.component.html',
  styleUrls: ['./checkout.component.css']
})
export class CheckoutComponent implements OnInit {

  items: PanierItem[] = [];
  total = 0;
  // 🏦 liste très simple
  modesPaiement = ['Carte de crédit', 'Carte de débit', 'PayPal', 'Virement'];
  modeSelectionne = this.modesPaiement[0];

  constructor(private panierService: PanierService,
              private router: Router) {}

  ngOnInit(): void {
    this.items = this.panierService.getItems();
    this.total = this.panierService.getTotal();

    // Sécurité : panier vide → retour
    if (this.items.length === 0) {
      alert('Votre panier est vide');
      this.router.navigate(['/categories']);
    }
  }

  confirmer(): void {
    // 🥳 Ici tu appellerais le backend pour créer la commande
    // nous simulons la réussite :
    this.panierService.viderPanier();
    alert('Merci pour votre achat !');
    this.router.navigate(['/categories']);
  }
}
