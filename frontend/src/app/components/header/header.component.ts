import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {
  userName: string | null = '';

  constructor(public router: Router) {}

  ngOnInit(): void {
    this.userName = localStorage.getItem('userName');
  }

  goToProfile(): void {
    const userId = localStorage.getItem('userId');
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
