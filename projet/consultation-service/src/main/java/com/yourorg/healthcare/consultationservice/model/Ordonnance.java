package com.yourorg.healthcare.consultationservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "ordonnances", indexes = {
        @Index(name = "idx_ordonnance_consultation", columnList = "consultationId")
})
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

    @PrePersist
    void prePersist() {
        if (id == null) id = UUID.randomUUID();
        if (date == null) date = Instant.now();
        createdAt = Instant.now();
    }
}
