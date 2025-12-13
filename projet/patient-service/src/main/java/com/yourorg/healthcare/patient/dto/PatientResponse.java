package com.yourorg.healthcare.patient.dto;

import com.yourorg.healthcare.patient.model.Patient.Sexe;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class PatientResponse {
    private UUID id;
    private String cin;
    private String nom;
    private String prenom;
    private LocalDate dateNaissance;
    private Sexe sexe;
    private String numTel;
    private String email;
    private String adresse;
    private String mutuelleNom;
    private String mutuelleNumero;
    private LocalDate mutuelleExpireLe;
    private boolean actif;
    private Instant createdAt;
    private Instant updatedAt;
}