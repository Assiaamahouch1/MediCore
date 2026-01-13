package com.example.factureservice.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime date;

}
