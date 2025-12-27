package com.yourorg.healthcare.patient.exception;

import java.util.UUID;

public class PatientNotFoundException extends RuntimeException {
    public PatientNotFoundException(UUID id) {
        super("Patient non trouvé: " + id);
    }
}