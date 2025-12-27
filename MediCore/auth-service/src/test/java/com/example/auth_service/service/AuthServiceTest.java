package com.example.auth_service.service;

import com.example.auth_service.config.JwtUtils;
import com.example.auth_service.dto.RegisterRequest;
import com.example.auth_service.dto.LoginRequest;
import com.example.auth_service.model.Role;
import com.example.auth_service.model.Utilisateur;
import com.example.auth_service.repository.UtilisateurRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UtilisateurRepository utilisateurRepository;

    @Mock
    private JwtUtils jwtUtils;

    @InjectMocks
    private AuthService authService;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private RegisterRequest registerRequest;
    private Utilisateur utilisateur;

    @BeforeEach
    void setUp() {
        registerRequest = new RegisterRequest();
        registerRequest.setUsername("doctor1");
        registerRequest.setPassword("password123");
        registerRequest.setNom("Dupont");
        registerRequest.setPrenom("Jean");
        registerRequest.setNumTel("0601020304");
        registerRequest.setRole(Role.MEDECIN);
        registerRequest.setCabinetId(1L);

        utilisateur = Utilisateur.builder()
                .username("doctor1")
                .password(passwordEncoder.encode("password123"))
                .nom("Dupont")
                .prenom("Jean")
                .role(Role.MEDECIN)
                .cabinetId(1L)
                .build();
    }

    @Test
    void register_Success() {
        // Given
        when(utilisateurRepository.existsByUsername("doctor1")).thenReturn(false);
        when(utilisateurRepository.save(any(Utilisateur.class))).thenReturn(utilisateur);

        // When
        String result = authService.register(registerRequest);

        // Then
        assertEquals("User registered successfully", result);
        verify(utilisateurRepository).save(any(Utilisateur.class));
    }

    @Test
    void register_UsernameAlreadyExists_ThrowsException() {
        // Given
        when(utilisateurRepository.existsByUsername("doctor1")).thenReturn(true);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authService.register(registerRequest);
        });
        assertEquals("Login already exists", exception.getMessage());
    }

    @Test
    void register_MedecinWithoutCabinetId_ThrowsException() {
        // Given
        registerRequest.setCabinetId(null); // cabinetId manquant

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authService.register(registerRequest);
        });
        assertEquals("cabinetId obligatoire pour MEDECIN/SECRETAIRE", exception.getMessage());
    }

    @Test
    void login_Success() {
        // Given
        Utilisateur savedUser = Utilisateur.builder()
                .username("doctor1")
                .password(passwordEncoder.encode("password123"))
                .role(Role.MEDECIN)
                .build();

        when(utilisateurRepository.findByUsername("doctor1")).thenReturn(Optional.of(savedUser));
        when(jwtUtils.generateToken(savedUser)).thenReturn("fake-jwt-token");

        LoginRequest loginRequest = new LoginRequest("doctor1", "password123");

        // When
        String token = authService.login(loginRequest);

        // Then
        assertEquals("fake-jwt-token", token);
    }

    @Test
    void login_InvalidPassword_ThrowsException() {
        // Given
        Utilisateur savedUser = Utilisateur.builder()
                .username("doctor1")
                .password(passwordEncoder.encode("wrongpassword"))
                .build();

        when(utilisateurRepository.findByUsername("doctor1")).thenReturn(Optional.of(savedUser));

        LoginRequest loginRequest = new LoginRequest("doctor1", "password123");

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authService.login(loginRequest);
        });
        assertEquals("Invalid password", exception.getMessage());
    }
}