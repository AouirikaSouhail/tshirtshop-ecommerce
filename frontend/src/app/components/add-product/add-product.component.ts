import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Router } from '@angular/router';

@Component({
  selector: 'app-add-product',
  templateUrl: './add-product.component.html',
  styleUrls: ['./add-product.component.css']
})
export class AddProductComponent implements OnInit {
  product = {
    name: '',
    price: 0,
    description: '',
    imageUrl: '',
    categoryId: null
  };

  categories: any[] = [];

  constructor(private http: HttpClient, private router: Router) {}

  ngOnInit(): void {
    this.http.get<any[]>('http://localhost:8080/categories').subscribe({
      next: (data) => {
        this.categories = data;
        console.log('Catégories chargées :', this.categories); // Debug
      },
      error: (err) => {
        console.error('Erreur lors du chargement des catégories :', err);
      }
    });
  }

  addProduct(): void {
    const token = localStorage.getItem('token');
    if (!token) {
      alert('Vous devez être connecté en tant qu’administrateur.');
      return;
    }

    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);

    this.http.post(
      `http://localhost:8080/products/category/${this.product.categoryId}`,
      this.product,
      { headers }
    ).subscribe({
      next: () => {
        alert('Produit ajouté avec succès !');
        this.router.navigate(['/']); // Redirection après succès
      },
      error: (err) => {
        console.error('Erreur lors de l’ajout du produit :', err);
        alert('Erreur : ' + err.message);
      }
    });
  }
}
