package com.yourorg.healthcare.medicamentservice.service;

import com.yourorg.healthcare.medicamentservice.model.Medicament;
import com.yourorg.healthcare.medicamentservice.repository.MedicamentRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ExternalMedicamentService {

    private final MedicamentRepository medicamentRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    // Lien DIRECT officiel en 2026
    private static final String ANSM_URL = "https://base-donnees-publique.medicaments.gouv.fr/download/file/CIS_bdpm.txt";

    public ExternalMedicamentService(MedicamentRepository medicamentRepository) {
        this.medicamentRepository = medicamentRepository;
    }

    public void fetchAndSaveMedicaments() {
        System.out.println("=== Début import ANSM (médicaments français) ===");

        try {
            String content = restTemplate.getForObject(ANSM_URL, String.class);

            if (content == null || content.isEmpty()) {
                System.out.println("Erreur : fichier vide");
                return;
            }

            List<Medicament> medicaments = parseAnsmFile(content);

            // medicamentRepository.deleteAll(); // Décommente si tu veux vider avant

            medicamentRepository.saveAll(medicaments);

            System.out.println("Import réussi : " + medicaments.size() + " médicaments ajoutés !");

        } catch (Exception e) {
            System.err.println("Erreur import : " + e.getMessage());
            e.printStackTrace();
        }
    }

    private List<Medicament> parseAnsmFile(String text) {
        List<Medicament> list = new ArrayList<>();
        String[] lines = text.split("\n");

        for (int i = 1; i < lines.length; i++) {
            String line = lines[i].trim();
            if (line.isEmpty()) continue;

            String[] cols = line.split("\t");

            if (cols.length < 7) continue;

            String nom = cols[1].trim();
            if (nom.isEmpty()) continue;

            if (medicamentRepository.existsByNomIgnoreCase(nom)) continue;

            Medicament med = new Medicament();
            med.setNom(nom.replaceAll("\\d+\\s?mg(?:/\\d+\\s?mg)?", "").replaceAll(",.*", "").trim());
            med.setForme(cols[2].trim());
            Pattern p = Pattern.compile("\\d+\\s?mg(?:/\\d+\\s?mg)?");
            Matcher m = p.matcher(nom);
            if (m.find()) {
                med.setDosageUnite(m.group());
            } else {
                med.setDosageUnite("Non spécifié");
            }
            med.setDescription("Importé depuis ANSM");
            med.setAtcCode(cols.length > 10 ? cols[10].trim() : "");
            med.setCreatedAt(Instant.now());
            med.setUpdatedAt(Instant.now());

            list.add(med);
        }

        return list;
    }

    public void delete(UUID id) {
        medicamentRepository.deleteById(id);
    }
}