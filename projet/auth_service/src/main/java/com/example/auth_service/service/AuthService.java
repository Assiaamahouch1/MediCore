package com.example.auth_service.service;




import com.example.auth_service.dto.ForgotPasswordRequest;
import com.example.auth_service.dto.RegisterRequest;
import com.example.auth_service.dto.ResetPasswordRequest;
import com.example.auth_service.model.Role;
import com.example.auth_service.repository.UtilisateurRepository;

import com.example.auth_service.config.JwtUtils;
import com.example.auth_service.dto.LoginRequest;

import com.example.auth_service.model.Utilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class AuthService {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private EmailService emailService;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public String register(RegisterRequest request) {
        if(utilisateurRepository.existsByUsername(request.getUsername())) // ← change ici
            throw new RuntimeException("Login already exists");

        // Validation simple
        if ((request.getRole() == Role.MEDECIN || request.getRole() == Role.SECRETAIRE)
                && request.getCabinetId() == null) {
            throw new RuntimeException("cabinetId obligatoire pour MEDECIN/SECRETAIRE");
        }

        Utilisateur user = Utilisateur.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .nom(request.getNom())
                .prenom(request.getPrenom())
                .numTel(request.getNumTel())
                .signature(request.getSignature())
                .role(request.getRole())
                .cabinetId(request.getCabinetId())
                .actif(true)
                .build();

        utilisateurRepository.save(user);
        return "User registered successfully";
    }

    public String login(LoginRequest request) {
        Utilisateur user = utilisateurRepository.findByUsername(request.getUsername()) // ← change ici
                .orElseThrow(() -> new RuntimeException("User not found"));

        if(!passwordEncoder.matches(request.getPassword(), user.getPassword()))
            throw new RuntimeException("Invalid password");

        return jwtUtils.generateToken(user);
    }



    public List<Utilisateur> all() {

        return utilisateurRepository.findAll();
    }


    @Transactional
    public void initiatePasswordReset(String username) {
        Utilisateur user = utilisateurRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        if (!user.isActif()) {
            return; // Ne rien faire si compte désactivé
        }

        // Générer token unique
        String token = UUID.randomUUID().toString();

        user.setActivationToken(token);
        user.setTokenExpiryDate(LocalDateTime.now().plusHours(1)); // 1 heure

        utilisateurRepository.save(user);

        String fullName = user.getPrenom() + " " + user.getNom();
        emailService.sendPasswordResetEmail(user.getUsername(), token, fullName);
    }

    @Transactional
    public void completePasswordReset(String token, ResetPasswordRequest request) {
        Utilisateur user = utilisateurRepository.findByActivationToken(token)
                .orElseThrow(() -> new RuntimeException("Lien invalide ou déjà utilisé"));

        if (LocalDateTime.now().isAfter(user.getTokenExpiryDate())) {
            throw new RuntimeException("Ce lien a expiré. Veuillez refaire une demande.");
        }

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new RuntimeException("Les mots de passe ne correspondent pas.");
        }

        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setActivationToken(null);
        user.setTokenExpiryDate(null);

        utilisateurRepository.save(user);
    }
}
