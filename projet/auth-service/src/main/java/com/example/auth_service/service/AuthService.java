package com.example.auth_service.service;




import com.example.auth_service.dto.*;
import com.example.auth_service.exception.ResourceNotFoundException;
import com.example.auth_service.mapper.UserMapper;
import com.example.auth_service.model.Role;
import com.example.auth_service.repository.UtilisateurRepository;

import com.example.auth_service.config.JwtUtils;

import com.example.auth_service.model.Utilisateur;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Function;

import static com.example.auth_service.Constant.Constant.IMAGE_DIRECTORY;
import static com.example.auth_service.Constant.Constant.IMAGE_DIRECTORY_SUPERADMIN;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

@Slf4j
@Service
public class AuthService {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private EmailService emailService;
    @Autowired
    private UtilisateurRepository repository;
    @Autowired
    private UserMapper mapper;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public String register(RegisterRequest request) {
        if(utilisateurRepository.existsByUsername(request.getUsername())) // ← change ici
            throw new RuntimeException("Login already exists");

        // Validation simple
        if ((request.getRole() == Role.MEDECIN || request.getRole() == Role.SECRETAIRE)
                && request.getCabinetId() == null) {
            throw new RuntimeException("cabinetId obligatoire pour MEDECIN/SECRETAIRE");
        }

        Utilisateur user = Utilisateur.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .nom(request.getNom())
                .prenom(request.getPrenom())
                .numTel(request.getNumTel())
                .signature(request.getSignature())
                .role(request.getRole())
                .cabinetId(request.getCabinetId())
                .actif(true)
                .build();

        utilisateurRepository.save(user);
        return "User registered successfully";
    }

    public String login(LoginRequest request) {
        Utilisateur user = utilisateurRepository.findByUsername(request.getUsername()) // ← change ici
                .orElseThrow(() -> new RuntimeException("User not found"));

        if(!passwordEncoder.matches(request.getPassword(), user.getPassword()))
            throw new RuntimeException("Invalid password");

        return jwtUtils.generateToken(user);
    }



    public List<Utilisateur> all() {

        return utilisateurRepository.findAll();
    }


    @Transactional
    public void initiatePasswordReset(String username) {
        Utilisateur user = utilisateurRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        if (!user.isActif()) {
            return; // Ne rien faire si compte désactivé
        }

        // Générer token unique
        String token = UUID.randomUUID().toString();

        user.setActivationToken(token);
        user.setTokenExpiryDate(LocalDateTime.now().plusHours(1)); // 1 heure

        utilisateurRepository.save(user);

        String fullName = user.getPrenom() + " " + user.getNom();
        emailService.sendPasswordResetEmail(user.getUsername(), token, fullName);
    }

    @Transactional
    public void completePasswordReset(String token, ResetPasswordRequest request) {
        Utilisateur user = utilisateurRepository.findByActivationToken(token)
                .orElseThrow(() -> new RuntimeException("Lien invalide ou déjà utilisé"));

        if (LocalDateTime.now().isAfter(user.getTokenExpiryDate())) {
            throw new RuntimeException("Ce lien a expiré. Veuillez refaire une demande.");
        }

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new RuntimeException("Les mots de passe ne correspondent pas.");
        }

        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setActivationToken(null);
        user.setTokenExpiryDate(null);

        utilisateurRepository.save(user);
    }
    public Utilisateur getCurrentUserInfo() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            throw new RuntimeException("Aucun utilisateur authentifié");
        }

        String username = auth.getName();
        Utilisateur user = utilisateurRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        return mapToUserInfoResponse(user);
    }

    private Utilisateur mapToUserInfoResponse(Utilisateur user) {
        Utilisateur response = new Utilisateur();
        response.setUsername(user.getUsername());
        response.setNom(user.getNom());
        response.setPrenom(user.getPrenom());
        response.setNumTel(user.getNumTel());
        response.setAvatar(user.getAvatar());
        response.setId(user.getId());
        response.setRole(user.getRole());
        response.setCabinetId(user.getCabinetId());
        return response;
    }

    public String uploadImage(Long adminId, MultipartFile file) {
        log.info("Saving picture for user ID: {}", adminId);
        Utilisateur admin= utilisateurRepository.findById(adminId)
                .orElseThrow(()->
                        new ResourceNotFoundException("User is not exist"+adminId));
        String imageUrl = imageFunction.apply(String.valueOf(adminId), file);
        admin.setAvatar(imageUrl);
        utilisateurRepository.save(admin);
        return imageUrl;
    }

    private final Function<String, String> fileExtension = filename -> Optional.of(filename).filter(name -> name.contains("."))
            .map(name -> "." + name.substring(filename.lastIndexOf(".") + 1)).orElse(".jpg");

    private final BiFunction<String, MultipartFile, String> imageFunction = (id, image) -> {
        String filename = id + fileExtension.apply(image.getOriginalFilename());
        try {
            Path fileStorageLocation = Paths.get(IMAGE_DIRECTORY_SUPERADMIN).toAbsolutePath().normalize();
            if(!Files.exists(fileStorageLocation)) { Files.createDirectories(fileStorageLocation); }
            Files.copy(image.getInputStream(), fileStorageLocation.resolve(filename), REPLACE_EXISTING);
            return filename;
        }catch (Exception exception) {
            throw new RuntimeException("Unable to save image");
        }
    };

    public UserDTO updateSuperAdmin(Long id, UserDTO dto) {
        Utilisateur entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur not found with id " + id));


        if (dto.getUsername() != null) {
            entity.setUsername(dto.getUsername());
        }
        if (dto.getNom() != null) {
            entity.setNom(dto.getNom());
        }
        if (dto.getPrenom() != null) {
            entity.setPrenom(dto.getPrenom());
        }
        if (dto.getNumTel() != null) {
            entity.setNumTel(dto.getNumTel());
        }
        if (dto.getAvatar() != null) {
            entity.setAvatar(dto.getAvatar());
        }
        return mapper.toDTO(repository.save(entity));
    }
}
