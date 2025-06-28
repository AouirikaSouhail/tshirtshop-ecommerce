package com.tshirtshop.backend.controller;

import com.tshirtshop.backend.dto.CreateOrderRequest;
import com.tshirtshop.backend.model.Order;
import com.tshirtshop.backend.model.OrderItem;
import com.tshirtshop.backend.service.MailService;
import com.tshirtshop.backend.service.OrderService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Contrôleur REST chargé de gérer les commandes.
 * – Créé une commande à partir du corps JSON envoyé par Angular.
 * – Envoie ensuite un e‑mail HTML de confirmation au client.
 * Les étapes 1️⃣ et 2️⃣ (création + e‑mail) sont clairement identifiées dans le code.
 */
@RestController
@RequestMapping("/orders")
@CrossOrigin(origins = "http://localhost:4200")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    private MailService mailService; // Service réutilisable pour l'envoi d'e‑mails

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody CreateOrderRequest request) {
        try {
            // 1️⃣ Création et sauvegarde de la commande
            Order savedOrder = orderService.createOrder(request);

            // 2️⃣ Construction du corps de l'e‑mail (HTML stylisé)
            String adresseComplet = savedOrder.getUser().getAdresse() + ", "
                    + savedOrder.getUser().getCodePostal() + " "
                    + savedOrder.getUser().getVille();

            StringBuilder msg = new StringBuilder();
            msg.append("<div style='font-family: Arial, sans-serif; color: #333;'>")
                    .append("<h2 style='color: #4CAF50;'>🧾 Confirmation de votre commande</h2>")
                    .append("<p>Bonjour ").append(savedOrder.getUser().getFirstName()).append(",</p>")
                    .append("<p>Merci pour votre commande sur <strong>TshirtShop</strong> !</p>")
                    .append("<h3>Détails de la commande :</h3>")
                    .append("<table style='width:100%; border-collapse: collapse;'>")
                    .append("<tr><th style='border:1px solid #ddd;padding:8px;'>Produit</th><th style='border:1px solid #ddd;padding:8px;'>Quantité</th></tr>");

            for (OrderItem item : savedOrder.getOrderItems()) {
                msg.append("<tr>")
                        .append("<td style='border:1px solid #ddd;padding:8px;'>")
                        .append(item.getProduct().getName())
                        .append("</td>")
                        .append("<td style='border:1px solid #ddd;padding:8px;'>")
                        .append(item.getQuantity())
                        .append("</td>")
                        .append("</tr>");
            }

            msg.append("</table>")
                    .append("<p><strong>Total payé : ")
                    .append(String.format("%.2f", savedOrder.getTotalAmount()))
                    .append(" €</strong></p>")
                    .append("<h3>Adresse de livraison :</h3>")
                    .append("<p>").append(adresseComplet).append("</p>")
                    .append("<p>Nous restons à votre disposition si vous avez des questions.</p>")
                    .append("<p>🛍️ L’équipe TshirtShop</p>")
                    .append("</div>");

            // 3️⃣ Envoi de l'e‑mail (try/catch spécifique)
            try {
                mailService.envoyerConfirmationCommande(
                        savedOrder.getUser().getEmail(),
                        "Confirmation de votre commande",
                        msg.toString()
                );
            } catch (MessagingException e) {
                // Log de l'erreur : la commande est créée même si l'e‑mail échoue
                System.err.println("Erreur d'envoi d’e‑mail : " + e.getMessage());
            }

            // 4️⃣ Réponse au frontend (commande créée avec succès)
            return ResponseEntity.ok(savedOrder);

        } catch (RuntimeException ex) {
            // Erreur fonctionnelle (ex : stock insuffisant)
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
}