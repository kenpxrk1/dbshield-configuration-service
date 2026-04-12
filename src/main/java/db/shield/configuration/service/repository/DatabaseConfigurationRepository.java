package db.shield.configuration.service.repository;


import db.shield.configuration.service.model.DatabaseConfigurationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;


public interface DatabaseConfigurationRepository extends JpaRepository<DatabaseConfigurationEntity, Long> {

    boolean existsByExternalId(UUID externalId);
    Optional<DatabaseConfigurationEntity> findDatabaseConfigurationEntityByExternalId(UUID externalId);
    void deleteByExternalId(UUID externalId);
}
