package com.example.rendezvousservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO pour les statistiques de consultations par jour
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConsultationStatsDTO {
    
    /** Date au format "yyyy-MM-dd" */
    private String date;
    
    /** Jour de la semaine (Lun, Mar, etc.) */
    private String jour;
    
    /** Nombre de consultations ce jour */
    private long count;
}

