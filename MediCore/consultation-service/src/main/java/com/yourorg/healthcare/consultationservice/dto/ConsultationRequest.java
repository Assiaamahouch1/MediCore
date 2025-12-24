package com.yourorg.healthcare.consultationservice.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.Instant;
import java.util.UUID;

public record ConsultationRequest(
        @Size(max=50) String type,
        Instant dateConsultation,
        @Size(max=2000) String examenClinique,
        @Size(max=2000) String examenComplementaire,
        @Size(max=1000) String diagnostic,
        @Size(max=2000) String traitement,
        @Size(max=2000) String observations,
        @NotNull UUID patientId,
        @NotNull UUID medecinId,
        UUID rendezVousId,
        UUID dossierMedicalId
) {}