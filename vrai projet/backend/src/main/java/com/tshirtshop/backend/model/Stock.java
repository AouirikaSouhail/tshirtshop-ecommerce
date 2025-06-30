package com.tshirtshop.backend.model;

import com.tshirtshop.backend.model.Product;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "produit_id", referencedColumnName = "id")
    private Product produit;

    private int quantiteDisponible;
}
