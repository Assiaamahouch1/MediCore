package com.example.cabinetservice.dto;

import java.util.List;

/**
 * DTO pour les résultats de recherche du chatbot
 */
public record ChatbotCabinetResult(
        Long id,
        String nom,
        String specialite,
        String adresse,
        String ville,
        String tel,
        String logo,
        List<String> horairesDisponibles
) {}
