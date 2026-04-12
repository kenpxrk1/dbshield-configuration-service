package db.shield.configuration.service.model;


import db.shield.configuration.service.model.constant.DatabaseType;
import db.shield.configuration.service.model.constant.EnvironmentType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;


@Entity
@Table(name = "database_configuration")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DatabaseConfigurationEntity extends BaseEntity {

    @Column(nullable = false, length = 100, name = "externalId")
    private UUID externalId;
    @Column(nullable = false, length = 150)
    private String name;
    @Enumerated(EnumType.STRING)
    @Column(name = "db_type", nullable = false, length = 30)
    private DatabaseType dbType;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private EnvironmentType environment;
    @Column(nullable = false, length = 255)
    private String host;
    @Column(nullable = false)
    private int port;
    @Column(name = "database_name", nullable = false, length = 150)
    private String databaseName;
    @Column(nullable = false, length = 150)
    private String username;
    @Column(name = "encrypted_password", nullable = false, length = 512)
    private String encryptedPassword;
    @Column(nullable = false)
    private boolean enabled;

    @PrePersist
    protected void onCreate() {
        if (this.externalId == null) {
            this.externalId = UUID.randomUUID();
        }
    }
}
