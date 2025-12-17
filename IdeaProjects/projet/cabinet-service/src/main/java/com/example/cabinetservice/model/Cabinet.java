package com.example.cabinetservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "cabinets")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cabinet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String logo;
    private String nom;
    private String specialite;
    private String adresse;
    private String tel;
    private Boolean service_actif;
    private LocalDate date_expiration_service;
}
