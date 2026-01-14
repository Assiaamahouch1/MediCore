package com.example.cabinetservice.service;

import com.example.cabinetservice.dto.ChatbotCabinetResult;
import com.example.cabinetservice.dto.ChatbotSearchRequest;
import com.example.cabinetservice.dto.ChatbotSearchResponse;
import com.example.cabinetservice.model.Cabinet;
import com.example.cabinetservice.repository.CabinetRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ChatbotService {

    private final CabinetRepository cabinetRepository;

    public ChatbotService(CabinetRepository cabinetRepository) {
        this.cabinetRepository = cabinetRepository;
    }

    /**
     * Récupère la liste des spécialités disponibles
     */
    public List<String> getSpecialites() {
        return cabinetRepository.findAll().stream()
                .filter(c -> Boolean.TRUE.equals(c.getService_actif()))
                .map(Cabinet::getSpecialite)
                .filter(s -> s != null && !s.isBlank())
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    /**
     * Récupère la liste des villes disponibles
     */
    public List<String> getVilles() {
        return cabinetRepository.findAll().stream()
                .filter(c -> Boolean.TRUE.equals(c.getService_actif()))
                .map(Cabinet::getVille)
                .filter(v -> v != null && !v.isBlank())
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    /**
     * Recherche des cabinets selon les critères du chatbot
     */
    public ChatbotSearchResponse searchCabinets(ChatbotSearchRequest request) {
        List<Cabinet> allCabinets = cabinetRepository.findAll().stream()
                .filter(c -> Boolean.TRUE.equals(c.getService_actif()))
                .collect(Collectors.toList());

        List<Cabinet> filteredCabinets = allCabinets;

        // Filtrer par spécialité
        if (request.specialite() != null && !request.specialite().isBlank()) {
            filteredCabinets = filteredCabinets.stream()
                    .filter(c -> c.getSpecialite() != null && 
                                 c.getSpecialite().toLowerCase().contains(request.specialite().toLowerCase()))
                    .collect(Collectors.toList());
        }

        // Filtrer par ville
        if (request.ville() != null && !request.ville().isBlank()) {
            filteredCabinets = filteredCabinets.stream()
                    .filter(c -> c.getVille() != null && 
                                 c.getVille().toLowerCase().contains(request.ville().toLowerCase()))
                    .collect(Collectors.toList());
        }

        // Filtrer par nom de médecin/cabinet
        if (request.nomMedecin() != null && !request.nomMedecin().isBlank()) {
            filteredCabinets = filteredCabinets.stream()
                    .filter(c -> c.getNom() != null && 
                                 c.getNom().toLowerCase().contains(request.nomMedecin().toLowerCase()))
                    .collect(Collectors.toList());
        }

        // Convertir en résultats
        List<ChatbotCabinetResult> results = filteredCabinets.stream()
                .map(this::toChatbotResult)
                .collect(Collectors.toList());

        String message;
        if (results.isEmpty()) {
            message = "Désolé, aucun cabinet ne correspond à vos critères. Essayez avec d'autres paramètres.";
        } else if (results.size() == 1) {
            message = "Nous avons trouvé 1 cabinet correspondant à votre recherche !";
        } else {
            message = "Nous avons trouvé " + results.size() + " cabinets correspondant à votre recherche !";
        }

        return new ChatbotSearchResponse(
                !results.isEmpty(),
                message,
                results.size(),
                results
        );
    }

    /**
     * Génère des horaires disponibles simulés pour un cabinet
     */
    public List<String> getHorairesDisponibles(Long cabinetId) {
        // Vérifier que le cabinet existe et est actif
        Optional<Cabinet> cabinetOpt = cabinetRepository.findById(cabinetId);
        if (cabinetOpt.isEmpty() || !Boolean.TRUE.equals(cabinetOpt.get().getService_actif())) {
            return Collections.emptyList();
        }

        // Générer des créneaux horaires simulés pour les 5 prochains jours
        List<String> horaires = new ArrayList<>();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("EEEE dd MMMM", Locale.FRENCH);
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        
        Random random = new Random(cabinetId); // Seed basé sur l'ID pour cohérence
        
        for (int day = 1; day <= 5; day++) {
            LocalDate date = LocalDate.now().plusDays(day);
            String dateStr = date.format(dateFormatter);
            
            // Générer 2-4 créneaux par jour
            int slotsCount = 2 + random.nextInt(3);
            List<LocalTime> daySlots = new ArrayList<>();
            
            // Créneaux du matin (9h-12h)
            if (random.nextBoolean()) {
                int hour = 9 + random.nextInt(3);
                int minute = random.nextBoolean() ? 0 : 30;
                daySlots.add(LocalTime.of(hour, minute));
            }
            
            // Créneaux de l'après-midi (14h-18h)
            for (int i = 0; i < slotsCount - 1; i++) {
                int hour = 14 + random.nextInt(4);
                int minute = random.nextBoolean() ? 0 : 30;
                daySlots.add(LocalTime.of(hour, minute));
            }
            
            // Trier et formater
            daySlots.stream()
                    .sorted()
                    .distinct()
                    .forEach(time -> {
                        horaires.add(dateStr + " à " + time.format(timeFormatter));
                    });
        }
        
        return horaires.stream().limit(8).collect(Collectors.toList());
    }

    private ChatbotCabinetResult toChatbotResult(Cabinet cabinet) {
        return new ChatbotCabinetResult(
                cabinet.getId(),
                cabinet.getNom(),
                cabinet.getSpecialite(),
                cabinet.getAdresse(),
                cabinet.getVille(),
                cabinet.getTel(),
                cabinet.getLogo(),
                getHorairesDisponibles(cabinet.getId())
        );
    }
}

