package com.yourorg.healthcare.consultationservice.dto;

import jakarta.validation.constraints.Size;

import java.util.UUID;

public record CreateLigneOrdonnanceRequest(
        @Size(max = 500) String nom,
        @Size(max = 255) String dosage,
        @Size(max = 255) String duree,
        @Size(max = 255) String forme,
        UUID medicamentId
) {}

