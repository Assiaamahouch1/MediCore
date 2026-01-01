package com.example.auth_service.mapper;

import com.example.auth_service.dto.UserDTO;
import com.example.auth_service.model.Utilisateur;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-01-01T00:32:54+0100",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.44.0.v20251118-1623, environment: Java 21.0.9 (Eclipse Adoptium)"
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
        userDTO.setActif( user.isActif() );
        userDTO.setActivationToken( user.getActivationToken() );
        userDTO.setCabinetId( user.getCabinetId() );
        userDTO.setId( user.getId() );
        userDTO.setNom( user.getNom() );
        userDTO.setNumTel( user.getNumTel() );
        userDTO.setPassword( user.getPassword() );
        userDTO.setPrenom( user.getPrenom() );
        userDTO.setRole( user.getRole() );
        userDTO.setSignature( user.getSignature() );
        userDTO.setTokenExpiryDate( user.getTokenExpiryDate() );
        userDTO.setUsername( user.getUsername() );

        return userDTO;
    }

    @Override
    public Utilisateur toEntity(UserDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Utilisateur.UtilisateurBuilder utilisateur = Utilisateur.builder();

        utilisateur.actif( dto.isActif() );
        utilisateur.activationToken( dto.getActivationToken() );
        utilisateur.avatar( dto.getAvatar() );
        utilisateur.cabinetId( dto.getCabinetId() );
        utilisateur.id( dto.getId() );
        utilisateur.nom( dto.getNom() );
        utilisateur.numTel( dto.getNumTel() );
        utilisateur.prenom( dto.getPrenom() );
        utilisateur.role( dto.getRole() );
        utilisateur.signature( dto.getSignature() );
        utilisateur.tokenExpiryDate( dto.getTokenExpiryDate() );
        utilisateur.username( dto.getUsername() );

        return utilisateur.build();
    }
}
