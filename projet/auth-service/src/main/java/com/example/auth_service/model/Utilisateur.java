package com.example.auth_service.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "utilisateurs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Utilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // <-- important
    private Long id;
    private String avatar;
    private String username;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;
    private String nom;
    private String prenom;
    private String numTel;
    private String signature;
    private String activationToken;
    private LocalDateTime tokenExpiryDate;
    private boolean actif = false;
    private Long cabinetId;

}
