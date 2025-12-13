package com.yourorg.healthcare.consultationservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "consultations", indexes = {
        @Index(name = "idx_consultations_patient_id", columnList = "patientId"),
        @Index(name = "idx_consultations_doctor_id", columnList = "doctorId"),
        @Index(name = "idx_consultations_date", columnList = "date")
})
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Consultation {

    @Id
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(nullable = false)
    private UUID patientId;

    @Column(nullable = false)
    private UUID doctorId;

    @Column(nullable = false)
    private Instant date;

    @Column(length = 1000)
    private String notes;

    @Column(length = 255)
    private String diagnosis;

    @Column(length = 255)
    private String prescription; // ex: texte libre ou référence

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;

    @PrePersist
    void prePersist() {
        if (id == null) id = UUID.randomUUID();
        Instant now = Instant.now();
        createdAt = now; updatedAt = now;
        if (date == null) date = now;
    }

    @PreUpdate
    void preUpdate() { updatedAt = Instant.now(); }
}