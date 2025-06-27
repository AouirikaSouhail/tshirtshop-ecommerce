package com.tshirtshop.backend.model;

import com.tshirtshop.backend.model.Product;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Lien avec un produit (1 produit = 1 stock)
    @OneToOne
    @JoinColumn(name = "produit_id", referencedColumnName = "id")
    private Product produit;

    private int quantiteDisponible;
}
