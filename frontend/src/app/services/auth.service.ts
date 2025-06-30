import { Injectable } from '@angular/core';
import { Router } from '@angular/router';  // a ajouter fonnctionnelle !!!!!  2
import { PanierService } from './panier.service';  // a ajouter fonnctionnelle !!!!! 2

@Injectable({ providedIn: 'root' })
export class AuthService {
  // a jouter fonctionnel !!!! 2
  constructor(private router: Router, private panierService: PanierService) {}


  isLoggedIn(): boolean {
    const token = localStorage.getItem('token');
    return !!token; // true si token existe
  }
    

  logout(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('userEmail');
    
    this.panierService.viderPanier(); // ✅ Vide le panier à la déconnexion

    this.router.navigate(['/login']);
  }
  
  
  // a jouter fonctionnel !!!!  2
}

//ajouter fonctionnel  !!!!!!  1