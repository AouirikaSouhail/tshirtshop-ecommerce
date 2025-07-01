// header.component.ts
import { Component, OnDestroy, OnInit } from '@angular/core';
import { PanierService } from '../../services/panier.service';
import { AuthService } from '../../services/auth.service';
import { Subscription } from 'rxjs';
import { Router } from '@angular/router';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit, OnDestroy {
  nbArticles: number = 0;
  isLoggedIn: boolean = false;
  userName: string = '';
  private subscriptions: Subscription[] = [];

  constructor(
    private panierService: PanierService,
    private authService: AuthService,
    private router : Router
  ) {}

  ngOnInit(): void {
    this.subscriptions.push(
      this.authService.isLoggedIn$.subscribe(status => this.isLoggedIn = status),
      this.authService.userName$.subscribe(name => this.userName = name),
      this.panierService.itemsChanged$.subscribe(items => {
        this.nbArticles = items.reduce((total, item) => total + item.quantite, 0);
      })
    );

    this.nbArticles = this.panierService.getNombreTotalArticles();
  }

logout(): void {
  this.authService.logout();            // mise à jour du statut connecté/déconnecté
  this.panierService.viderPanier();     // vider le panier
  this.router.navigate(['/login']);     // rediriger vers la page de login
}
  
    goToProfile(): void {
    const userId = localStorage.getItem('userId');
    if (userId) {
      this.router.navigate([`/profile-edit/${userId}`]);
    } else {
      this.router.navigate(['/login']);
    }
  }
  ngOnDestroy(): void {
    this.subscriptions.forEach(s => s.unsubscribe());
  }
}
