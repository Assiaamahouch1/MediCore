package com.example.rendezvousservice.dto;
import com.example.rendezvousservice.model.StatutRdv;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RendezVousUpdateRequest {


    private Date dateRdv;
    private String heureRdv;
    private String motif;

    private String notes;
    private Long cabinetId;




}
