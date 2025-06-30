import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent {
  userName: string | null = '';

  constructor(public router: Router) {}

  ngOnInit(): void {
    this.userName = localStorage.getItem('userName');
  }

  goToProfile(): void {
  const userId = localStorage.getItem('userId'); // tu avais sauvegardé ça après le login
  if (userId) {
    this.router.navigate([`/profile-edit/${userId}`]);
  }
}

  logout(): void {
    localStorage.clear();
    this.router.navigate(['/login']);
  }

  isLoggedIn(): boolean {
    return localStorage.getItem('token') !== null;
  }
}
