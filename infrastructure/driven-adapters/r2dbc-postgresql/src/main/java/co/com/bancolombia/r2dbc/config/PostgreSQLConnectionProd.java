package co.com.bancolombia.r2dbc.config;

import io.r2dbc.spi.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.r2dbc.ConnectionFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@Configuration
@Profile("prod")
@EnableR2dbcRepositories(
        basePackages = "co.com.bancolombia.r2dbc.repository",
        entityOperationsRef = "r2dbcEntityTemplateFranchise"
)
public class PostgreSQLConnectionProd {

    private static final Logger logger = LoggerFactory.getLogger(PostgreSQLConnectionProd.class);

    private static final String HOST = "franchise-instance-1.cyna20giicbd.us-east-1.rds.amazonaws.com";
    private static final int PORT = 5432;
    private static final String DATABASE = "franchise";
    private static final String USER = "postgres";
    private static final String PASSWORD = "Wv6D3Ft5wk1tNFq2YHqL";
    private static final String SCHEMA = "public";

    @Bean
    @Qualifier("rds-franchise")
    public ConnectionFactory connectionFactoryProd() {
        String url = String.format(
                "r2dbc:postgresql://%s:%d/%s?schema=%s&sslMode=require",
                HOST, PORT, DATABASE, SCHEMA
        );
        logger.info("🔗 Connecting to Aurora PostgreSQL RDS: {}", url);

        return ConnectionFactoryBuilder
                .withUrl(url)
                .username(USER)
                .password(PASSWORD)
                .build();
    }

    @Bean
    public R2dbcEntityTemplate r2dbcEntityTemplateFranchise(
            @Qualifier("rds-franchise") ConnectionFactory connectionFactory) {
        return new R2dbcEntityTemplate(connectionFactory);
    }
}
