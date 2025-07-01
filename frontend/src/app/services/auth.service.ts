import { Injectable } from '@angular/core';
import { Router }     from '@angular/router';
import { BehaviorSubject } from 'rxjs';

import { PanierService }   from './panier.service';

@Injectable({ providedIn: 'root' })
export class AuthService {

  /** ───────── État interne sous forme de BehaviorSubject ───────── */
  private isLoggedInSubject = new BehaviorSubject<boolean>(!!localStorage.getItem('token'));
  private userNameSubject   = new BehaviorSubject<string | null>(localStorage.getItem('userName'));

  /** Observables publiques (si un composant veut écouter) */
  readonly isLoggedIn$ = this.isLoggedInSubject.asObservable();
  readonly userName$   = this.userNameSubject.asObservable();

  constructor(
    private router: Router,
    private panierService: PanierService
  ) {}

  /** -------- API conservée (on ne change PAS le nom) -------- */
  isLoggedIn(): boolean {
    return this.isLoggedInSubject.value;
  }

  /** Appelée depuis le login : enregistre et propage l’état */
  login(token: string, userName: string): void {
    localStorage.setItem('token', token);
    localStorage.setItem('userName', userName);

    this.isLoggedInSubject.next(true);
    this.userNameSubject.next(userName);
  }

  /** Appelée depuis le header ou ailleurs */
  logout(): void {
    localStorage.clear();                     // vide tout le LS
    this.panierService.viderPanier();         // vide le panier

    this.isLoggedInSubject.next(false);       // propage déconnexion
    this.userNameSubject.next(null);

    this.router.navigate(['/login']);
  }
}
