package com.example.auth_service.mapper;

import com.example.auth_service.dto.UserDTO;
import com.example.auth_service.model.Utilisateur;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-12-30T21:24:05+0100",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.9 (Microsoft)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserDTO toDTO(Utilisateur user) {
        if ( user == null ) {
            return null;
        }

        UserDTO userDTO = new UserDTO();

        userDTO.setAvatar( user.getAvatar() );
        userDTO.setId( user.getId() );
        userDTO.setUsername( user.getUsername() );
        userDTO.setPassword( user.getPassword() );
        userDTO.setRole( user.getRole() );
        userDTO.setNom( user.getNom() );
        userDTO.setPrenom( user.getPrenom() );
        userDTO.setNumTel( user.getNumTel() );
        userDTO.setSignature( user.getSignature() );
        userDTO.setActivationToken( user.getActivationToken() );
        userDTO.setTokenExpiryDate( user.getTokenExpiryDate() );
        userDTO.setActif( user.isActif() );
        userDTO.setCabinetId( user.getCabinetId() );

        return userDTO;
    }

    @Override
    public Utilisateur toEntity(UserDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Utilisateur.UtilisateurBuilder utilisateur = Utilisateur.builder();

        utilisateur.id( dto.getId() );
        utilisateur.avatar( dto.getAvatar() );
        utilisateur.username( dto.getUsername() );
        utilisateur.role( dto.getRole() );
        utilisateur.nom( dto.getNom() );
        utilisateur.prenom( dto.getPrenom() );
        utilisateur.numTel( dto.getNumTel() );
        utilisateur.signature( dto.getSignature() );
        utilisateur.activationToken( dto.getActivationToken() );
        utilisateur.tokenExpiryDate( dto.getTokenExpiryDate() );
        utilisateur.actif( dto.isActif() );
        utilisateur.cabinetId( dto.getCabinetId() );

        return utilisateur.build();
    }
}
