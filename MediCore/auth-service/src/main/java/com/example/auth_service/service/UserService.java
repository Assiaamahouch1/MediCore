package com.example.auth_service.service;

import com.example.auth_service.dto.UserDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author sawssan
 **/
public interface UserService {
    UserDTO createAdmin(UserDTO dto);
    UserDTO getByIdAdmin(Long id);
    List<UserDTO> getAllAdmin();
    UserDTO updateAdmin(Long id, UserDTO dto);
    void deleteAdmin(Long id);
    String uploadImage(Long adminId, MultipartFile file);
}
