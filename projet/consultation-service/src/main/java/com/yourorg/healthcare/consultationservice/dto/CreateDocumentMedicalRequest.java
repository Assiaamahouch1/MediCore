package com.yourorg.healthcare.consultationservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateDocumentMedicalRequest(

        @NotBlank String antMedicaux,
        @NotBlank String antChirug,
        @NotBlank String allergies,
        @NotBlank String traitement,
        @NotBlank String habitudes,

        @NotNull UUID patientId
) {}
