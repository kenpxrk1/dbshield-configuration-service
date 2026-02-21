package db.shield.configuration.service.service;


import db.shield.configuration.service.dto.DatabaseConfigurationCreateRequest;
import db.shield.configuration.service.dto.DatabaseConfigurationResponse;
import db.shield.configuration.service.dto.DatabaseConfigurationUpdateRequest;
import db.shield.configuration.service.exception.EntityNotFoundException;
import db.shield.configuration.service.mapper.DatabaseConfigurationMapper;
import db.shield.configuration.service.model.DatabaseConfigurationEntity;
import db.shield.configuration.service.repository.DatabaseConfigurationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;


@Slf4j
@Service
@RequiredArgsConstructor
public class ConfigurationServiceImpl implements ConfigurationService {

    private final DatabaseConfigurationRepository configurationRepository;
    private final DatabaseConfigurationMapper mapper;

    @Override
    @Transactional
    public DatabaseConfigurationResponse create(DatabaseConfigurationCreateRequest request) {
        log.info("Creating database configuration: name={}, env={}",
                request.name(), request.environment());

        DatabaseConfigurationEntity entity = mapper.toEntity(request);

        entity.setEncryptedPassword(request.password());

        DatabaseConfigurationEntity saved = configurationRepository.save(entity);

        return mapper.toResponse(saved);
    }

    @Override
    @Transactional
    public DatabaseConfigurationResponse update(UUID id, DatabaseConfigurationUpdateRequest request) {

        DatabaseConfigurationEntity entity = getEntityOrThrow(id);

        mapper.updateEntityFromRequest(request, entity);

        if (request.password() != null) {
            entity.setEncryptedPassword(request.password());
        }

        return mapper.toResponse(entity);
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        log.info("Deleting configuration id={}", id);

        if (!configurationRepository.existsById(id)) {
            throw new EntityNotFoundException("Configuration not found: " + id);
        }

        configurationRepository.deleteById(id);
    }

    @Override
    public DatabaseConfigurationResponse getById(UUID id) {
        return mapper.toResponse(getEntityOrThrow(id));
    }

    @Override
    public List<DatabaseConfigurationResponse> getAll() {
        return mapper.toResponseList(configurationRepository.findAll());
    }

    @Override
    @Transactional
    public void enable(UUID id) {
        DatabaseConfigurationEntity entity = getEntityOrThrow(id);
        entity.setEnabled(true);
    }

    @Override
    @Transactional
    public void disable(UUID id) {
        DatabaseConfigurationEntity entity = getEntityOrThrow(id);
        entity.setEnabled(false);
    }

    private DatabaseConfigurationEntity getEntityOrThrow(UUID id) {
        return configurationRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Configuration not found: " + id)
                );
    }
}
