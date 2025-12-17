package com.example.auth_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendPasswordResetEmail(String toEmail, String token, String fullName) {
        String resetLink = "http://localhost:4200/reset-password/" + token; // À adapter avec ton URL frontend

        String subject = "Réinitialisation de votre mot de passe";

        String content = """
            Bonjour %s,

            Vous avez demandé la réinitialisation de votre mot de passe pour l'application Gestion Cabinet Médical.

            Cliquez sur le lien ci-dessous pour définir un nouveau mot de passe :

            <a href="%s">Réinitialiser mon mot de passe</a>

            ⚠️ Ce lien est valable uniquement pendant 1 heure et pour une seule utilisation.

            Si vous n'êtes pas à l'origine de cette demande, ignorez cet email.

            Cordialement,
            L'équipe technique
            """.formatted(fullName, resetLink);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(content);
        message.setFrom("no-reply@cabinetmedical.com");

        mailSender.send(message);
    }
}