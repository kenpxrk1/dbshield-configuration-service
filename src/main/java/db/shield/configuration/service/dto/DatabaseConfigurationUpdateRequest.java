package db.shield.configuration.service.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;


@Schema(description = "Request for updating an existing database configuration")
public record DatabaseConfigurationUpdateRequest(

        @NotBlank
        @Schema(description = "Database host", example = "db.example.com")
        String host,

        @Min(1)
        @Max(65535)
        @Schema(description = "Database port", example = "5432")
        int port,

        @NotBlank
        @Schema(description = "Database name", example = "my_database")
        String databaseName,

        @NotBlank
        @Schema(description = "Database username", example = "dbuser")
        String username,

        @Schema(description = "Password for database connection", example = "secret", required = false)
        String password
) {}
