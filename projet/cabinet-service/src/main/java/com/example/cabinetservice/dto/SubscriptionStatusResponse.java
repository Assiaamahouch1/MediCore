package com.example.cabinetservice.dto;

public record SubscriptionStatusResponse(
        boolean active,
        boolean expired,
        long daysRemaining
) { }