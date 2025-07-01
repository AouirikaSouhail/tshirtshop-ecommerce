// src/app/services/auth.service.ts
import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { PanierService } from './panier.service';
import { Route, Router } from '@angular/router';
@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private isLoggedInSubject = new BehaviorSubject<boolean>(this.hasToken());
  isLoggedIn$ = this.isLoggedInSubject.asObservable();

  private userNameSubject = new BehaviorSubject<string>(this.getUserNameFromStorage());
  userName$ = this.userNameSubject.asObservable();

  constructor(private panierService : PanierService, private router : Router){}

  login(token: string, userName: string): void {
    localStorage.setItem('token', token);
    localStorage.setItem('userName', userName);
    this.isLoggedInSubject.next(true);
    this.userNameSubject.next(userName);
  }

  logout(): void {
    localStorage.clear();
    this.isLoggedInSubject.next(false);
    this.userNameSubject.next('');
    this.panierService.viderPanier();
    this.router.navigate(['/login']);
  }

  // Utile pour les composants qui ne réagissent pas avec des observables
  getUserName(): string {
    return localStorage.getItem('userName') || '';
  }

  private hasToken(): boolean {
    return !!localStorage.getItem('token');
  }

  private getUserNameFromStorage(): string {
    return localStorage.getItem('userName') || '';
  }
}
