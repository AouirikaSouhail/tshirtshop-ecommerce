import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

interface LoginResponse {
  token:      string;
  id:         number;
  firstName:  string;
  lastName:   string;
  adresse:    string;
  codePostal: string;
  ville:      string;
  email:      string;
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

  constructor(private http: HttpClient, private router: Router, private authService : AuthService) {}

  onSubmit(): void {
    const loginData = { email: this.email, password: this.password };
    

    this.http.post<LoginResponse>('http://localhost:8080/api/login', loginData)
      .subscribe({
        next: (res) => {
          // 🔐 Stockage sécurisé (localStorage ici : à améliorer plus tard)
         this.authService.login(res.token, `${res.firstName} ${res.lastName}`);
        localStorage.setItem('token',       res.token);
        localStorage.setItem('userId',      res.id.toString());
        localStorage.setItem('userEmail',   res.email);
        localStorage.setItem('userFirstName',  res.firstName);
        localStorage.setItem('userLastName',  res.lastName);
        localStorage.setItem('userName',    `${res.firstName} ${res.lastName}`);
        localStorage.setItem('userAdresse', res.adresse);
        localStorage.setItem('userCP',      res.codePostal);
        localStorage.setItem('userVille',   res.ville);
       

          this.successMessage = 'Connexion réussie ✅';
          setTimeout(() => this.router.navigate(['/categories']), 2000);
        },
        error: () => {
          this.errorMessage = 'Email ou mot de passe incorrect.';
        }
      });
  }
}
