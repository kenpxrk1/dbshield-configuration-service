package db.shield.configuration.service.dto;


import db.shield.configuration.service.model.constant.DatabaseType;
import db.shield.configuration.service.model.constant.EnvironmentType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.UUID;


@Schema(description = "Response with database configuration details")
public record DatabaseConfigurationResponse(

        @Schema(description = "Unique ID of the configuration", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID externalId,

        @Schema(description = "Configuration name", example = "Main DB Config")
        String name,

        @Schema(description = "Database type", example = "POSTGRESQL")
        DatabaseType dbType,

        @Schema(description = "Environment type", example = "PRODUCTION")
        EnvironmentType environment,

        @Schema(description = "Database host", example = "db.example.com")
        String host,

        @Schema(description = "Database port", example = "5432")
        int port,

        @Schema(description = "Database name", example = "my_database")
        String databaseName,

        @Schema(description = "Database username", example = "dbuser")
        String username,

        @Schema(description = "Database password", example = "dbpassword")
        String encryptedPassword,

        @Schema(description = "Indicates whether the configuration is enabled", example = "true")
        boolean enabled,

        @Schema(description = "Configuration creation timestamp")
        Instant createdAt,

        @Schema(description = "Configuration last update timestamp")
        Instant updatedAt
) {}
