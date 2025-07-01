import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { forkJoin } from 'rxjs';

interface Category { id: number; name: string; }

@Component({
  selector   : 'app-edit-product',
  templateUrl: './edit-product.component.html',
  styleUrls  : ['./edit-product.component.css']
})
export class EditProductComponent implements OnInit {

  product: any = null;          // DTO reçu du backend
  categories: Category[] = [];  // Liste des catégories
  private productId!: number;

  constructor(
    private route : ActivatedRoute,
    private http  : HttpClient,
    private router: Router
  ) {}

  ngOnInit(): void {

    /** 🔒 1) Vérif. token */
    const token = localStorage.getItem('token');
    if (!token) {
      alert('Vous devez être connecté.');
      this.router.navigate(['/login']);
      return;
    }
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);

    /** 🆔 2) Id du produit dans l’URL */
    this.productId = Number(this.route.snapshot.paramMap.get('id'));

    /** 📡 3) Appels parallèles : produit + catégories  */
    forkJoin({
      product   : this.http.get<any>(`http://localhost:8080/products/${this.productId}`, { headers }),
      categories: this.http.get<Category[]>(`http://localhost:8080/categories`,          { headers })
    }).subscribe({
      next: res => {
        this.product    = res.product;
        this.categories = res.categories;

        /** ✅ Sécurité : éviter erreur sur product.category.id undefined */
        if (!this.product.category) {
          this.product.category = { id: null, name: '' };
        }
      },
      error: err => alert('Erreur de chargement : ' + err.message)
    });
  }

  /* ----------- Enregistrer ----------- */
  updateProduct(): void {

    const token = localStorage.getItem('token');
    if (!token) {
      alert('Non autorisé.');
      return;
    }
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);

    this.http.put(
      `http://localhost:8080/products/${this.productId}`,
      this.product,
      { headers }
    ).subscribe({
      next : ()   => {
        alert('✅ Produit mis à jour avec succès.');
        this.router.navigate(['/categories']);          // ou route admin
      },
      error: err => alert('Erreur mise à jour : ' + err.message)
    });
  }
}
