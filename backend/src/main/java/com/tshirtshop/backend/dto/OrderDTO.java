package com.tshirtshop.backend.dto;

import jakarta.persistence.Column;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDTO {
    private Long id;
    @Column(name = "date_commande")
    private LocalDateTime dateCommande;
    private double total;
    private List<OrderItemDTO> items;
}