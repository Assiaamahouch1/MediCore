package com.example.auth_service.controller;




import com.example.auth_service.dto.ForgotPasswordRequest;
import com.example.auth_service.dto.LoginRequest;
import com.example.auth_service.dto.RegisterRequest;
import com.example.auth_service.dto.ResetPasswordRequest;
import com.example.auth_service.model.Utilisateur;
import com.example.auth_service.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public String register(@RequestBody RegisterRequest request){
        return authService.register(request);
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest request){
        return authService.login(request);
    }
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Utilisateur> all(){
        return authService.all();
    }
    // 1. Demander le lien de réinitialisation
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        authService.initiatePasswordReset(request.getUsername());
        return ResponseEntity.ok("Si un compte existe avec cet identifiant, un email de réinitialisation a été envoyé.");
    }

    // 2. Utiliser le lien pour changer le mot de passe
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/reset-password/{token}")
    public ResponseEntity<String> resetPassword(
            @PathVariable String token,
            @RequestBody ResetPasswordRequest request) {
        authService.completePasswordReset(token, request);
        return ResponseEntity.ok("Mot de passe réinitialisé avec succès ! Vous pouvez vous connecter.");
    }


}
