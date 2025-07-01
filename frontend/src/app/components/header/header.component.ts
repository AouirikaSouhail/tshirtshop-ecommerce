import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';

import { PanierService } from '../../services/panier.service';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit, OnDestroy {

  isLoggedIn = false;
  userName: string | null = null;
  nbArticles = 0;

  private subs: Subscription[] = [];

  constructor(
    private router: Router,
    private panierService: PanierService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.isLoggedIn = this.authService.isLoggedIn();
    this.userName = localStorage.getItem('userName');
    this.nbArticles = this.panierService.getNombreTotalArticles();

    this.subs.push(
      this.panierService.itemsChanged$.subscribe(() => {
        this.nbArticles = this.panierService.getNombreTotalArticles();
      })
    );
  }

  goToProfile(): void {
    const userId = localStorage.getItem('userId');
    if (userId) {
      this.router.navigate([`/profile-edit/${userId}`]);
    }
  }

  logout(): void {
    this.panierService.viderPanier();
    this.authService.logout();
    this.isLoggedIn = false;
    this.userName = null;
    this.nbArticles = 0;
  }

  ngOnDestroy(): void {
    this.subs.forEach(sub => sub.unsubscribe());
  }
}
