package com.tshirtshop.backend.controller;

import com.tshirtshop.backend.service.MailService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class MailTestController {

    @Autowired
    private MailService mailService;

    @GetMapping("/test-mail")
    public String testEnvoiMail() {
        try {
            mailService.envoyerConfirmationCommande(
                    "hdvideoforlife@gmail.com", // Remplace par un vrai email
                    "Test SMTP depuis Spring Boot",
                    "<h3>Bravo !</h3><p>Ceci est un test SMTP.</p>"
            );
            return "Mail envoyé avec succès";
        } catch (MessagingException e) {
            e.printStackTrace();
            return "Échec de l’envoi du mail : " + e.getMessage();
        }
    }
}
