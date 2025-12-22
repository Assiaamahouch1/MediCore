package com.example.cabinetservice.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record SubscriptionSetExpirationRequest(
        @NotNull LocalDate expirationDate
) { }