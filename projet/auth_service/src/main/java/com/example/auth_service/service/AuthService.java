package com.example.auth_service.service;




import com.example.auth_service.dto.RegisterRequest;
import com.example.auth_service.model.Role;
import com.example.auth_service.repository.UtilisateurRepository;

import com.example.auth_service.config.JwtUtils;
import com.example.auth_service.dto.LoginRequest;

import com.example.auth_service.model.Utilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthService {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private JwtUtils jwtUtils;

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
}
