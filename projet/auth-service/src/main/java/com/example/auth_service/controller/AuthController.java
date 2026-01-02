package com.example.auth_service.controller;

import com.example.auth_service.dto.*;
import com.example.auth_service.model.Utilisateur;
import com.example.auth_service.service.AuthService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static com.example.auth_service.Constant.Constant.IMAGE_DIRECTORY;
import static com.example.auth_service.Constant.Constant.IMAGE_DIRECTORY_SUPERADMIN;
import static org.springframework.util.MimeTypeUtils.IMAGE_JPEG_VALUE;
import static org.springframework.util.MimeTypeUtils.IMAGE_PNG_VALUE;

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

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody ForgotPasswordRequest request) throws MessagingException {
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

    @GetMapping("/me")
    public ResponseEntity<Utilisateur> getCurrentUser() {
        Utilisateur userInfo = authService.getCurrentUserInfo();
        return ResponseEntity.ok(userInfo);
    }
    @PutMapping("/superadmin/image")
    public ResponseEntity<String> uploadImageSuperAdmin(@RequestParam("id") Long id, @RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok().body(authService.uploadImage(id, file));
    }
    @GetMapping(path = "/superadmin/image/{filename}", produces = { IMAGE_PNG_VALUE, IMAGE_JPEG_VALUE })
    public byte[] getImageSuperAdmin(@PathVariable("filename") String filename) throws IOException {
        return Files.readAllBytes(Paths.get(IMAGE_DIRECTORY_SUPERADMIN + filename));
    }
    @PutMapping("/superadmin/{id}")
    public UserDTO updateSuperAdmin(@PathVariable Long id, @RequestBody UserDTO dto) {
        return authService.updateSuperAdmin(id, dto);
    }

}
