package com.yourorg.healthcare.consultationservice.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.Instant;
import java.util.UUID;

public record OrdonnanceRequest(
        Instant date,
        @Size(max=50) String type,
        @NotNull UUID consultationId
) {}
