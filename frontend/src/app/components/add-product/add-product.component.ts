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

  ngOnInit() {
    this.http.get<any[]>('http://localhost:8080/categories').subscribe(data => {
      this.categories = data;
    });
  }

  addProduct() {
    const token = localStorage.getItem('token'); // 🔐 Token JWT stocké
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);

    this.http.post(`http://localhost:8080/products/category/${this.product.categoryId}`, this.product, { headers })
      .subscribe({
        next: () => {
          alert("Produit ajouté avec succès !");
          this.router.navigate(['/']); // Redirection
        },
        error: err => {
          alert("Erreur : " + err.message);
        }
      });
  }
}
