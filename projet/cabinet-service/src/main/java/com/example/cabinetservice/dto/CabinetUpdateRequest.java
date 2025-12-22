package com.example.cabinetservice.dto;

import jakarta.validation.constraints.Size;

public record CabinetUpdateRequest(
        String logo,
        @Size(max = 100) String nom,
        @Size(max = 100) String specialite,
        @Size(max = 255) String adresse,
        @Size(max = 30) String tel
) { }