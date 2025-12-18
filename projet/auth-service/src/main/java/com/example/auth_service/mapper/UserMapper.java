package com.example.auth_service.mapper;

import com.example.auth_service.dto.UserDTO;
import com.example.auth_service.model.Utilisateur;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
    @author sawssan
**/
@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDTO toDTO(Utilisateur user);

    @Mapping(target = "password", ignore = true)
    Utilisateur toEntity(UserDTO dto);
}

