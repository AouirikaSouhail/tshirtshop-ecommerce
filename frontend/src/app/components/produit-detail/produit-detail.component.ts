import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ProduitService, Produit } from '../../services/produit.service';

@Component({
  selector: 'app-produit-detail',
  templateUrl: './produit-detail.component.html',
// "s" a styleUrl Parce que Angular attend un tableau de chemins vers des fichiers CSS → même s’il y en a un seul !
  styleUrls: ['./produit-detail.component.css'] 
})
export class ProduitDetailComponent implements OnInit {

  produit!: Produit; // Variable pour stocker le produit chargé
  id!: number;       // L’ID récupéré depuis l’URL

  constructor(
    private route: ActivatedRoute,
    private produitService: ProduitService
  ) {}

  ngOnInit(): void {
    // 🔄 1. Récupérer l'ID du produit depuis l'URL
    this.route.paramMap.subscribe(params => {
      this.id = Number(params.get('id'));

      // 📡 2. Appeler le backend via le service Angular
      this.produitService.getProduitParId(this.id).subscribe(
        (data) => {
          // 💾 3. Stocker les données dans la variable locale
          this.produit = data;
        },
        (error) => {
          console.error('Erreur lors du chargement du produit :', error);
        }
      );
    });
  }
}
