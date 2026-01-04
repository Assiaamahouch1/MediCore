package com.example.auth_service.service;

import com.example.auth_service.dto.UserDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author sawssan
 **/
public interface SecrService {
    UserDTO createSecr(UserDTO dto);
    UserDTO getByIdSecr(Long id);
    List<UserDTO> getAllSecr();
    UserDTO updateSecr(Long id, UserDTO dto);
    void deleteSecr(Long id);
    String uploadImage(Long adminId, MultipartFile file);
}
