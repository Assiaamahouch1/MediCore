package com.yourorg.healthcare.patient.mapper;

import com.yourorg.healthcare.patient.dto.PatientRequest;
import com.yourorg.healthcare.patient.dto.PatientResponse;
import com.yourorg.healthcare.patient.model.Patient;

public final class PatientMapper {

    private PatientMapper() {}

    public static Patient toEntity(PatientRequest req, Patient target) {
        if (target == null) target = new Patient();
        target.setCin(req.getCin());
        target.setNom(req.getNom());
        target.setPrenom(req.getPrenom());
        target.setDateNaissance(req.getDateNaissance());
        target.setSexe(req.getSexe());
        target.setNumTel(req.getNumTel());
        target.setEmail(req.getEmail());
        target.setAdresse(req.getAdresse());
        target.setMutuelleNom(req.getMutuelleNom());
        target.setMutuelleNumero(req.getMutuelleNumero());
        target.setMutuelleExpireLe(req.getMutuelleExpireLe());
        return target;
    }

    public static PatientResponse toResponse(Patient entity) {
        PatientResponse dto = new PatientResponse();
        dto.setId(entity.getId());
        dto.setCin(entity.getCin());
        dto.setNom(entity.getNom());
        dto.setPrenom(entity.getPrenom());
        dto.setDateNaissance(entity.getDateNaissance());
        dto.setSexe(entity.getSexe());
        dto.setNumTel(entity.getNumTel());
        dto.setEmail(entity.getEmail());
        dto.setAdresse(entity.getAdresse());
        dto.setMutuelleNom(entity.getMutuelleNom());
        dto.setMutuelleNumero(entity.getMutuelleNumero());
        dto.setMutuelleExpireLe(entity.getMutuelleExpireLe());
        dto.setActif(entity.isActif());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }
}