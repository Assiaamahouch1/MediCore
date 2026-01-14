package com.example.rendezvousservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO pour les statistiques de consultations par jour
 * @author sawssan
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConsultationStatsDTO {
    private String date;
    private String jour;
    private int count;
}

