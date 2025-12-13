package com.yourorg.healthcare.patient.model;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "dossiers_medicals", indexes = {
        @Index(name = "idx_dossier_patient", columnList = "patientId")
})
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class DossierMedical {

    @Id
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(length = 1000) private String anChir;
    @Column(length = 1000) private String allergies;
    @Column(length = 1000) private String antFam;
    @Column(length = 1000) private String traitement;
    @Column(length = 1000) private String habitudes;

    @Column(nullable = false, updatable = false)
    private Instant dateCreation;

    @Column(nullable = false)
    private UUID patientId;

    @PrePersist
    void prePersist() {
        if (id == null) id = UUID.randomUUID();
        dateCreation = Instant.now();
    }
}