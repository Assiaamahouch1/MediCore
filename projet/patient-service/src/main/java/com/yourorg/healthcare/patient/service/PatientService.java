package com.yourorg.healthcare.patient.service;

import com.yourorg.healthcare.patient.dto.PatientRequest;
import com.yourorg.healthcare.patient.dto.PatientResponse;
import com.yourorg.healthcare.patient.model.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface PatientService {
    public PatientResponse creerPatient(PatientRequest request);
    public PatientResponse modifierPatient(UUID id, PatientRequest request);
    public void supprimerPatient(UUID id);
    public void RestaurerrPatient(UUID id);
    public PatientResponse getPatientById(UUID id);
    public List<Patient> getAll(Long cabinetId);
    public List<Patient> getAllNoActif(Long cabinetId);


}