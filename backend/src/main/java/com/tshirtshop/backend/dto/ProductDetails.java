package com.tshirtshop.backend.dto;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter

public class ProductDetails {

    private Long id;
    private String name;
    private String brand;
    private double price;
    private String imageUrl;
    private String description;
    private String categoryName;
    private int quantiteStock; // ✅ Ajouté
}
