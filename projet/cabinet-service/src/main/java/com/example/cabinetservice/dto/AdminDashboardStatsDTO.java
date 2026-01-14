package com.example.cabinetservice.dto;

import java.util.Map;

/**
 * DTO pour les statistiques du dashboard admin/superadmin
 */
public record AdminDashboardStatsDTO(
        /** Nombre total de cabinets */
        long totalCabinets,

        /** Nombre de cabinets actifs */
        long cabinetsActifs,

        /** Nombre de cabinets inactifs */
        long cabinetsInactifs,

        /** Nombre de cabinets expirant dans les 7 jours */
        long cabinetsExpirantBientot,

        /** Répartition par spécialité */
        Map<String, Long> repartitionParSpecialite,

        /** Nombre de cabinets expirés */
        long cabinetsExpires
) {}
