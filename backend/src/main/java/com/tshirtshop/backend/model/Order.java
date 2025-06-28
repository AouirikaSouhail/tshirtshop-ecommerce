package com.tshirtshop.backend.model;


import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;


@Entity
@Data @NoArgsConstructor @AllArgsConstructor
@Table(name = "orders")               // ➡️ table orders
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ✅ L’utilisateur qui a passé la commande
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")     // FK en base
    private User user;

    private LocalDateTime createdAt = LocalDateTime.now();

    private double total;             // total € stocké « snapshot »

    // ✅ Les lignes de la commande
    @OneToMany(mappedBy = "order",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<OrderItem> items;
}
/**
 * La classe Order représente une commande passée par un utilisateur.
 * Elle contient :
 *
 * ✅ id : identifiant unique de la commande.
 *
 * 👤 user : l’utilisateur qui a passé la commande.
 *
 * 🕒 createdAt : date et heure de la commande.
 *
 * 💰 total : montant total payé (enregistré comme un "snapshot").
 *
 * 📦 items : liste des articles commandés (OrderItem).
 *
 * Elle est liée :
 *
 * à l’entité User (plusieurs commandes par utilisateur),
 *
 * à l’entité OrderItem (plusieurs articles dans une commande).
 *
 * 👉 C’est l’entité principale pour représenter un achat complet.
 */