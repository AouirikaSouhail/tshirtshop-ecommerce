package com.tshirtshop.backend.model;

import jakarta.persistence.*;

@Entity
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /* ----- relations ----- */
    @ManyToOne(optional = false)
    private User user;

    @ManyToOne(optional = false)
    private Product produit;

    /* ----- données ----- */
    private int quantite;

    /* ----- getters / setters ----- */
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Product getProduit() { return produit; }
    public void setProduit(Product produit) { this.produit = produit; }

    public int getQuantite() { return quantite; }
    public void setQuantite(int quantite) { this.quantite = quantite; }
}
