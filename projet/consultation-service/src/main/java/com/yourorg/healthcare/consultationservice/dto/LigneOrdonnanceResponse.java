package com.yourorg.healthcare.consultationservice.dto;

import java.util.UUID;

public record LigneOrdonnanceResponse(
        UUID id,
        UUID ordonnanceId,
        String description,
        String dosage,
        String duree,
        UUID medicamentId
) {}
