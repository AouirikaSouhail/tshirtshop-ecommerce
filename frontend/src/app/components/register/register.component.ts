import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {
  firstName: string = '';
  lastName: string = '';
  adresse: string = '';
  codePostal: string = '';
  ville: string = '';
  email: string = '';
  password: string = '';

  errorMessage: string = '';
  successMessage: string = '';

  constructor(private http: HttpClient, private router: Router) {}

  onSubmit(): void {
    if (!this.firstName || !this.lastName || !this.adresse || !this.codePostal ||
        !this.ville || !this.email || !this.password) {
      this.errorMessage = "Tous les champs sont obligatoires.";
      return;
    }

    if (!this.email.includes('@')) {
      this.errorMessage = "Adresse email invalide.";
      return;
    }

    const registerData = {
      firstName: this.firstName,
      lastName: this.lastName,
      adresse: this.adresse,
      codePostal: this.codePostal,
      ville: this.ville,
      email: this.email,
      password: this.password
    };

    this.http.post('http://localhost:8080/api/register', registerData).subscribe({
      next: () => {
        this.successMessage = "Inscription réussie ! Redirection en cours…";
        this.errorMessage = '';
        setTimeout(() => this.router.navigate(['/login']), 2000);
      },
      error: (error) => {
        this.errorMessage = "Inscription échouée. Vérifiez vos informations ou réessayez.";
        this.successMessage = '';
        console.error(error);
      }
    });
  }
}
