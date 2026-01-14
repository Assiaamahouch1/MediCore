package com.example.cabinetservice.service;

import com.example.cabinetservice.dto.*;
import com.example.cabinetservice.mapper.CabinetMapper;
import com.example.cabinetservice.model.Cabinet;
import com.example.cabinetservice.repository.CabinetRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CabinetService {

    private final CabinetRepository repo;
    private final CabinetMapper mapper;

    public CabinetService(CabinetRepository repo, CabinetMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }







    public Cabinet create(Cabinet cabinet) {
        return repo.save(cabinet);
    }

    public Optional<Cabinet> getById(Long id) {
        return repo.findById(id);
    }

    public List<Cabinet> getAll() {
        return repo.findAll();
    }

    public Cabinet update(Long id, Cabinet cabinet) {
        return repo.findById(id).map(c -> {
            c.setNom(cabinet.getNom());
            c.setLogo(cabinet.getLogo());
            c.setAdresse(cabinet.getAdresse());
            c.setSpecialite(cabinet.getSpecialite());
            c.setTel(cabinet.getTel());
            c.setDate_expiration_service(cabinet.getDate_expiration_service());
            return repo.save(c);
        }).orElseThrow(() -> new RuntimeException("Cabinet not found"));
    }






    public Page<CabinetResponse> list(String q, Integer page, Integer size, String sort) {
        Sort s = Sort.by((sort == null || sort.isBlank()) ? "nom" : sort.split(",")[0]);
        if (sort != null && sort.toLowerCase().endsWith(",desc")) s = s.descending();
        Pageable pageable = PageRequest.of(page == null ? 0 : page, size == null ? 20 : size, s);
        Page<Cabinet> result = (q == null || q.isBlank())
                ? repo.findAll(pageable)
                : repo.findByNomContainingIgnoreCase(q.trim(), pageable);
        return result.map(mapper::toResponse);
    }

    public CabinetResponse get(Long id) {
        return repo.findById(id).map(mapper::toResponse)
                .orElseThrow(() -> new NoSuchElementException("Cabinet not found: " + id));
    }

    public CabinetResponse create(CabinetCreateRequest req) {
        Cabinet c = mapper.toEntity(req);
        return mapper.toResponse(repo.save(c));
    }

    public CabinetResponse update(Long id, CabinetUpdateRequest req) {
        Cabinet c = repo.findById(id).orElseThrow(() -> new NoSuchElementException("Cabinet not found: " + id));
        mapper.updateEntity(req, c);
        return mapper.toResponse(repo.save(c));
    }

    public void delete(Long id) { repo.deleteById(id); }

    public CabinetResponse activate(Long id) {
        Cabinet c = repo.findById(id).orElseThrow(() -> new NoSuchElementException("Cabinet not found: " + id));
        LocalDate today = LocalDate.now();
        if (c.getDate_expiration_service() == null || !c.getDate_expiration_service().isAfter(today)) {
            throw new IllegalStateException("Subscription expired or not set. Renew or set expiration before activation.");
        }
        c.setService_actif(Boolean.TRUE);
        return mapper.toResponse(repo.save(c));
    }

    public CabinetResponse deactivate(Long id) {
        Cabinet c = repo.findById(id).orElseThrow(() -> new NoSuchElementException("Cabinet not found: " + id));
        c.setService_actif(Boolean.FALSE);
        return mapper.toResponse(repo.save(c));
    }

    public CabinetResponse renew(Long id, SubscriptionRenewRequest req) {
        Cabinet c = repo.findById(id).orElseThrow(() -> new NoSuchElementException("Cabinet not found: " + id));
        LocalDate base = c.getDate_expiration_service();
        LocalDate today = LocalDate.now();
        if (base == null || !base.isAfter(today)) base = today;
        LocalDate newExp = base.plusMonths(req.months());
        c.setDate_expiration_service(newExp);
        c.setService_actif(Boolean.TRUE);
        return mapper.toResponse(repo.save(c));
    }

    public CabinetResponse setExpiration(Long id, SubscriptionSetExpirationRequest req) {
        Cabinet c = repo.findById(id).orElseThrow(() -> new NoSuchElementException("Cabinet not found: " + id));
        c.setDate_expiration_service(req.expirationDate());
        c.setService_actif(req.expirationDate().isAfter(LocalDate.now()));
        return mapper.toResponse(repo.save(c));
    }

    public SubscriptionStatusResponse status(Long id) {
        Cabinet c = repo.findById(id).orElseThrow(() -> new NoSuchElementException("Cabinet not found: " + id));
        LocalDate exp = c.getDate_expiration_service();
        LocalDate today = LocalDate.now();
        boolean expired = (exp == null) || !exp.isAfter(today);
        long remaining = expired ? 0 : ChronoUnit.DAYS.between(today, exp);
        boolean active = Boolean.TRUE.equals(c.getService_actif()) && !expired;
        return new SubscriptionStatusResponse(active, expired, remaining);
    }

    // Utilitaire pour le scheduler (pour test unitaire direct)
    public int deactivateExpired() {
        var expired = repo.findActiveExpired(LocalDate.now());
        expired.forEach(c -> c.setService_actif(Boolean.FALSE));
        repo.saveAll(expired);
        return expired.size();
    }

    // Récupérer les cabinets qui expirent dans X jours (pour les alertes admin)
    public List<CabinetResponse> getExpiringCabinets(int daysBeforeExpiration) {
        LocalDate today = LocalDate.now();
        LocalDate threshold = today.plusDays(daysBeforeExpiration);
        return repo.findExpiringCabinets(today, threshold)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }
    @Value("${cabinet.logo.upload-dir:uploads/logos}")
    private String uploadDir;

    public String uploadLogo(Long id, MultipartFile file) {
        Cabinet c = repo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Cabinet not found: " + id));

        try {
            // Créer le répertoire s'il n'existe pas
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Supprimer l'ancien logo s'il existe
            if (c.getLogo() != null && !c.getLogo().isBlank()) {
                Path oldFile = uploadPath.resolve(c.getLogo());
                Files.deleteIfExists(oldFile);
            }

            // Générer un nom unique pour le fichier
            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String newFilename = "cabinet_" + id + "_" + UUID.randomUUID().toString().substring(0, 8) + extension;

            // Sauvegarder le fichier
            Path filePath = uploadPath.resolve(newFilename);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // Mettre à jour le cabinet
            c.setLogo(newFilename);
            repo.save(c);

            return newFilename;

        } catch (IOException e) {
            throw new RuntimeException("Failed to upload logo: " + e.getMessage(), e);
        }
    }

    public Resource getLogo(String filename) {
        try {
            Path filePath = Paths.get(uploadDir).resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                throw new NoSuchElementException("Logo not found: " + filename);
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error reading logo: " + e.getMessage(), e);
        }
    }

    /**
     * Calcule les statistiques du dashboard admin
     */
    public AdminDashboardStatsDTO getDashboardStats() {
        LocalDate today = LocalDate.now();
        LocalDate threshold = today.plusDays(7);

        List<Cabinet> allCabinets = repo.findAll();
        System.out.println("Nombre total de cabinets trouvés: " + allCabinets.size());

        long totalCabinets = allCabinets.size();
        long cabinetsActifs = allCabinets.stream()
                .filter(c -> Boolean.TRUE.equals(c.getService_actif()))
                .count();
        long cabinetsInactifs = totalCabinets - cabinetsActifs;

        long cabinetsExpires = allCabinets.stream()
                .filter(c -> c.getDate_expiration_service() != null
                        && !c.getDate_expiration_service().isAfter(today))
                .count();

        long cabinetsExpirantBientot = allCabinets.stream()
                .filter(c -> c.getDate_expiration_service() != null
                        && c.getDate_expiration_service().isAfter(today)
                        && !c.getDate_expiration_service().isAfter(threshold))
                .count();

        // Répartition par spécialité
        Map<String, Long> repartitionParSpecialite = allCabinets.stream()
                .filter(c -> c.getSpecialite() != null && !c.getSpecialite().isBlank())
                .collect(Collectors.groupingBy(
                        Cabinet::getSpecialite,
                        Collectors.counting()
                ));

        AdminDashboardStatsDTO stats = new AdminDashboardStatsDTO(
                totalCabinets,
                cabinetsActifs,
                cabinetsInactifs,
                cabinetsExpirantBientot,
                repartitionParSpecialite,
                cabinetsExpires
        );

        System.out.println("Statistiques calculées - Total: " + totalCabinets +
                ", Actifs: " + cabinetsActifs +
                ", Inactifs: " + cabinetsInactifs);

        return stats;
    }


}