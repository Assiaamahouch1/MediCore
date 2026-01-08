package com.example.factureservice.dto;

import java.time.LocalDateTime;

public record FactureCreeEvent(
        Long factureId,
        Long rendezVousId,
        Double montant,
        LocalDateTime date
) {}