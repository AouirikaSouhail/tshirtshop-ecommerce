package com.tshirtshop.backend.controller;

import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.tshirtshop.backend.service.MailService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/stripe")
public class StripeWebhookController {

    @Value("${stripe.webhook.secret}")
    private String endpointSecret;

    private final MailService mailService;

    public StripeWebhookController(MailService mailService) {
        this.mailService = mailService;
    }

    @PostMapping("/webhook")
    public ResponseEntity<Map<String, String>> handleStripeWebhook(
            @RequestHeader("Stripe-Signature") String sigHeader,
            @RequestBody String payload) {

        Event event;

        try {
            event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
        } catch (SignatureVerificationException e) {
            return ResponseEntity.badRequest().build();
        }

        if ("checkout.session.completed".equals(event.getType())) {
            Session session = (Session) event.getDataObjectDeserializer().getObject().orElse(null);
            if (session != null) {
                String clientEmail = session.getCustomerDetails().getEmail();
                try {
                    mailService.envoyerConfirmationCommande(
                            clientEmail,
                            "Confirmation de votre commande",
                            "<h1>Merci pour votre achat !</h1><p>Votre commande a été reçue.</p>"
                    );
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            }
        }

        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        return ResponseEntity.ok(response);
    }
}
