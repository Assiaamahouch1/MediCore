package com.yourorg.healthcare.consultationservice.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record LigneOrdonnanceRequest(
        @NotNull UUID ordonnanceId,
        @Size(max=500) String description,
        @Size(max=255) String dosage,
        @Size(max=255) String duree,
        UUID medicamentId
) {}
