package com.example.rendezvousservice.dto;

import com.example.rendezvousservice.model.StatutRdv;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author sawssan
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RendezVousDTO {
    private Long idRdv;
    private Date dateRdv;
    private String heureRdv;
    private String motif;
    private StatutRdv statut;
    private String notes;
    private Long patientId;
    private Long userId;

}
