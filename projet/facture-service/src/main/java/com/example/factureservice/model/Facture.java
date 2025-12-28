package com.example.factureservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author sawssan
 **/
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Facture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idFacture;

    private Double montant;

    @Enumerated(EnumType.STRING)
    private ModePaiement modePaiement;

    private Long rendezVousId;

}
