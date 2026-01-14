package com.example.cabinetservice.controller;

import com.example.cabinetservice.dto.ChatbotSearchRequest;
import com.example.cabinetservice.dto.ChatbotSearchResponse;
import com.example.cabinetservice.service.ChatbotService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controller pour le chatbot de la page d'accueil
 * Ces endpoints sont publics (sans authentification)
 * CORS est géré au niveau de l'API Gateway
 */
@RestController
@RequestMapping("/api/chatbot")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS})
public class ChatbotController {

    private final ChatbotService chatbotService;

    public ChatbotController(ChatbotService chatbotService) {
        this.chatbotService = chatbotService;
    }

    /**
     * Récupère la liste des spécialités disponibles
     */
    @GetMapping("/specialites")
    public ResponseEntity<List<String>> getSpecialites() {
        return ResponseEntity.ok(chatbotService.getSpecialites());
    }

    /**
     * Récupère la liste des villes disponibles
     */
    @GetMapping("/villes")
    public ResponseEntity<List<String>> getVilles() {
        return ResponseEntity.ok(chatbotService.getVilles());
    }

    /**
     * Recherche des cabinets selon les critères
     */
    @PostMapping("/search")
    public ResponseEntity<ChatbotSearchResponse> searchCabinets(
            @RequestBody ChatbotSearchRequest request) {
        return ResponseEntity.ok(chatbotService.searchCabinets(request));
    }

    /**
     * Récupère les horaires disponibles pour un cabinet
     */
    @GetMapping("/horaires/{cabinetId}")
    public ResponseEntity<List<String>> getHoraires(@PathVariable Long cabinetId) {
        return ResponseEntity.ok(chatbotService.getHorairesDisponibles(cabinetId));
    }

    /**
     * Message de bienvenue du chatbot
     */
    @GetMapping("/welcome")
    public ResponseEntity<Map<String, Object>> getWelcomeMessage() {
        return ResponseEntity.ok(Map.of(
            "message", "Bonjour ! 👋 Je suis votre assistant MediCore. Je vais vous aider à trouver le médecin qu'il vous faut.",
            "nextStep", "specialite",
            "question", "Quel type de médecin recherchez-vous ?",
            "options", chatbotService.getSpecialites()
        ));
    }
}

