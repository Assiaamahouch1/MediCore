package com.yourorg.healthcare.patient.dto;


import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PatientRequest {

    @Size(max = 50) private String cin;

    @NotBlank @Size(max = 100) private String nom;

    @NotBlank @Size(max = 100) private String prenom;

    @Past(message = "dateNaissance doit être dans le passé")
    private LocalDate dateNaissance;

    private String sexe;

    @Pattern(regexp = "^[0-9+()\\-\\s]{6,20}$", message = "numTel invalide")
    private String numTel;

    @Email @Size(max = 180) private String email;

    @Size(max = 255) private String adresse;

    @Size(max = 120) private String mutuelleNom;

    @Size(max = 60) private String mutuelleNumero;

    private LocalDate mutuelleExpireLe;
    private Long cabinetId;
}