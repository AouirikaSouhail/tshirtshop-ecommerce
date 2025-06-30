import { Component, OnInit } from '@angular/core';
import { OrderService } from '../../../app/services/order.service'

@Component({
  selector: 'app-mes-commandes',
  templateUrl: './mes-commandes.component.html'
})
export class MesCommandesComponent implements OnInit {

  commandes: {
    id: number;
    createdAt: string;
    total: number;
    items: {
      productName: string;
      quantity: number;
      priceUnitSnapshot: number;
    }[];
  }[] = [];

  constructor(private orderService: OrderService) {}

  ngOnInit(): void {
    this.orderService.getOrdersByUser().subscribe({
      next: data => this.commandes = data,
      error: err => console.error('Erreur chargement commandes', err)
    });
  }
}
