package com.yourorg.healthcare.patient.service.impl;

import com.yourorg.healthcare.patient.dto.PatientRequest;
import com.yourorg.healthcare.patient.dto.PatientResponse;
import com.yourorg.healthcare.patient.exception.PatientNotFoundException;
import com.yourorg.healthcare.patient.mapper.PatientMapper;
import com.yourorg.healthcare.patient.model.Patient;
import com.yourorg.healthcare.patient.repository.PatientRepository;
import com.yourorg.healthcare.patient.service.PatientService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;
    @Override
    public PatientResponse getPatientById(UUID id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Patient non trouvé avec l'id : " + id));
        return mapToResponse(patient);
    }

    // Créer un patient
    @Override
    public PatientResponse creerPatient(PatientRequest request) {
        Patient patient = Patient.builder()
                .cin(request.getCin())
                .nom(request.getNom())
                .prenom(request.getPrenom())
                .dateNaissance(request.getDateNaissance())
                .sexe(request.getSexe())
                .numTel(request.getNumTel())
                .email(request.getEmail())
                .adresse(request.getAdresse())
                .mutuelleNom(request.getMutuelleNom())
                .mutuelleNumero(request.getMutuelleNumero())
                .mutuelleExpireLe(request.getMutuelleExpireLe())
                .cabinetId(request.getCabinetId())
                .build();

        Patient saved = patientRepository.save(patient);
        return mapToResponse(saved);
    }

    // Modifier un patient
    @Override
    public PatientResponse modifierPatient(UUID id, PatientRequest request) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Patient non trouvé avec l'id : " + id));

        patient.setCin(request.getCin());
        patient.setNom(request.getNom());
        patient.setPrenom(request.getPrenom());
        patient.setDateNaissance(request.getDateNaissance());
        patient.setSexe(request.getSexe());
        patient.setNumTel(request.getNumTel());
        patient.setEmail(request.getEmail());
        patient.setAdresse(request.getAdresse());
        patient.setMutuelleNom(request.getMutuelleNom());
        patient.setMutuelleNumero(request.getMutuelleNumero());
        patient.setMutuelleExpireLe(request.getMutuelleExpireLe());
        patient.setCabinetId(request.getCabinetId());

        Patient updated = patientRepository.save(patient);
        return mapToResponse(updated);
    }
    @Override
    public void supprimerPatient(UUID id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Patient non trouvé avec l'id : " + id));

        patient.setActif(false); // soft delete
        patientRepository.save(patient);
    }
    @Override
    public void RestaurerrPatient(UUID id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Patient non trouvé avec l'id : " + id));

        patient.setActif(true); // soft delete
        patientRepository.save(patient);
    }





    // Mapper Patient -> PatientResponse

    public PatientResponse mapToResponse(Patient patient) {
        PatientResponse response = new PatientResponse();
        response.setId(patient.getId());
        response.setCin(patient.getCin());
        response.setNom(patient.getNom());
        response.setPrenom(patient.getPrenom());
        response.setDateNaissance(patient.getDateNaissance());
        response.setSexe(patient.getSexe());
        response.setNumTel(patient.getNumTel());
        response.setEmail(patient.getEmail());
        response.setAdresse(patient.getAdresse());
        response.setMutuelleNom(patient.getMutuelleNom());
        response.setMutuelleNumero(patient.getMutuelleNumero());
        response.setMutuelleExpireLe(patient.getMutuelleExpireLe());
        response.setActif(patient.isActif());
        response.setCreatedAt(patient.getCreatedAt());
        response.setUpdatedAt(patient.getUpdatedAt());
        return response;
    }
    @Override
    public List<Patient> getAll(Long cabinetId) {
        return patientRepository.findByCabinetIdAndActifTrue(cabinetId);
    }

    @Override
    public List<Patient> getAllNoActif(Long cabinetId) {
        return this.patientRepository.findByCabinetIdAndActifFalse(cabinetId);
    }




}