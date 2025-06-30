// src/app/services/produit.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

/* ---------------------------- Modèles ---------------------------- */
export interface Produit {
  id: number;
  name: string;
  brand: string;
  price: number;
  imageUrl: string;
  description: string;
  category: { id: number; name: string };
  quantiteStock: number;
}

export interface ProductDetails {
  id: number;
  name: string;
  brand: string;
  price: number;
  imageUrl: string;
  description: string;
  categoryName: string;   // string seul
  quantiteStock: number;
}

/* ---------------------------- Service ---------------------------- */
@Injectable({ providedIn: 'root' })
export class ProduitService {

  /** URL racine de toutes les routes « produits » */
  private readonly baseUrl = 'http://localhost:8080/products';

  constructor(private http: HttpClient) {}

  /** Liste des produits d’une catégorie */
  getProduitsParCategorie(idCategorie: number): Observable<Produit[]> {
    return this.http.get<Produit[]>(`${this.baseUrl}/by-category/${idCategorie}`);
  }

  /** Détail d’un produit (US 03 / US 10) */
  getProduitParId(id: number): Observable<ProductDetails> {
    // ⬇️ correction : baseUrl au lieu de apiUrl
    return this.http.get<ProductDetails>(`${this.baseUrl}/${id}`);
  }

  /** Alias conservé pour compatibilité avec l’ancien nom */
  getProduitById(id: number): Observable<ProductDetails> {
    return this.getProduitParId(id);
  }

  /** Suppression (admin) */
  deleteProduct(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}
