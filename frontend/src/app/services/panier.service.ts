import { Injectable } from '@angular/core';
import { Produit } from './produit.service';

export interface PanierItem {
  produit: Produit;
  quantite: number;
}


@Injectable({
  providedIn: 'root'
})
export class PanierService {
  private items: PanierItem[] = [];

  constructor() {
    this.chargerDepuisLocalStorage();
  }

  getItems(): PanierItem[] {
    return this.items;
  }

  ajouterProduit(produit: Produit, quantite: number = 1): void {
    const index = this.items.findIndex(p => p.produit.id === produit.id);
    if (index > -1) {
      this.items[index].quantite += quantite;
    } else {
      this.items.push({ produit, quantite });
    }
    this.sauvegarderDansLocalStorage();
  }

  supprimerProduit(id: number): void {
    this.items = this.items.filter(p => p.produit.id !== id);
    this.sauvegarderDansLocalStorage();
  }

  viderPanier(): void {
    this.items = [];
    this.sauvegarderDansLocalStorage();
  }

  getTotal(): number {
    return this.items.reduce((total, item) => total + item.produit.price * item.quantite, 0);
  }

  private sauvegarderDansLocalStorage(): void {
    localStorage.setItem('panier', JSON.stringify(this.items));
  }

  private chargerDepuisLocalStorage(): void {
    const data = localStorage.getItem('panier');
    if (data) {
      this.items = JSON.parse(data);
    }
  }
}
