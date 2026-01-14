package com.example.cabinetservice.dto;

import java.util.List;

/**
 * DTO pour la réponse de recherche du chatbot
 */
public record ChatbotSearchResponse(
    boolean success,
    String message,
    int totalResults,
    List<ChatbotCabinetResult> cabinets
) {}

