package com.example.cabinetservice.dto;

/**
 * DTO pour la recherche via chatbot
 */
public record ChatbotSearchRequest(
    String specialite,
    String ville,
    String nomMedecin
) {}

