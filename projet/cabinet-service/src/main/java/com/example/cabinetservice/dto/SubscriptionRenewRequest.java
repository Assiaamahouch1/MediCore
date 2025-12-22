package com.example.cabinetservice.dto;

import jakarta.validation.constraints.Min;

public record SubscriptionRenewRequest(@Min(1) int months) {}