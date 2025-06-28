import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { loadStripe } from '@stripe/stripe-js';

import { PanierService, PanierItem } from '../../services/panier.service';

const stripePublicKey = 'pk_test_8uQoCKArz5IEtBxJMRas9BxG';   // ← ta vraie clé
const stripePromise   = loadStripe(stripePublicKey);

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
    private http: HttpClient              // ✅ injection de HttpClient
  ) {}

  /** charge le panier au démarrage */
  ngOnInit(): void {
    this.refreshPanier();
  }

  /** remet à jour le tableau + total */
  private refreshPanier(): void {
    this.produitsDansPanier = this.panierService.getItems();
    this.totalPanier        = this.panierService.getTotal();
  }

  /** +1 quantité */
  augmenter(id: number): void {
    this.panierService.augmenterQuantite(id);
    this.refreshPanier();
  }

  /** –1 quantité (ou supprime si 0) */
  diminuer(id: number): void {
    const item = this.produitsDansPanier.find(p => p.produit.id === id);
    if (item && item.quantite === 1 &&
        !confirm('La quantité va tomber à zéro. Supprimer ce produit ?')) {
      return;
    }
    this.panierService.diminuerQuantite(id);
    this.refreshPanier();
  }

  /** suppression directe */
  supprimer(id: number): void {
    if (confirm('Supprimer ce produit du panier ?')) {
      this.panierService.supprimerProduit(id);
      this.refreshPanier();
    }
  }

  /** bouton « Passer au paiement » (checkout maison) */
  validerCommande(): void {
    if (!localStorage.getItem('token')) {
      alert('Connectez-vous ou créez un compte pour finaliser la commande');
      this.router.navigate(['/login']);
      return;
    }
    this.router.navigate(['/checkout']);
  }

  /** bouton « Payer avec Stripe »  */
  payerAvecStripe(): void {
    // 1. transformer le panier au format attendu par le backend
    const itemsForStripe = this.produitsDansPanier.map(i => ({
      productName: i.produit.name,
      quantity:    i.quantite,
      price:       i.produit.price
    }));

    // 2. créer la session Stripe côté backend
    this.http
      .post<{ id: string }>(
        'http://localhost:8080/api/stripe/create-checkout-session',
        itemsForStripe
      )
      .subscribe({
        next: async (res) => {
          const stripe = await stripePromise;
          await stripe?.redirectToCheckout({ sessionId: res.id });
        },
        error: err => console.error(
          'Échec de création de session Stripe', err
        )
      });
  }
}
