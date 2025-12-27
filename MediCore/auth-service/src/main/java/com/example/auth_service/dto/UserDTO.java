package com.example.auth_service.dto;

import com.example.auth_service.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author sawssan
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private Long id;
    private String avatar;
    private String username;
    private String password;
    private Role role;
    private String nom;
    private String prenom;
    private String numTel;
    private String signature;
    private String activationToken;        // On le renomme mentalement en "resetToken"
    private LocalDateTime tokenExpiryDate;
    private boolean actif = false;         // On garde actif = true pour les comptes normaux
    private Long cabinetId;
}
