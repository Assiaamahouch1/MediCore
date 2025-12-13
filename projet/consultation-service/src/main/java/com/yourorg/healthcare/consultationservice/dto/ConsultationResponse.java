package com.yourorg.healthcare.consultationservice.dto;
import java.time.Instant;
import java.util.UUID;

public record ConsultationResponse(
        UUID id,
        UUID patientId,
        UUID doctorId,
        Instant date,
        String notes,
        String diagnosis,
        String prescription,
        Instant createdAt,
        Instant updatedAt
) {}