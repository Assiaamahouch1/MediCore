package com.example.auth_service.service;

import com.example.auth_service.dto.UserDTO;
import com.example.auth_service.exception.ResourceNotFoundException;
import com.example.auth_service.mapper.UserMapper;
import com.example.auth_service.model.Role;
import com.example.auth_service.model.Utilisateur;
import com.example.auth_service.repository.UtilisateurRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
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
import java.util.stream.Collectors;

import static com.example.auth_service.Constant.Constant.IMAGE_DIRECTORY;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

/**
 * @author sawssan
 **/
@Slf4j
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    @Autowired
    private UtilisateurRepository repository;

    private final UserMapper mapper;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    private EmailService emailService;
    @Override
    public UserDTO createAdmin(UserDTO dto) {
        if(repository.existsByUsername(dto.getUsername())) // ← change ici
            throw new RuntimeException("Admin already exists");
        Utilisateur entity = mapper.toEntity(dto);
        entity.setUsername(dto.getUsername());
        entity.setNom(dto.getNom());
        entity.setPrenom(dto.getPrenom());
        entity.setNumTel(dto.getNumTel());

        String tempPassword = UUID.randomUUID().toString();
        entity.setPassword(passwordEncoder.encode(tempPassword));

        entity.setRole(Role.ADMIN);
        entity.setActif(true);
        String token = UUID.randomUUID().toString();
        entity.setActivationToken(token);
        entity.setTokenExpiryDate(LocalDateTime.now().plusHours(24));
        Utilisateur saved = repository.save(entity);
        String fullName = saved.getPrenom() + " " + saved.getNom();
        try {
            emailService.sendAccountCreationEmail(saved.getUsername(), token, fullName);
            log.info("Email de création envoyé à {}", saved.getUsername());
        } catch (Exception e) {
            log.error(" Erreur envoi email à {}: {}", saved.getUsername(), e.getMessage());
        }
        return mapper.toDTO(saved);
    }

    @Override
    public UserDTO getByIdAdmin(Long id) {
        Utilisateur entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur not found with id " + id));
        if (entity.getRole() != Role.ADMIN) {
            throw new RuntimeException("Seul un Admin peut être récupéré");
        }
        return mapper.toDTO(entity);
    }

    @Override
    public List<UserDTO> getAllAdmin() {
        return repository.findAll().stream()
                .filter(u -> u.getRole() == Role.ADMIN)
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO updateAdmin(Long id, UserDTO dto) {
        Utilisateur entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur not found with id " + id));

        if (entity.getRole() != Role.ADMIN) {
            throw new RuntimeException("Seul un Admin peut être modifié");
        }
        entity.setUsername(dto.getUsername());
        entity.setNom(dto.getNom());
        entity.setPrenom(dto.getPrenom());
        entity.setNumTel(dto.getNumTel());
        return mapper.toDTO(repository.save(entity));
    }

    @Override
    public void deleteAdmin(Long id) {
        Utilisateur entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur not found with id " + id));
        if (entity.getRole() != Role.ADMIN) {
            throw new RuntimeException("Seul un Admin peut être supprimé");
        }
        repository.deleteById(id);
    }
    @Override
    public String uploadImage(Long adminId, MultipartFile file) {
        log.info("Saving picture for user ID: {}", adminId);
        Utilisateur admin= repository.findById(adminId)
                .orElseThrow(()->
                        new ResourceNotFoundException("Admin is not exist"+adminId));
        String imageUrl = imageFunction.apply(String.valueOf(adminId), file);
        admin.setAvatar(imageUrl);
        repository.save(admin);
        return imageUrl;
    }

    private final Function<String, String> fileExtension = filename -> Optional.of(filename).filter(name -> name.contains("."))
            .map(name -> "." + name.substring(filename.lastIndexOf(".") + 1)).orElse(".jpg");

    private final BiFunction<String, MultipartFile, String> imageFunction = (id, image) -> {
        String filename = id + fileExtension.apply(image.getOriginalFilename());
        try {
            Path fileStorageLocation = Paths.get(IMAGE_DIRECTORY).toAbsolutePath().normalize();
            if(!Files.exists(fileStorageLocation)) { Files.createDirectories(fileStorageLocation); }
            Files.copy(image.getInputStream(), fileStorageLocation.resolve(filename), REPLACE_EXISTING);
            return filename;
        }catch (Exception exception) {
            throw new RuntimeException("Unable to save image");
        }
    };
}
