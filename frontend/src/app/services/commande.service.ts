import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { PanierItem } from './panier.service';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class CommandeService {
  private apiUrl = 'http://localhost:8080/orders';

  constructor(private http: HttpClient) {}

  passerCommande(panier: PanierItem[]): Observable<any> {
    const token = localStorage.getItem('token') ?? '';

    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });

    const payload = {
      items: panier.map(item => ({
        produitId: item.produit.id,
        quantite: item.quantite
      }))
    };

    /** ⬇️ on poste directement sur /orders (plus de /create) */
    return this.http.post(this.apiUrl, payload, { headers });
  }
}


