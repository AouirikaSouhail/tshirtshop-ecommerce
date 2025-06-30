package com.tshirtshop.backend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // produit commandé
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id")
    private Product product;

    // la commande parente
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private int quantity;
    private double priceUnitSnapshot;   // prix € copie le jour J
}
/**
 * La classe OrderItem représente une ligne dans une commande, c’est-à-dire un produit + une quantité + un prix unitaire.
 * Elle est reliée :
  à un produit (Product),
  et à une commande (Order).
 * */