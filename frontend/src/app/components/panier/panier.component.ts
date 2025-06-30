import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { loadStripe } from '@stripe/stripe-js';

import { PanierService, PanierItem } from '../../services/panier.service';

const stripePromise = loadStripe('pk_test_8uQoCKArz5IEtBxJMRas9BxG');

interface CheckoutRequest {
  email: string;
  items: { productName: string; quantity: number; price: number }[];
}

@Component({
  selector: 'app-panier',
  templateUrl: './panier.component.html',
  styleUrls: ['./panier.component.css']
})
export class PanierComponent implements OnInit {

  produitsDansPanier: PanierItem[] = [];
  totalPanier = 0;

  constructor(
    private panierService: PanierService,
    private router: Router,
    private http: HttpClient
  ) {}

  ngOnInit(): void {
    this.refreshPanier();
  }

  private refreshPanier(): void {
    this.produitsDansPanier = this.panierService.getItems();
    this.totalPanier        = this.panierService.getTotal();
  }

  augmenter(id: number): void {
    this.panierService.augmenterQuantite(id);
    this.refreshPanier();
  }

  diminuer(id: number): void {
    const item = this.produitsDansPanier.find(p => p.produit.id === id);
    if (item && item.quantite === 1 &&
        !confirm('La quantité va tomber à zéro. Supprimer ce produit ?')) {
      return;
    }
    this.panierService.diminuerQuantite(id);
    this.refreshPanier();
  }

  supprimer(id: number): void {
    if (confirm('Supprimer ce produit du panier ?')) {
      this.panierService.supprimerProduit(id);
      this.refreshPanier();
    }
  }

  validerCommande(): void {
    if (!localStorage.getItem('token')) {
      alert('Connectez-vous ou créez un compte pour finaliser la commande');
      this.router.navigate(['/login']);
      return;
    }
    this.router.navigate(['/checkout']);
  }

  payerAvecStripe(): void {
    const body: CheckoutRequest = {
      email: localStorage.getItem('userEmail') || 'invite@tshirtshop.dev',
      items: this.produitsDansPanier.map(i => ({
        productName: i.produit.name,
        quantity:    i.quantite,
        price:       i.produit.price
      }))
    };

    this.http.post<{ id: string }>(
      'http://localhost:8080/api/stripe/create-checkout-session',
      body
    ).subscribe({
      next: async res => {
        const stripe = await stripePromise;
        await stripe?.redirectToCheckout({ sessionId: res.id });
      },
      error: err => {
        console.error('Échec de création de session Stripe', err);
        alert('Une erreur est survenue lors du paiement Stripe.');
      }
    });
  }
}
