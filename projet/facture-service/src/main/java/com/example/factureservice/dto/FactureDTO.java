package com.example.factureservice.dto;

import com.example.factureservice.model.ModePaiement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author sawssan
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FactureDTO {
    private Long idFacture;
    private Double montant;
    private ModePaiement modePaiement;
    private Long rendezVousId;
    private LocalDateTime date;
}
