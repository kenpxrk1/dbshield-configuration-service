package db.shield.configuration.service.controller;


import db.shield.configuration.service.dto.DatabaseConfigurationCreateRequest;
import db.shield.configuration.service.dto.DatabaseConfigurationResponse;
import db.shield.configuration.service.dto.DatabaseConfigurationUpdateRequest;
import db.shield.configuration.service.service.ConfigurationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/configs")
@RequiredArgsConstructor
@Tag(name = "Database Configuration Controller", description = "Operations to manage database configurations")
public class DatabaseConfigurationController {

    private final ConfigurationService service;

    @Operation(summary = "Create new database configuration",
            description = "Creates a new database configuration entry")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Configuration successfully created"),
            @ApiResponse(responseCode = "400", description = "Invalid request body")
    })
    @PostMapping
    public ResponseEntity<DatabaseConfigurationResponse> create(@RequestBody @Valid DatabaseConfigurationCreateRequest request) {
        DatabaseConfigurationResponse response = service.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Update database configuration",
            description = "Updates an existing database configuration by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Configuration successfully updated"),
            @ApiResponse(responseCode = "404", description = "Configuration not found"),
            @ApiResponse(responseCode = "400", description = "Invalid request body")
    })
    @PutMapping("/{id}")
    public ResponseEntity<DatabaseConfigurationResponse> update(
            @PathVariable UUID id,
            @RequestBody @Valid DatabaseConfigurationUpdateRequest request) {
        DatabaseConfigurationResponse response = service.update(id, request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete database configuration",
            description = "Deletes a database configuration by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Configuration successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Configuration not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

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

    @Operation(summary = "Get all database configurations",
            description = "Returns all database configurations")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of configurations")
    })
    @GetMapping
    public ResponseEntity<List<DatabaseConfigurationResponse>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @Operation(summary = "Enable database configuration",
            description = "Enables a database configuration by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Configuration enabled"),
            @ApiResponse(responseCode = "404", description = "Configuration not found")
    })
    @PostMapping("/{id}/enable")
    public ResponseEntity<Void> enable(@PathVariable UUID id) {
        service.enable(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Disable database configuration",
            description = "Disables a database configuration by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Configuration disabled"),
            @ApiResponse(responseCode = "404", description = "Configuration not found")
    })
    @PostMapping("/{id}/disable")
    public ResponseEntity<Void> disable(@PathVariable UUID id) {
        service.disable(id);
        return ResponseEntity.noContent().build();
    }
}
