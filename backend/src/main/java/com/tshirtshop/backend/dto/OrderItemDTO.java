package com.tshirtshop.backend.dto;

import lombok.Data;

@Data
public class OrderItemDTO {
    private String productName;
    private double price;
    private int quantity;
}
