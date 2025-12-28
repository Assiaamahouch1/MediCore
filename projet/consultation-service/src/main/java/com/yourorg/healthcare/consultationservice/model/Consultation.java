package com.yourorg.healthcare.consultationservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "consultations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Consultation {

    @Id
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(length = 50)
    private String type;

    @Column(nullable = false)
    private Instant dateConsultation;

    @Column(length = 2000)
    private String examenClinique;

    @Column(length = 2000)
    private String examenComplementaire;

    @Column(length = 1000)
    private String diagnostic;

    @Column(length = 2000)
    private String traitement;

    @Column(length = 2000)
    private String observations;

    @Column(nullable = false)
    private UUID patientId;

    @Column(nullable = false)
    private UUID medecinId;

    private UUID rendezVousId;
    private UUID dossierMedicalId;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;

    @PrePersist
    void prePersist() {
        if (id == null) id = UUID.randomUUID();
        Instant now = Instant.now();
        if (dateConsultation == null) dateConsultation = now;
        createdAt = now; updatedAt = now;
    }

    @PreUpdate
    void preUpdate() { updatedAt = Instant.now(); }
}