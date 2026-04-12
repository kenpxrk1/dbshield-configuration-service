package db.shield.configuration.service.mapper;


import db.shield.configuration.service.Initializer;
import db.shield.configuration.service.dto.DatabaseConfigurationResponse;
import db.shield.configuration.service.model.DatabaseConfigurationEntity;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


class DatabaseConfigurationMapperTest extends Initializer {

    private final DatabaseConfigurationMapper mapper = new DatabaseConfigurationMapperImpl();

    @Test
    void shouldMapCreateRequestToEntity() {
        DatabaseConfigurationEntity entity =
                mapper.toEntity(configurationCreateRequest);

        assertThat(entity).isNotNull();
        assertThat(entity.getId()).isNull();
        assertThat(entity.getName()).isEqualTo(configurationCreateRequest.name());
        assertThat(entity.getDbType()).isEqualTo(configurationCreateRequest.dbType());
        assertThat(entity.getEnvironment()).isEqualTo(configurationCreateRequest.environment());
        assertThat(entity.getHost()).isEqualTo(configurationCreateRequest.host());
        assertThat(entity.getPort()).isEqualTo(configurationCreateRequest.port());
        assertThat(entity.getDatabaseName()).isEqualTo(configurationCreateRequest.databaseName());
        assertThat(entity.getUsername()).isEqualTo(configurationCreateRequest.username());
        assertThat(entity.isEnabled()).isTrue();
        assertThat(entity.getEncryptedPassword()).isNull();
    }

    @Test
    void shouldMapEntityToResponse() {
        DatabaseConfigurationResponse response =
                mapper.toResponse(configurationEntity);

        assertThat(response).isNotNull();
        assertThat(response.externalId()).isEqualTo(configurationEntity.getExternalId());
        assertThat(response.name()).isEqualTo(configurationEntity.getName());
        assertThat(response.dbType()).isEqualTo(configurationEntity.getDbType());
        assertThat(response.environment()).isEqualTo(configurationEntity.getEnvironment());
        assertThat(response.host()).isEqualTo(configurationEntity.getHost());
        assertThat(response.port()).isEqualTo(configurationEntity.getPort());
        assertThat(response.databaseName()).isEqualTo(configurationEntity.getDatabaseName());
        assertThat(response.username()).isEqualTo(configurationEntity.getUsername());
        assertThat(response.enabled()).isEqualTo(configurationEntity.isEnabled());
        assertThat(response.createdAt()).isEqualTo(configurationEntity.getCreatedAt());
        assertThat(response.updatedAt()).isEqualTo(configurationEntity.getUpdatedAt());
    }

    @Test
    void shouldUpdateEntityFromUpdateRequest() {
        mapper.updateEntityFromRequest(configurationUpdateRequest, configurationEntity);

        assertThat(configurationEntity.getHost()).isEqualTo(configurationUpdateRequest.host());
        assertThat(configurationEntity.getPort()).isEqualTo(configurationUpdateRequest.port());
        assertThat(configurationEntity.getDatabaseName()).isEqualTo(configurationUpdateRequest.databaseName());
        assertThat(configurationEntity.getUsername()).isEqualTo(configurationUpdateRequest.username());

        assertThat(configurationEntity.getName()).isEqualTo("main-db");
        assertThat(configurationEntity.getDbType()).isNotNull();
        assertThat(configurationEntity.getEnvironment()).isNotNull();
        assertThat(configurationEntity.isEnabled()).isTrue();
    }

    @Test
    void shouldMapEntityListToResponseList() {
        List<DatabaseConfigurationResponse> responses =
                mapper.toResponseList(List.of(configurationEntity));

        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).externalId())
                .isEqualTo(configurationEntity.getExternalId());
    }
}
