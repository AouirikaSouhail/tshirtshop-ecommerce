import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class AuthService {

  private loggedInSub = new BehaviorSubject<boolean>(this.hasToken());
  /** observable public pour s’abonner dans Header */
  readonly loggedIn$: Observable<boolean> = this.loggedInSub.asObservable();

  /* ============ getters simples ============ */
  isLoggedIn(): boolean          { return this.hasToken(); }
  getToken(): string             { return localStorage.getItem('token') || ''; }
  getUserEmail(): string         { return localStorage.getItem('email') || ''; }
  getUserFirstName(): string     { return localStorage.getItem('firstName') || ''; }
  getUserLastName(): string      { return localStorage.getItem('lastName')  || ''; }
  getUserId(): string            { return localStorage.getItem('id')        || ''; }

  /* ============ login / logout ============ */
  /** À appeler dans LoginComponent après la réponse backend */
  login(payload: {
    token: string;
    id: string|number;
    firstName: string;
    lastName: string;
    email: string;
  }): void {
    localStorage.setItem('token',     payload.token);
    localStorage.setItem('id',        String(payload.id));
    localStorage.setItem('firstName', payload.firstName);
    localStorage.setItem('lastName',  payload.lastName);
    localStorage.setItem('email',     payload.email);
    this.loggedInSub.next(true);
  }
 
  
  logout(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('id');
    localStorage.removeItem('firstName');
    localStorage.removeItem('lastName');
    localStorage.removeItem('email');
    this.loggedInSub.next(false);
  }

  /* ============ interne ============ */
  private hasToken(): boolean {
    return !!localStorage.getItem('token');
  }
}
