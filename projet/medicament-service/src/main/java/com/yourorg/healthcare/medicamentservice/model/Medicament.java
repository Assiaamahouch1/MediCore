package com.yourorg.healthcare.medicamentservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import java.time.Instant;
import java.util.UUID;
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Medicament {
    @Id
    @GeneratedValue
    @Column(nullable = false, updatable = false)
    private UUID id;

    @NotBlank
    @Column(nullable = false, length = 500)
    private String nom;

    @Column(length = 500)
    private String description;

    @Column(length = 500)
    private String atcCode;

    @Column(length = 500)
    private String forme;

    @Column(length = 500)
    private String dosageUnite;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;


}