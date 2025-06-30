import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environement';

@Injectable({ providedIn: 'root' })
export class OrderService {

  private readonly BASE_URL = `${environment.apiBaseUrl}/orders`;

  constructor(private http: HttpClient) {}

  getOrdersByUser(): Observable<any[]> {
    return this.http.get<any[]>(`${this.BASE_URL}/me`);
  }
}
