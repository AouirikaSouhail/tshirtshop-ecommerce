import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';

interface LoginResponse {
  token: string;
  id: number;
  name: string;
  email: string;
}

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {

  email = '';
  password = '';
  errorMessage = '';
  successMessage = '';

  constructor(private http: HttpClient, private router: Router) {}

  onSubmit(): void {
    const loginData = { email: this.email, password: this.password };

    this.http.post<LoginResponse>('http://localhost:8080/api/login', loginData)
      .subscribe({
        next: (res) => {
          localStorage.setItem('token',    res.token);
          localStorage.setItem('userId',   res.id.toString());
          localStorage.setItem('userName', res.name);
          localStorage.setItem('userEmail',res.email);

          this.successMessage = 'Connexion réussie ✅';
          setTimeout(() => this.router.navigate(['/categories']), 2000);
        },
        error: () => {
          this.errorMessage = 'Email ou mot de passe incorrect.';
        }
      });
  }
}
