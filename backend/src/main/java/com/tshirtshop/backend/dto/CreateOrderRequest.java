package com.tshirtshop.backend.dto;

import lombok.Data;
import java.util.List;

@Data
public class CreateOrderRequest {

    private Long userId;
    private List<Item> items;

    @Data
    public static class Item {
        private Long productId;
        private int quantity;
    }
}

/**
 * Le DTO CreateOrderRequest est un objet envoyé depuis le frontend pour créer une commande.
 * Il contient :
 *
 * l’ID de l’utilisateur qui passe la commande,
 *
 * une liste d’articles, chacun avec un productId et une quantity.
 *
 * 🔁 Il ne contient ni prix, ni date → ce sera calculé côté backend au moment de créer la commande.
 * */