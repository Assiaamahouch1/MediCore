package com.yourorg.healthcare.patient.model;

import jakarta.persistence.*;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
@Table(name = "patients")
public class Patient {

    @Id
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(length = 50)
    private String cin;

    @Column(nullable = false, length = 100)
    private String nom;

    @Column(nullable = false, length = 100)
    private String prenom;

    private LocalDate dateNaissance;

    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private Sexe sexe;

    @Column(length = 30)
    private String numTel;

    @Column(length = 180)
    private String email;

    @Column(length = 255)
    private String adresse;

    @Column(length = 120)
    private String mutuelleNom;

    @Column(length = 60)
    private String mutuelleNumero;

    private LocalDate mutuelleExpireLe;

    @Column(nullable = false)
    private boolean actif = true;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;

    public enum Sexe { HOMME, FEMME, AUTRE }

    @PrePersist
    void prePersist() {
        if (id == null) id = UUID.randomUUID();
        Instant now = Instant.now();
        createdAt = now;
        updatedAt = now;
        actif = true;
    }

    @PreUpdate
    void preUpdate() { updatedAt = Instant.now(); }


}