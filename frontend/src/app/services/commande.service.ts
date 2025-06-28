import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { PanierItem } from './panier.service';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CommandeService {
  private apiUrl = 'http://localhost:8080/orders';

  constructor(private http: HttpClient) {}

  passerCommande(panier: PanierItem[]): Observable<any> {
    // On prépare un objet avec les infos nécessaires
    const payload = {
      items: panier.map(item => ({
        produitId: item.produit.id,
        quantite: item.quantite
      }))
    };

    return this.http.post(`${this.apiUrl}/create`, payload);
  }
}
