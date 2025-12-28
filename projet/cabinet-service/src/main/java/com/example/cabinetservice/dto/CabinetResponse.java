package com.example.cabinetservice.dto;

import java.time.LocalDate;

public record CabinetResponse(
        Long id,
        String logo,
        String nom,
        String specialite,
        String adresse,
        String tel,
        Boolean service_actif,
        LocalDate date_expiration_service
) {}