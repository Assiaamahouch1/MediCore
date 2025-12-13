package com.yourorg.healthcare.patient.service.impl;

import com.yourorg.healthcare.patient.dto.PatientRequest;
import com.yourorg.healthcare.patient.dto.PatientResponse;
import com.yourorg.healthcare.patient.exception.PatientNotFoundException;
import com.yourorg.healthcare.patient.mapper.PatientMapper;
import com.yourorg.healthcare.patient.model.Patient;
import com.yourorg.healthcare.patient.repository.PatientRepository;
import com.yourorg.healthcare.patient.service.PatientService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class PatientServiceImpl implements PatientService {

    private final PatientRepository repository;

    public PatientServiceImpl(PatientRepository repository) {
        this.repository = repository;
    }

    @Override
    public PatientResponse create(PatientRequest request) {
        Patient entity = PatientMapper.toEntity(request, null);
        entity = repository.save(entity);
        return PatientMapper.toResponse(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PatientResponse> list(String q, Pageable pageable) {
        Page<Patient> page = (q == null || q.isBlank())
                ? repository.findActive(pageable)
                : repository.searchActive(q.trim(), pageable);
        return page.map(PatientMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public PatientResponse get(UUID id) {
        Patient p = repository.findByIdAndActifTrue(id).orElseThrow(() -> new PatientNotFoundException(id));
        return PatientMapper.toResponse(p);
    }

    @Override
    public PatientResponse update(UUID id, PatientRequest request) {
        Patient existing = repository.findByIdAndActifTrue(id).orElseThrow(() -> new PatientNotFoundException(id));
        PatientMapper.toEntity(request, existing);
        return PatientMapper.toResponse(existing);
    }

    @Override
    public void delete(UUID id) {
        Patient existing = repository.findByIdAndActifTrue(id).orElseThrow(() -> new PatientNotFoundException(id));
        existing.setActif(false); // soft delete
    }
}