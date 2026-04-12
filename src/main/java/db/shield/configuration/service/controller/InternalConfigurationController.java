package db.shield.configuration.service.controller;

import db.shield.configuration.service.dto.DatabaseConfigurationResponse;
import db.shield.configuration.service.service.ConfigurationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("api/internal/configs")
@RequiredArgsConstructor
@Tag(name = "Internal Database Configuration Controller", description = "Operations to manage database configurations for internal services")
public class InternalConfigurationController {

    private final ConfigurationService service;


    @Operation(summary = "Get database configuration by ID",
            description = "Returns a single database configuration by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Configuration found"),
            @ApiResponse(responseCode = "404", description = "Configuration not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<DatabaseConfigurationResponse> getById(@PathVariable UUID id) {
        DatabaseConfigurationResponse response = service.getById(id);
        return ResponseEntity.ok(response);
    }
}
