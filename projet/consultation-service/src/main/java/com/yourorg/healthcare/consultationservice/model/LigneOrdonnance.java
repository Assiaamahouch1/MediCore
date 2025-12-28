package com.yourorg.healthcare.consultationservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "lignes_ordonnance", indexes = {
        @Index(name = "idx_ligne_ordonnance", columnList = "ordonnanceId"),
        @Index(name = "idx_ligne_medicament", columnList = "medicamentId")
})
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class LigneOrdonnance {

    @Id
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(length = 500)
    private String description;

    @Column(length = 255)
    private String dosage;

    @Column(length = 255)
    private String duree;

    @Column(nullable = false)
    private UUID ordonnanceId;

    // Référence vers medicaments-service (optionnelle)
    private UUID medicamentId;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    void prePersist() {
        if (id == null) id = UUID.randomUUID();
        createdAt = Instant.now();
    }
}
