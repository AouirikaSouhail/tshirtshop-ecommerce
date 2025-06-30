package com.tshirtshop.backend.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    @Autowired
    private JavaMailSender mailSender;

    public void envoyerConfirmationCommande(String to, String subject, String body) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true); // true = multipart
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(body, true); // true = HTML
        helper.setFrom("testeafcuccle@gmail.com"); // même que spring.mail.username

        mailSender.send(message);
    }
}
