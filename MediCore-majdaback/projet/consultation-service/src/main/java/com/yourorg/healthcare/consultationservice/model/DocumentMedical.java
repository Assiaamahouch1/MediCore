package com.yourorg.healthcare.consultationservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "documents_consultation")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class DocumentMedical {

    @Id
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(nullable = false, length = 255)
    private String antMedicaux;
    @Column(nullable = false, length = 255)
    private String antChirug;
    @Column(nullable = false, length = 255)
    private String allergies;
    @Column(nullable = false, length = 255)
    private String traitement;
    @Column(nullable = false, length = 255)
    private String habitudes;


    @Column(nullable = false)
    private UUID patientId;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;


}