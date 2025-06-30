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
    const totalSouhaite = this.items[index].quantite + quantite;
    const quantiteMax = produit.quantiteStock;

    if (totalSouhaite <= quantiteMax) {
      this.items[index].quantite = totalSouhaite;
    } else {
      alert(`Stock insuffisant. Il ne reste que ${quantiteMax - this.items[index].quantite} unité(s).`);
      return;
    }
  } else {
    if (quantite <= produit.quantiteStock) {
      this.items.push({ produit, quantite });
    } else {
      alert(`Stock insuffisant. Il ne reste que ${produit.quantiteStock} unité(s).`);
      return;
    }
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

augmenterQuantite(id: number): void {
  const item = this.items.find(p => p.produit.id === id);
  if (item) {
    if (item.quantite < item.produit.quantiteStock) {
      item.quantite++;
      this.sauvegarderDansLocalStorage();
    } else {
      alert("❌ Stock maximal atteint !");
    }
  }
}



diminuerQuantite(id: number): void {
  const item = this.items.find(p => p.produit.id === id);
  if (item && item.quantite > 1) {
    item.quantite--;
    this.sauvegarderDansLocalStorage();
  } else if (item && item.quantite === 1) {
    this.supprimerProduit(id);
  }
}

}
