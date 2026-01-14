package com.yourorg.healthcare.consultationservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "lignes_ordonnance")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class LigneOrdonnance {

    @Id
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(length = 500)
    private String nom;

    @Column(length = 255)
    private String dosage;

    @Column(length = 255)
    private String duree;

    @Column(length = 255)
    private String forme;

    @Column(nullable = false)
    private UUID ordonnanceId;

    // Référence vers medicaments-service (optionnelle)
    private UUID medicamentId;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;


}
