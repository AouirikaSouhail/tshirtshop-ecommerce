import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { PanierService, PanierItem } from '../../services/panier.service';
import { CommandeService } from '../../services/commande.service';

@Component({
  selector: 'app-checkout',
  templateUrl: './checkout.component.html',
  styleUrls: ['./checkout.component.css']
})
export class CheckoutComponent implements OnInit {

  items: PanierItem[] = [];
  total = 0;
  modesPaiement = ['Carte de crédit', 'Carte de débit', 'PayPal', 'Virement'];
  modeSelectionne = this.modesPaiement[0];

  constructor(
    private panierService: PanierService,
    private commandeService: CommandeService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.items = this.panierService.getItems();
    this.total = this.panierService.getTotal();

    if (this.items.length === 0) {
      alert('Votre panier est vide');
      this.router.navigate(['/categories']);
    }
  }

  confirmer(): void {
    this.commandeService.passerCommande(this.items).subscribe({
      next: () => {
        this.panierService.viderPanier();
        alert('Merci pour votre commande !');
        this.router.navigate(['/categories']);
      },
      error: (err) => {
        console.error('Erreur lors de la commande :', err);
        alert('Une erreur est survenue lors de la validation de votre commande.');
      }
    });
  }
}
