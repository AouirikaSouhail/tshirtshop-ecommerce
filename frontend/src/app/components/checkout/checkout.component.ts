import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { PanierService, PanierItem } from '../../services/panier.service';
import { CommandeService } from '../../services/commande.service';
import { AuthService } from '../../services/auth.service'; // à ajouter fonctionnel !!!!!
import { HttpClient } from '@angular/common/http'; // à ajouter fonctionnel !!!!!
import { loadStripe } from '@stripe/stripe-js';  // à ajouter fonctionnel !!!!!

const stripePromise = loadStripe('pk_test_8uQoCKArz5IEtBxJMRas9BxG'); // à ajouter fonctionnel !!!!!


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
    private router: Router,
    private authService: AuthService, // à ajouter fonctionnel !!!!!!
    private http : HttpClient  // à ajouter fonctionnel !!!!!!
  ) {}

  ngOnInit(): void {
    this.items = this.panierService.getItems();
    this.total = this.panierService.getTotal();

    if (this.items.length === 0) {
      alert('Votre panier est vide');
      this.router.navigate(['/categories']);
    }
  }
  
  payerAvecStripe(): void {
  const email = localStorage.getItem('userEmail') || 'invite@tshirtshop.dev';
  const items = this.items.map(i => ({
    productName: i.produit.name,
    quantity: i.quantite,
    price: i.produit.price
  }));

  const body = { email, items };

  this.http.post<{ id: string }>(
    'http://localhost:8080/api/stripe/create-checkout-session',
    body
  ).subscribe({
    next: async (res) => {
      const stripe = await stripePromise;
      await stripe?.redirectToCheckout({ sessionId: res.id });
    },
    error: (err) => {
      console.error('Échec Stripe :', err);
      alert('Une erreur est survenue avec Stripe.');
    }
  });
}

 /**  Ajout pas fonctionnel !!!!!!
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
   */

  confirmer(): void {
  if (!this.authService.isLoggedIn()) {
    alert("Vous devez être connecté pour payer !");
    this.router.navigate(['/login']);
    return;
  }

  // ➕ Stripe se lance ici si connecté
  this.payerAvecStripe();
}

}
