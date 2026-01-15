package com.example.cabinetservice.mapper;

import com.example.cabinetservice.dto.*;
import com.example.cabinetservice.model.Cabinet;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface CabinetMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "service_actif", constant = "false")
    @Mapping(target = "date_expiration_service", ignore = true)
    Cabinet toEntity(CabinetCreateRequest req);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "service_actif", ignore = true)
    @Mapping(target = "date_expiration_service", ignore = true)
    void updateEntity(CabinetUpdateRequest req, @MappingTarget Cabinet cabinet);

    CabinetResponse toResponse(Cabinet cabinet);
}
