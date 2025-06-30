package com.tshirtshop.backend.dto;

import java.util.List;

public class CheckoutRequest {
    private List<CheckoutItemDTO> items;
    private String email;

    // Getters et setters
    public List<CheckoutItemDTO> getItems() {
        return items;
    }

    public void setItems(List<CheckoutItemDTO> items) {
        this.items = items;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
