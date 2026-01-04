package com.example.auth_service.controller;

import com.example.auth_service.dto.UserDTO;
import com.example.auth_service.service.SecrService;
import com.example.auth_service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static com.example.auth_service.Constant.Constant.IMAGE_DIRECTORY;
import static org.springframework.util.MimeTypeUtils.IMAGE_JPEG_VALUE;
import static org.springframework.util.MimeTypeUtils.IMAGE_PNG_VALUE;

/**
 * @author sawssan
 **/
@RestController
@RequestMapping("/api/auth/secr")
public class SecrController {

    @Autowired
    private SecrService secrService;

    @PostMapping
    @PreAuthorize("hasRole('MEDECIN')")
    public UserDTO createSecr(@RequestBody UserDTO dto) {
        return secrService.createSecr(dto);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('MEDECIN')")
    public UserDTO getByIdSecr(@PathVariable Long id) {
        return secrService.getByIdSecr(id);
    }

    @GetMapping
    @PreAuthorize("hasRole('MEDECIN')")
    public List<UserDTO> getAllSecrs() {
        return secrService.getAllSecr();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('MEDECIN')")
    public UserDTO updateSecr(@PathVariable Long id, @RequestBody UserDTO dto) {
        return secrService.updateSecr(id, dto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('MEDECIN')")
    public void deleteSecr(@PathVariable Long id) {
        secrService.deleteSecr(id);
    }

    @PreAuthorize("hasRole('MEDECIN')")
    @PutMapping("/image")
    public ResponseEntity<String> uploadImage(@RequestParam("id") Long id, @RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok().body(secrService.uploadImage(id, file));
    }

    @GetMapping(path = "/image/{filename}", produces = { IMAGE_PNG_VALUE, IMAGE_JPEG_VALUE })
    public byte[] getImage(@PathVariable("filename") String filename) throws IOException {
        return Files.readAllBytes(Paths.get(IMAGE_DIRECTORY + filename));
    }

}
