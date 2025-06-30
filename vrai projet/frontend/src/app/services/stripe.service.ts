import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class StripeService {
  constructor(private http: HttpClient) {}

  createCheckoutSession(items: any[], email: string) {
  return this.http.post<any>('http://localhost:8080/api/stripe/create-checkout-session', {
    items: items.map(i => ({
      productName: i.produit.name,
      price: i.produit.price,
      quantity: i.quantite
    })),
    email: email // ✅
  });
}

}
