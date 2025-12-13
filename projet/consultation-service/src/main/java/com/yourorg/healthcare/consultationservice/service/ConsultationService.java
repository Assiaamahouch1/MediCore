package com.yourorg.healthcare.consultationservice.service;

import com.yourorg.healthcare.consultationservice.dto.ConsultationRequest;
import com.yourorg.healthcare.consultationservice.dto.ConsultationResponse;
import com.yourorg.healthcare.consultationservice.model.Consultation;
import com.yourorg.healthcare.consultationservice.repository.ConsultationRepository;
import org.springframework.data.domain.*;
        import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ConsultationService {
    private final ConsultationRepository repo;

    public ConsultationService(ConsultationRepository repo) {
        this.repo = repo;
    }

    public Page<ConsultationResponse> list(Integer page, Integer size, String sort) {
        Sort s = Sort.by(sort == null || sort.isBlank() ? "date" : sort.split(",")[0]);
        if (sort != null && sort.toLowerCase().endsWith(",desc")) s = s.descending();
        Pageable pageable = PageRequest.of(page == null ? 0 : page, size == null ? 20 : size, s);
        return repo.findAll(pageable).map(this::toResponse);
    }

    public ConsultationResponse get(UUID id) {
        return repo.findById(id).map(this::toResponse)
                .orElseThrow(() -> new IllegalArgumentException("Consultation not found: " + id));
    }

    public ConsultationResponse create(ConsultationRequest req) {
        Consultation c = Consultation.builder()
                .patientId(req.patientId())
                .doctorId(req.doctorId())
                .date(req.date())
                .notes(req.notes())
                .diagnosis(req.diagnosis())
                .prescription(req.prescription())
                .build();
        return toResponse(repo.save(c));
    }

    public ConsultationResponse update(UUID id, ConsultationRequest req) {
        Consultation c = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Consultation not found: " + id));
        c.setPatientId(req.patientId());
        c.setDoctorId(req.doctorId());
        c.setDate(req.date());
        c.setNotes(req.notes());
        c.setDiagnosis(req.diagnosis());
        c.setPrescription(req.prescription());
        return toResponse(repo.save(c));
    }

    public void delete(UUID id) { repo.deleteById(id); }

    private ConsultationResponse toResponse(Consultation c) {
        return new ConsultationResponse(
                c.getId(), c.getPatientId(), c.getDoctorId(), c.getDate(),
                c.getNotes(), c.getDiagnosis(), c.getPrescription(),
                c.getCreatedAt(), c.getUpdatedAt()
        );
    }
}