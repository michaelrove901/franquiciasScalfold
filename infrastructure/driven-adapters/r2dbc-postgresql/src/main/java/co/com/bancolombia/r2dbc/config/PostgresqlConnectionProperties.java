package co.com.bancolombia.r2dbc.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "spring.r2dbc")
public class PostgresqlConnectionProperties {
    private String host;
    private Integer port;
    private String database;
    private String username;
    private String password;
    private String schema;
}
