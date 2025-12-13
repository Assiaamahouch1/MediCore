package com.yourorg.healthcare.consultationservice.dto;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.Instant;
import java.util.UUID;

public record ConsultationRequest(
        @NotNull UUID patientId,
        @NotNull UUID doctorId,
        Instant date,
        @Size(max=1000) String notes,
        @Size(max=255) String diagnosis,
        @Size(max=255) String prescription
) {}