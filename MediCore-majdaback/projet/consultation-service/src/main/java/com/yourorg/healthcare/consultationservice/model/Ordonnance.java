package com.yourorg.healthcare.consultationservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "ordonnances")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Ordonnance {

    @Id
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(nullable = false)
    private Instant date;

    @Column(length = 50)
    private String type;

    @Column(nullable = false)
    private UUID consultationId;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;


}
