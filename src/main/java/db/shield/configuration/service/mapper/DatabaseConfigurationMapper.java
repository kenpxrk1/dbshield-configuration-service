package db.shield.configuration.service.mapper;


import db.shield.configuration.service.dto.DatabaseConfigurationCreateRequest;
import db.shield.configuration.service.dto.DatabaseConfigurationResponse;
import db.shield.configuration.service.dto.DatabaseConfigurationUpdateRequest;
import db.shield.configuration.service.model.DatabaseConfigurationEntity;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;


@Mapper(componentModel = "spring")
public interface DatabaseConfigurationMapper {

    DatabaseConfigurationResponse toResponse(DatabaseConfigurationEntity entity);

    List<DatabaseConfigurationResponse> toResponseList(List<DatabaseConfigurationEntity> entities);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "externalId", ignore = true)
    @Mapping(target = "encryptedPassword", ignore = true)
    @Mapping(target = "enabled", constant = "true")
    DatabaseConfigurationEntity toEntity(DatabaseConfigurationCreateRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "externalId", ignore = true)
    @Mapping(target = "name", ignore = true)
    @Mapping(target = "dbType", ignore = true)
    @Mapping(target = "environment", ignore = true)
    @Mapping(target = "encryptedPassword", ignore = true)
    @Mapping(target = "enabled", ignore = true)
    void updateEntityFromRequest(
            DatabaseConfigurationUpdateRequest request,
            @MappingTarget DatabaseConfigurationEntity entity
    );
}
