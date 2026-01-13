package com.example.rendezvousservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO pour les statistiques du dashboard médecin
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardStatsDTO {
    
    /** Nombre de patients arrivés (en salle d'attente) */
    private long patientsEnAttenteSalle;
    
    /** Nombre de RDV confirmés pour aujourd'hui */
    private long rdvConfirmesAujourdhui;
    
    /** Nombre de RDV en attente de confirmation */
    private long rdvEnAttenteConfirmation;
    
    /** Nombre de consultations effectuées aujourd'hui (statut HISTORIQUE) */
    private long consultationsAujourdhui;
    
    /** Total des RDV du jour (tous statuts confondus) */
    private long totalRdvJour;
}

