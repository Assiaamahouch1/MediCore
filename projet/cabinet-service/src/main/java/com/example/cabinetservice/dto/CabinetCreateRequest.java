package com.example.cabinetservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CabinetCreateRequest(
        String logo,
        @NotBlank @Size(max = 100) String nom,
        @Size(max = 100) String specialite,
        @Size(max = 255) String adresse,
        @Size(max = 100) String ville,
        @Size(max = 30) String tel
) {}
