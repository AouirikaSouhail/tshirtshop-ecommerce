import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Component({
  selector: 'app-edit-product',
  templateUrl: './edit-product.component.html',
  styleUrls: ['./edit-product.component.css']
})
export class EditProductComponent implements OnInit {
  product: any = null;
  categories: any[] = [];
  productId!: number;

  constructor(
    private route: ActivatedRoute,
    private http: HttpClient,
    private router: Router
  ) {}

  ngOnInit(): void {
    const token = localStorage.getItem('token');
    if (!token) {
      alert("Vous devez être connecté.");
      this.router.navigate(['/login']);
      return;
    }

    this.productId = Number(this.route.snapshot.paramMap.get('id'));

    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);

    // 1️⃣ Charger le produit
    this.http.get<any>(`http://localhost:8080/products/${this.productId}`, { headers }).subscribe({
      next: data => this.product = data,
      error: err => alert("Erreur chargement produit : " + err.message)
    });

    // 2️⃣ Charger les catégories
    this.http.get<any[]>(`http://localhost:8080/categories`).subscribe({
      next: data => this.categories = data,
      error: err => alert("Erreur chargement catégories : " + err.message)
    });
  }

  updateProduct(): void {
    const token = localStorage.getItem('token');
    if (!token) {
      alert("Non autorisé.");
      return;
    }

    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);

    this.http.put(`http://localhost:8080/products/${this.productId}`, this.product, { headers })
      .subscribe({
        next: () => {
          alert('✅ Produit mis à jour avec succès.');
          this.router.navigate(['/dashboard']); // change si ta route diffère
        },
        error: err => {
          alert("Erreur lors de la mise à jour : " + err.message);
        }
      });
  }
}
