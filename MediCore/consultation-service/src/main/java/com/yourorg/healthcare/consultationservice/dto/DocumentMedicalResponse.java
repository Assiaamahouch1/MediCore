package com.yourorg.healthcare.consultationservice.dto;

import java.util.UUID;

public record DocumentMedicalResponse(
        UUID id,
        UUID consultationId,
        String titre,
        String url
) {}
