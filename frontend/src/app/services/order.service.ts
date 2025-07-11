import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class OrderService {

  private api = 'http://localhost:8080/orders';

  constructor(private http: HttpClient) {}

  /** Toutes les commandes de l’utilisateur connecté */
  getOrdersByUser(): Observable<any[]> {
    const token = localStorage.getItem('token') || '';
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    return this.http.get<any[]>(`${this.api}/by-user`, { headers });
  }
}
