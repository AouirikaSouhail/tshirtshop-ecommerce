import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Component({
  selector: 'app-edit-product',
  templateUrl: './edit-product.component.html',
  styleUrls: ['./edit-product.component.css']
})
export class EditProductComponent implements OnInit {
  product: any;
  categories: any[] = [];
  productId!: number;

  constructor(
    private route: ActivatedRoute,
    private http: HttpClient,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.productId = Number(this.route.snapshot.paramMap.get('id'));

    const token = localStorage.getItem('token');
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);

    // 1️⃣ Charger le produit à modifier
    this.http.get<any>(`http://localhost:8080/products/${this.productId}`, { headers })
      .subscribe(data => {
        this.product = data;
      });

    // 2️⃣ Charger les catégories
    this.http.get<any[]>(`http://localhost:8080/categories`)
      .subscribe(data => {
        this.categories = data;
      });
  }

  updateProduct(): void {
    const token = localStorage.getItem('token');
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);

    this.http.put(`http://localhost:8080/products/${this.productId}`, this.product, { headers })
      .subscribe({
        next: () => {
          alert('Produit mis à jour avec succès.');
          this.router.navigate(['/']);
        },
        error: err => {
          alert("Erreur : " + err.message);
        }
      });
  }
}







