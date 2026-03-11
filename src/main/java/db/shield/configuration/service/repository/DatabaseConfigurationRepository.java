package db.shield.configuration.service.repository;


import db.shield.configuration.service.model.DatabaseConfigurationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;


public interface DatabaseConfigurationRepository extends JpaRepository<DatabaseConfigurationEntity, UUID> {
}
