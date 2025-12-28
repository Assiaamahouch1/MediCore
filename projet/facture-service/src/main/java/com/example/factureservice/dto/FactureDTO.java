package com.example.factureservice.dto;

import com.example.factureservice.model.ModePaiement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
}
