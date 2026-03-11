package db.shield.configuration.service;


import db.shield.configuration.service.dto.DatabaseConfigurationCreateRequest;
import db.shield.configuration.service.dto.DatabaseConfigurationUpdateRequest;
import db.shield.configuration.service.model.DatabaseConfigurationEntity;
import db.shield.configuration.service.model.constant.DatabaseType;
import db.shield.configuration.service.model.constant.EnvironmentType;
import org.junit.jupiter.api.BeforeAll;

import java.time.Instant;
import java.util.UUID;


public abstract class Initializer {

    protected static DatabaseConfigurationCreateRequest configurationCreateRequest;
    protected static DatabaseConfigurationUpdateRequest configurationUpdateRequest;
    protected static DatabaseConfigurationEntity configurationEntity;

    @BeforeAll
    static void init() {

        configurationCreateRequest =
                new DatabaseConfigurationCreateRequest(
                        "main-db",
                        DatabaseType.POSTGRES,
                        EnvironmentType.PROD,
                        "localhost",
                        5432,
                        "shield",
                        "admin",
                        "raw-password"
                );

        configurationUpdateRequest =
                new DatabaseConfigurationUpdateRequest(
                        "127.0.0.1",
                        5433,
                        "shield_new",
                        "new_admin",
                        "new-password"
                );

        configurationEntity = new DatabaseConfigurationEntity();
        configurationEntity.setId(UUID.randomUUID());
        configurationEntity.setName("main-db");
        configurationEntity.setDbType(DatabaseType.POSTGRES);
        configurationEntity.setEnvironment(EnvironmentType.PROD);
        configurationEntity.setHost("localhost");
        configurationEntity.setPort(5432);
        configurationEntity.setDatabaseName("shield");
        configurationEntity.setUsername("admin");
        configurationEntity.setEncryptedPassword("encrypted");
        configurationEntity.setEnabled(true);
        configurationEntity.setCreatedAt(Instant.now());
        configurationEntity.setUpdatedAt(Instant.now());
    }
}