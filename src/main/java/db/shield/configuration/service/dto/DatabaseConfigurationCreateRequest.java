package db.shield.configuration.service.dto;


import db.shield.configuration.service.model.constant.DatabaseType;
import db.shield.configuration.service.model.constant.EnvironmentType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


@Schema(description = "Request for creating a new database configuration")
public record DatabaseConfigurationCreateRequest(

        @NotBlank
        @Size(max = 100)
        @Schema(description = "Configuration name", example = "Main DB Config", required = true)
        String name,

        @NotNull
        @Schema(description = "Database type", example = "POSTGRES, ORACLE (one of them)", required = true)
        DatabaseType dbType,

        @NotNull
        @Schema(description = "Environment type", example = "PROD, DEV, TEST (one of them)", required = true)
        EnvironmentType environment,

        @NotBlank
        @Schema(description = "Database host", example = "db.example.com", required = true)
        String host,

        @Min(1)
        @Max(65535)
        @Schema(description = "Database port", example = "5432", required = true)
        int port,

        @NotBlank
        @Schema(description = "Database name", example = "my_database", required = true)
        String databaseName,

        @NotBlank
        @Schema(description = "Username for database connection", example = "dbuser", required = true)
        String username,

        @NotBlank
        @Schema(description = "Password for database connection", example = "secret", required = true)
        String password
) {}
