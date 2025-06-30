package com.tshirtshop.backend.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.net.Webhook;
import com.tshirtshop.backend.service.MailService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

@RestController
@RequestMapping("/api/stripe")
public class StripeWebhookController {

    private static final Logger logger = Logger.getLogger(StripeWebhookController.class.getName());

    @Value("${stripe.webhook.secret}")
    private String endpointSecret;

    private final MailService mailService;

    public StripeWebhookController(MailService mailService) {
        this.mailService = mailService;
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> handleStripeWebhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String sigHeader) {

        Event event;

        try {
            event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
            logger.info("✅ Webhook reçu : " + event.getType());

            if ("checkout.session.completed".equals(event.getType())) {
                JsonObject json = JsonParser.parseString(payload).getAsJsonObject();
                JsonObject dataObject = json.getAsJsonObject("data").getAsJsonObject("object");
                String email = dataObject.getAsJsonObject("customer_details").get("email").getAsString();
                String sessionId = dataObject.get("id").getAsString();

                logger.info("📧 Email Stripe détecté : " + email);
                logger.info("🆔 ID Session : " + sessionId);

                try {
                    mailService.envoyerConfirmationCommande(
                            email,
                            "Confirmation de votre commande",
                            "<h2>Merci pour votre achat !</h2><p>ID de commande : " + sessionId + "</p>"
                    );
                    logger.info("✉️ E-mail envoyé !");
                } catch (MessagingException e) {
                    logger.severe("❌ Erreur envoi email : " + e.getMessage());
                }
            }

        } catch (SignatureVerificationException e) {
            logger.severe("❌ Signature Stripe invalide : " + e.getMessage());
            return ResponseEntity.badRequest().body("Signature invalide");
        } catch (Exception e) {
            logger.severe("❌ Erreur webhook générale : " + e.getMessage());
            return ResponseEntity.badRequest().body("Erreur");
        }

        return ResponseEntity.ok("Webhook traité");
    }
}
