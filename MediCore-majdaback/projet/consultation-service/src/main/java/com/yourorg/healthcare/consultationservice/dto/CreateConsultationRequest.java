package com.yourorg.healthcare.consultationservice.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Date;
import java.util.UUID;

public record CreateConsultationRequest(

        String type,

        @NotNull Date dateConsultation,

        @Size(max = 2000) String examenClinique,
        @Size(max = 2000) String examenComplementaire,
        @Size(max = 1000) String diagnostic,
        @Size(max = 2000) String traitement,
        @Size(max = 2000) String observations,

        @NotNull UUID patientId
) {}
