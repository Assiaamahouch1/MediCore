package com.yourorg.healthcare.patient.model;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "documents_medicals")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class DocumentMedical {

    @Id
    @GeneratedValue
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(nullable = false, length = 255)
    private String titre;

    @Column(nullable = false, length = 255)
    private String url;

    private UUID patientId;
    private UUID dossierMedicalId;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    void prePersist() {
        if (id == null) id = UUID.randomUUID();
        createdAt = Instant.now();
    }
}