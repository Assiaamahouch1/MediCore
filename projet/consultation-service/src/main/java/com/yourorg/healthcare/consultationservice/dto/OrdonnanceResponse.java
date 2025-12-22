package com.yourorg.healthcare.consultationservice.dto;

import java.time.Instant;
import java.util.UUID;

public record OrdonnanceResponse(
        UUID id,
        Instant date,
        String type,
        UUID consultationId,
        Instant createdAt
) {}