package com.yourorg.healthcare.patient.controller;

import com.yourorg.healthcare.patient.dto.PatientRequest;
import com.yourorg.healthcare.patient.dto.PatientResponse;
import com.yourorg.healthcare.patient.model.Patient;
import com.yourorg.healthcare.patient.service.PatientService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController

@RequestMapping("/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService service; // ✅ INJECTION CORRECTE

    // Créer un patient
    @PostMapping
    public ResponseEntity<PatientResponse> creerPatient(
            @Valid @RequestBody PatientRequest request) {

        PatientResponse response = service.creerPatient(request);
        return ResponseEntity
                .created(URI.create("/patients/" + response.getId()))
                .body(response);
    }

    // Modifier un patient
    @PutMapping("/{id}")

        public ResponseEntity<PatientResponse> modifierPatient(@PathVariable("id") UUID id,
                @RequestBody PatientRequest request){

        return ResponseEntity.ok(service.modifierPatient(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable("id") UUID id) {
        try {
            service.supprimerPatient(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            // Pour voir le détail de l'erreur
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @PutMapping("/restore/{id}")
    public ResponseEntity<Void> restorePatient(@PathVariable("id") UUID id) {
        try {
            service.RestaurerrPatient(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            // Pour voir le détail de l'erreur
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    // Récupérer par ID
    @GetMapping("/{id}")
    public ResponseEntity<PatientResponse> getPatient(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getPatientById(id));
    }

    // Récupérer tous les patients
    @GetMapping("/all")
    public List<Patient> getAllPatient() {
        return service.getAll();
    }
    @GetMapping("/allNoActif")
    public List<Patient> getAllPatientNoActif() {
        return service.getAllNoActif();
    }
}
