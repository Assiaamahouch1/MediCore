package com.example.auth_service.service;

import lombok.RequiredArgsConstructor;
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
    public void sendAccountCreationEmail(String toEmail, String token, String fullName) {
        String activationLink = "http://localhost:4200/reset-password/" + token;

        String subject = "Bienvenue - Créez votre mot de passe";

        String content = """
            Bonjour %s,

            Votre compte administrateur a été créé avec succès sur l'application Gestion Cabinet Médical.

            Pour activer votre compte, veuillez cliquer sur le lien ci-dessous et définir votre mot de passe :

            %s

            ⚠️ Ce lien est valable uniquement pendant 24 heures.

            Pour toute question, n'hésitez pas à contacter l'équipe technique.

            Cordialement,
            L'équipe Gestion Cabinet Médical
            """.formatted(fullName, activationLink);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(content);
        message.setFrom("no-reply@cabinetmedical.com");

        mailSender.send(message);
    }


    public void sendAccountCreationEmailMedc(String toEmail, String token, String fullName,String psw) {
        String activationLink = "http://localhost:4200/signin/";

        String subject = "Bienvenue - Activez Votre compte";

        String content = """
            Bonjour %s,

            Votre compte Médecin a été créé avec succès sur l'application Gestion Cabinet Médical.

            Pour activer votre compte, veuillez cliquer sur le lien ci-dessous  :

            %s

            et voila votre mot de passe (tu peut le modifier):
            %s

            Pour toute question, n'hésitez pas à contacter l'équipe technique.

            Cordialement,
            L'équipe Gestion Cabinet Médical
            """.formatted(fullName,activationLink, psw);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(content);
        message.setFrom("no-reply@cabinetmedical.com");

        mailSender.send(message);
    }
}