package com.example.auth_service.controller;

import com.example.auth_service.dto.UserDTO;
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
@RequestMapping("/api/auth")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    @PreAuthorize("hasRole('SUPERADMIN')")
    public UserDTO createAdmin(@RequestBody UserDTO dto) {
        return userService.createAdmin(dto);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('SUPERADMIN')")
    public UserDTO getByIdAdmin(@PathVariable Long id) {
        return userService.getByIdAdmin(id);
    }

    @GetMapping
    @PreAuthorize("hasRole('SUPERADMIN')")
    public List<UserDTO> getAllAdmins() {
        return userService.getAllAdmin();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('SUPERADMIN')")
    public UserDTO updateAdmin(@PathVariable Long id, @RequestBody UserDTO dto) {
        return userService.updateAdmin(id, dto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SUPERADMIN')")
    public void deleteAdmin(@PathVariable Long id) {
        userService.deleteAdmin(id);
    }
    @PreAuthorize("hasRole('SUPERADMIN')")
    @PutMapping("/image")
    public ResponseEntity<String> uploadImage(@RequestParam("id") Long id, @RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok().body(userService.uploadImage(id, file));
    }

    @GetMapping(path = "/image/{filename}", produces = { IMAGE_PNG_VALUE, IMAGE_JPEG_VALUE })
    public byte[] getImage(@PathVariable("filename") String filename) throws IOException {
        return Files.readAllBytes(Paths.get(IMAGE_DIRECTORY + filename));
    }

}
