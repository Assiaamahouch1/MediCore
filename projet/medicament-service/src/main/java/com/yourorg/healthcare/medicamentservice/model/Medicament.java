package com.yourorg.healthcare.medicamentservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "medicaments",
        indexes = { @Index(name="idx_medicaments_nom", columnList="nom"),
                @Index(name="idx_medicaments_atc", columnList="atcCode") },
        uniqueConstraints = { @UniqueConstraint(name="uk_medicaments_nom", columnNames = "nom") }
)
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Medicament {
    @Id
    @Column(nullable = false, updatable = false)
    private UUID id;

    @NotBlank
    @Column(nullable = false, length = 255)
    private String nom;

    @Column(length = 255)
    private String description;

    @Column(length = 50)
    private String atcCode;

    @Column(length = 100)
    private String forme;

    @Column(length = 20)
    private String dosageUnite;

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