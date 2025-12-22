package com.yourorg.healthcare.consultationservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record DocumentMedicalRequest(
        @NotNull UUID consultationId,
        @NotBlank @Size(max=255) String titre,
        @NotBlank @Size(max=255) String url
) {}
