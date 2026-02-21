package db.shield.configuration.service.service;


import db.shield.configuration.service.dto.DatabaseConfigurationCreateRequest;
import db.shield.configuration.service.dto.DatabaseConfigurationResponse;
import db.shield.configuration.service.dto.DatabaseConfigurationUpdateRequest;

import java.util.List;
import java.util.UUID;


public interface ConfigurationService {

    DatabaseConfigurationResponse create(DatabaseConfigurationCreateRequest request);

    DatabaseConfigurationResponse update(UUID id, DatabaseConfigurationUpdateRequest request);

    void delete(UUID id);

    DatabaseConfigurationResponse getById(UUID id);

    List<DatabaseConfigurationResponse> getAll();

    void enable(UUID id);

    void disable(UUID id);
}
