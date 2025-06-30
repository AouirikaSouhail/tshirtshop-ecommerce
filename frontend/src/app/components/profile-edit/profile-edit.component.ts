import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';

export interface User {
  name: string;
  email: string;
  password: string;
}

@Component({
  selector: 'app-profile-edit',
  templateUrl: './profile-edit.component.html',
  styleUrls: ['./profile-edit.component.css']
})
export class ProfileEditComponent implements OnInit {

  id!: number;
  name: string = '';
  email: string = '';
  password: string = '';
  successMessage: string = '';
  errorMessage: string = '';

  constructor(
    private route: ActivatedRoute,
    private http: HttpClient,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.id = Number(this.route.snapshot.paramMap.get('id'));

    this.http.get<User>(`http://localhost:8080/api/user/${this.id}`).subscribe({
      next: (data) => {
        this.name = data.name;
        this.email = data.email;
        // Mieux vaut ne pas préremplir le mot de passe pour des raisons de sécurité
        this.password = ''; 
      },
      error: (error) => {
        this.errorMessage = "Impossible de charger les informations utilisateur.";
        console.error(error);
      }
    });
  }

  onSubmit(): void {
    const updatedData = {
      name: this.name,
      email: this.email,
      password: this.password
    };

    this.http.put(`http://localhost:8080/api/user/${this.id}`, updatedData).subscribe({
      next: () => {
        this.successMessage = 'Profil mis à jour avec succès !';
        setTimeout(() => this.router.navigate(['/categories']), 2000);
      },
      error: (error) => {
        this.errorMessage = error.error?.error || "Une erreur est survenue lors de la mise à jour.";
        console.error(error);
      }
    });
  }
}
