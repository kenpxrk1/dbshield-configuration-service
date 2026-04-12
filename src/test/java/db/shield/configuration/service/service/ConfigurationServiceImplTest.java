package db.shield.configuration.service.service;


import db.shield.configuration.service.Initializer;
import db.shield.configuration.service.exception.EntityNotFoundException;
import db.shield.configuration.service.mapper.DatabaseConfigurationMapper;
import db.shield.configuration.service.mapper.DatabaseConfigurationMapperImpl;
import db.shield.configuration.service.model.DatabaseConfigurationEntity;
import db.shield.configuration.service.repository.DatabaseConfigurationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class ConfigurationServiceImplTest extends Initializer {

    @Mock
    private DatabaseConfigurationRepository configurationRepository;
    @Spy
    private DatabaseConfigurationMapper mapper = new DatabaseConfigurationMapperImpl();
    @InjectMocks
    private ConfigurationServiceImpl service;

    @Test
    void create_shouldSaveConfiguration() {
        when(configurationRepository.save(any(DatabaseConfigurationEntity.class)))
                .thenReturn(configurationEntity);

        var response = service.create(configurationCreateRequest);

        verify(configurationRepository).save(any(DatabaseConfigurationEntity.class));
        assertThat(response.name()).isEqualTo(configurationCreateRequest.name());
    }

    @Test
    void update_shouldUpdateAndReturnResponse() {
        when(configurationRepository.findDatabaseConfigurationEntityByExternalId(configurationEntity.getExternalId()))
                .thenReturn(Optional.of(configurationEntity));

        var response = service.update(configurationEntity.getExternalId(), configurationUpdateRequest);

        assertThat(response.host()).isEqualTo(configurationUpdateRequest.host());
        assertThat(response.port()).isEqualTo(configurationUpdateRequest.port());
    }

    @Test
    void update_shouldThrowIfNotFound() {
        UUID id = UUID.randomUUID();
        when(configurationRepository.findDatabaseConfigurationEntityByExternalId(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> service.update(id, configurationUpdateRequest));
    }

    @Test
    void delete_shouldDeleteConfiguration() {
        UUID id = configurationEntity.getExternalId();
        when(configurationRepository.existsByExternalId(id)).thenReturn(true);

        service.delete(id);

        verify(configurationRepository).deleteByExternalId(id);
    }

    @Test
    void delete_shouldThrowIfNotFound() {
        UUID id = UUID.randomUUID();
        when(configurationRepository.existsByExternalId(id)).thenReturn(false);

        assertThrows(EntityNotFoundException.class,
                () -> service.delete(id));
    }

    @Test
    void getById_shouldReturnConfiguration() {
        UUID id = configurationEntity.getExternalId();
        when(configurationRepository.findDatabaseConfigurationEntityByExternalId(id)).thenReturn(Optional.of(configurationEntity));

        var response = service.getById(id);

        assertThat(response.externalId()).isEqualTo(id);
    }

    @Test
    void getById_shouldThrowIfNotFound() {
        UUID id = UUID.randomUUID();
        when(configurationRepository.findDatabaseConfigurationEntityByExternalId(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> service.getById(id));
    }

    @Test
    void getAll_shouldReturnAllConfigurations() {
        when(configurationRepository.findAll()).thenReturn(List.of(configurationEntity));

        var responses = service.getAll();

        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).externalId()).isEqualTo(configurationEntity.getExternalId());
    }

    @Test
    void enable_shouldSetEnabledTrue() {
        UUID id = configurationEntity.getExternalId();
        when(configurationRepository.findDatabaseConfigurationEntityByExternalId(id)).thenReturn(Optional.of(configurationEntity));

        service.enable(id);

        assertThat(configurationEntity.isEnabled()).isTrue();
    }

    @Test
    void disable_shouldSetEnabledFalse() {
        UUID id = configurationEntity.getExternalId();
        when(configurationRepository.findDatabaseConfigurationEntityByExternalId(id)).thenReturn(Optional.of(configurationEntity));

        service.disable(id);

        assertThat(configurationEntity.isEnabled()).isFalse();
    }
}
