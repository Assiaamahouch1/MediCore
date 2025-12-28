package com.yourorg.healthcare.consultationservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "documents_consultation", indexes = {
        @Index(name = "idx_doc_consultation", columnList = "consultationId")
})
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class DocumentMedical {

    @Id
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(nullable = false, length = 255)
    private String titre;

    @Column(nullable = false, length = 255)
    private String url;

    @Column(nullable = false)
    private UUID consultationId;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    void prePersist() {
        if (id == null) id = UUID.randomUUID();
        createdAt = Instant.now();
    }
}