package com.tshirtshop.backend.controller;

import com.tshirtshop.backend.service.MailService;
import jakarta.mail.MessagingException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class MailTestController {

    private final MailService mailService;

    public MailTestController(MailService mailService) {
        this.mailService = mailService;
    }

    // POST /api/test-mail
    @PostMapping("/test-mail")
    public String testEnvoiMail() {
        try {
            mailService.envoyerConfirmationCommande(
                    "hdvideoforlife@gmail.com",                       // ← mets ici l’adresse à tester
                    "Test SMTP depuis Spring Boot",
                    "<h3>Bravo !</h3><p>Ceci est un test SMTP.</p>"
            );
            return "Mail envoyé avec succès ✅";
        } catch (MessagingException e) {
            e.printStackTrace();
            return "❌ Échec de l’envoi du mail : " + e.getMessage();
        }
    }
}
