package com.yourorg.healthcare.consultationservice.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateDocumentMedicalRequest(

        @NotBlank String antMedicaux,
        @NotBlank String antChirug,
        @NotBlank String allergies,
        @NotBlank String traitement,
        @NotBlank String habitudes
) {}
