package com.yourorg.healthcare.consultationservice.dto;

import java.time.Instant;
import java.util.UUID;

public record ConsultationResponse(
        UUID id,
        String type,
        Instant dateConsultation,
        String examenClinique,
        String examenComplementaire,
        String diagnostic,
        String traitement,
        String observations,
        UUID patientId,
        UUID medecinId,
        UUID rendezVousId,
        UUID dossierMedicalId,
        Instant createdAt,
        Instant updatedAt
) {}