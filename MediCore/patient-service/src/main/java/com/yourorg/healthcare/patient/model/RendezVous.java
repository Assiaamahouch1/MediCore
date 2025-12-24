package com.yourorg.healthcare.patient.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "rendez_vous", indexes = {
        @Index(name = "idx_rdv_patient", columnList = "patientId"),
        @Index(name = "idx_rdv_medecin", columnList = "medecinId"),
        @Index(name = "idx_rdv_date", columnList = "dateConsultation")
})
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class RendezVous {

    @Id
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(nullable = false)
    private LocalDate dateConsultation;

    @Column(nullable = false)
    private LocalTime heureRdv;

    @Column(length = 255)
    private String motif;

    @Column(length = 1000)
    private String notes;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private StatutRendezVous statut;

    @Column(nullable = false)
    private UUID patientId;

    @Column(nullable = false)
    private UUID medecinId;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;

    @PrePersist
    void prePersist() {
        if (id == null) id = UUID.randomUUID();
        Instant now = Instant.now();
        createdAt = now; updatedAt = now;
    }

    @PreUpdate
    void preUpdate() { updatedAt = Instant.now(); }
}