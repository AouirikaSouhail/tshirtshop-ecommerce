import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { Produit } from './produit.service';

export interface PanierItem {
  produit: Produit;
  quantite: number;
}

@Injectable({ providedIn: 'root' })
export class PanierService {

  /* ------------ État interne ------------ */
  private items: PanierItem[] = [];

  /* Observable qui émet la liste à chaque mise à jour */
  private itemsChangedSubject = new BehaviorSubject<PanierItem[]>([]);
  readonly itemsChanged$       = this.itemsChangedSubject.asObservable();

  constructor() {
    this.loadFromStorage();          // hydrate + première émission
  }

  /* ------------ Getters simples ------------ */
  getItems(): PanierItem[] {
    return this.items;
  }

  getNombreTotalArticles(): number {
    return this.items.reduce((total, item) => total + item.quantite, 0);
  }

  getTotal(): number {
    return this.items.reduce(
      (total, item) => total + item.produit.price * item.quantite,
      0
    );
  }

  /* ------------ Mutations ------------ */
  ajouterProduit(produit: Produit, qte = 1): void {
    const idx = this.items.findIndex(i => i.produit.id === produit.id);

    if (idx > -1) {
      const souhaité = this.items[idx].quantite + qte;
      if (souhaité > produit.quantiteStock) {
        alert(`Stock insuffisant (max ${produit.quantiteStock}).`);
        return;
      }
      this.items[idx].quantite = souhaité;
    } else {
      if (qte > produit.quantiteStock) {
        alert(`Stock insuffisant (max ${produit.quantiteStock}).`);
        return;
      }
      this.items.push({ produit, quantite: qte });
    }

    this.persistAndNotify();
  }

  supprimerProduit(id: number): void {
    this.items = this.items.filter(i => i.produit.id !== id);
    this.persistAndNotify();
  }

  viderPanier(): void {
    this.items = [];
    this.persistAndNotify();
  }

  augmenterQuantite(id: number): void {
    const item = this.items.find(i => i.produit.id === id);
    if (item && item.quantite < item.produit.quantiteStock) {
      item.quantite++;
      this.persistAndNotify();
    }
  }

  diminuerQuantite(id: number): void {
    const item = this.items.find(i => i.produit.id === id);
    if (!item) { return; }

    if (item.quantite > 1) {
      item.quantite--;
    } else {
      this.supprimerProduit(id);
      return;
    }
    this.persistAndNotify();
  }

  /* ------------ Persistance + notification ------------ */
  private persistAndNotify(): void {
    localStorage.setItem('panier', JSON.stringify(this.items));
    this.itemsChangedSubject.next([...this.items]);
  }

  private loadFromStorage(): void {
    try {
      const data = localStorage.getItem('panier');
      this.items = data ? JSON.parse(data) : [];
    } catch {
      this.items = [];
    }
    this.itemsChangedSubject.next([...this.items]);
  }
}
