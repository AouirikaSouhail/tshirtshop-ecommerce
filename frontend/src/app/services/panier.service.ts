// src/app/services/panier.service.ts
import { Injectable }      from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { Produit }         from './produit.service';

export interface PanierItem { produit: Produit; quantite: number; }

@Injectable({ providedIn: 'root' })
export class PanierService {

  /* État interne ----------------------------------------------------------- */
  private items: PanierItem[] = [];

  /* Observable public : émet à chaque modification ------------------------ */
  private itemsChangedSubject = new BehaviorSubject<PanierItem[]>([]);
  readonly itemsChanged$       = this.itemsChangedSubject.asObservable();

  constructor() { this.loadFromStorage(); }

  /* Getters ---------------------------------------------------------------- */
  getItems()               { return this.items; }
  getNombreTotalArticles() { return this.items.reduce((t,i)=>t+i.quantite, 0); }
  getTotal()               { return this.items.reduce(
                               (t,i)=>t+i.produit.price*i.quantite, 0); }

  /* Mutations -------------------------------------------------------------- */
  ajouterProduit(produit: Produit, qte = 1): void {
    const idx = this.items.findIndex(i => i.produit.id === produit.id);
    if (idx > -1) {
      const souhaité = this.items[idx].quantite + qte;
      if (souhaité > produit.quantiteStock) { alert(`Stock insuffisant`); return; }
      this.items[idx].quantite = souhaité;
    } else {
      if (qte > produit.quantiteStock) { alert(`Stock insuffisant`); return; }
      this.items.push({ produit, quantite: qte });
    }
    this.persistAndNotify();
  }

  supprimerProduit(id: number) { this.items = this.items.filter(i => i.produit.id !== id); this.persistAndNotify(); }
  viderPanier()               { this.items = []; this.persistAndNotify(); }

  augmenterQuantite(id: number) {
    const it = this.items.find(i => i.produit.id === id);
    if (it && it.quantite < it.produit.quantiteStock) { it.quantite++; this.persistAndNotify(); }
  }
  diminuerQuantite(id: number) {
    const it = this.items.find(i => i.produit.id === id);
    if (!it) { return; }
    if (it.quantite > 1) { it.quantite--; }
    else { this.supprimerProduit(id); return; }
    this.persistAndNotify();
  }

  /* Persistance + notification -------------------------------------------- */
  private persistAndNotify() {
    localStorage.setItem('panier', JSON.stringify(this.items));
    this.itemsChangedSubject.next([...this.items]);
  }
  private loadFromStorage() {
    try { this.items = JSON.parse(localStorage.getItem('panier') || '[]'); }
    catch { this.items = []; }
    this.itemsChangedSubject.next([...this.items]);
  }
}
