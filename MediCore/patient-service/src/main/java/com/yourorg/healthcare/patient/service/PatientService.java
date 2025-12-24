package com.yourorg.healthcare.patient.service;

import com.yourorg.healthcare.patient.dto.PatientRequest;
import com.yourorg.healthcare.patient.dto.PatientResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface PatientService {
    PatientResponse create(PatientRequest request);
    Page<PatientResponse> list(String q, Pageable pageable);
    PatientResponse get(UUID id);
    PatientResponse update(UUID id, PatientRequest request);
    void delete(UUID id); // soft delete: actif=false
}