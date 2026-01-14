package com.yourorg.healthcare.consultationservice.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.UUID;

public record CreateOrdonnanceRequest(
        @NotNull UUID consultationId,
        @Size(max = 50) String type,
        List<CreateLigneOrdonnanceRequest> lignes
) {}

