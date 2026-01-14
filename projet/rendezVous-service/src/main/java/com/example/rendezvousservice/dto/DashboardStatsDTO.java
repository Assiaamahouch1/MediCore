package com.example.rendezvousservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO pour les statistiques du dashboard médecin
 * @author sawssan
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStatsDTO {
    private int patientsEnAttenteSalle;
    private int rdvConfirmesAujourdhui;
    private int rdvEnAttenteConfirmation;
    private int consultationsAujourdhui;
    private int totalRdvJour;
}

