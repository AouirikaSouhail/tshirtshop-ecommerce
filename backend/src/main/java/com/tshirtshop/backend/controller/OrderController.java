package com.tshirtshop.backend.controller;

import com.tshirtshop.backend.dto.CreateOrderRequest;
import com.tshirtshop.backend.dto.OrderDTO;
import com.tshirtshop.backend.dto.OrderItemDTO;
import com.tshirtshop.backend.model.Order;
import com.tshirtshop.backend.model.OrderItem;
import com.tshirtshop.backend.model.User;
import com.tshirtshop.backend.service.MailService;
import com.tshirtshop.backend.service.OrderService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@CrossOrigin(origins = "http://localhost:4200")
public class OrderController {

    private final OrderService orderService;
    private final MailService  mailService;

    @Autowired
    public OrderController(OrderService orderService,
                           MailService mailService) {
        this.orderService = orderService;
        this.mailService  = mailService;
    }

    /* ------------------------------------------------------------------ */
    /*   1.  CRÉATION D’UNE COMMANDE + MAIL DE CONFIRMATION                */
    /* ------------------------------------------------------------------ */
    @PostMapping
    public ResponseEntity<?> create(@RequestBody CreateOrderRequest request) {
        try {
            /* 1️⃣  Création / sauvegarde */
            Order savedOrder = orderService.createOrder(request);

            /* 2️⃣  Construction du mail HTML (très résumé) */
            StringBuilder html = new StringBuilder()
                    .append("<h2>🧾 Confirmation de commande</h2>")
                    .append("<ul>");
            for (OrderItem it : savedOrder.getOrderItems()) {
                html.append("<li>")
                        .append(it.getQuantity())
                        .append(" × ")
                        .append(it.getProduct().getName())
                        .append("</li>");
            }
            html.append("</ul>")
                    .append("<p>Total : ")
                    .append(String.format("%.2f €", savedOrder.getTotalAmount()))
                    .append("</p>");

            /* 3️⃣  Envoi du mail  */
            try {
                mailService.envoyerConfirmationCommande(
                        savedOrder.getUser().getEmail(),
                        "Confirmation de votre commande",
                        html.toString());
            } catch (MessagingException e) {
                System.err.println("❌ Erreur d’envoi d’e-mail : " + e.getMessage());
            }

            /* 4️⃣  Retour au frontend */
            return ResponseEntity.ok(savedOrder);

        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    /* ------------------------------------------------------------------ */
    /*   2.  LISTE DES COMMANDES DE L’UTILISATEUR CONNECTÉ                */
    /* ------------------------------------------------------------------ */
    @GetMapping("/by-user")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> listByLoggedUser(Authentication authentication) {

        String email = authentication.getName();          // 🔐 e-mail du token JWT
        User   user  = orderService.findUserByEmail(email);

        if (user == null) {
            return ResponseEntity.badRequest().body("Utilisateur non trouvé.");
        }

        List<Order> orders = orderService.findByUser(user);

        List<OrderDTO> dtoList = orders.stream()
                .map(this::toDto)
                .toList();

        return ResponseEntity.ok(dtoList);
    }

    /* ------------------------------------------------------------------ */
    /*   3.  Méthode utilitaire : entity ➜ DTO                            */
    /* ------------------------------------------------------------------ */
    private OrderDTO toDto(Order o) {
        OrderDTO dto = new OrderDTO();
        dto.setId(o.getId());
        dto.setDateCommande(o.getCreatedAt());
        dto.setTotal(o.getTotal());

        List<OrderItemDTO> items = o.getItems().stream().map(it -> {
            OrderItemDTO d = new OrderItemDTO();
            d.setProductName(it.getProduct().getName());
            d.setQuantity(it.getQuantity());
            d.setPrice(it.getPriceUnitSnapshot());
            return d;
        }).toList();

        dto.setItems(items);
        return dto;
    }
}
