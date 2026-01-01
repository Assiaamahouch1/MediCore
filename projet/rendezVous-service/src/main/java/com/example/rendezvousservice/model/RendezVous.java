package com.example.rendezvousservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

/**
 * @author sawssan
 **/
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RendezVous {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idRdv;

    private Date dateRdv;
    private String heureRdv;
    private String motif;

    @Enumerated(EnumType.STRING)
    private StatutRdv statut;

    private String notes;
    private UUID patientId;
    private Long userId;



}
