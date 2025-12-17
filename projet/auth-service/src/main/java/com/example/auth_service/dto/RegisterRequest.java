package com.example.auth_service.dto;
import com.example.auth_service.model.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    private String username;
    private String password;
    private String nom;
    private String prenom;
    private String numTel;
    private String signature;      // optionnel, peut être null
    private Role role;
    private Long cabinetId;        // obligatoire seulement si MEDECIN ou SECRETAIRE
}