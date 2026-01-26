package com.pete.bibliogere.config;

import com.zaxxer.hikari.HikariDataSource;
import net.ttddyy.dsproxy.listener.logging.SLF4JLogLevel;
import net.ttddyy.dsproxy.support.ProxyDataSourceBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.net.URI;
import java.net.URISyntaxException;

@Configuration
public class DataSourceProxyConfig {

    @Value("${spring.datasource.url:}")
    private String datasourceUrl;

    @Value("${spring.datasource.username:}")
    private String datasourceUsername;

    @Value("${spring.datasource.password:}")
    private String datasourcePassword;

    @Bean
    @Primary
    public DataSource dataSource() {
        String railwayUrl = System.getenv("DATABASE_URL");

        HikariDataSource hikariDataSource;

        // If DATABASE_URL exists (Railway), parse it
        if (railwayUrl != null && !railwayUrl.isEmpty()) {
            try {
                hikariDataSource = createRailwayDataSource(railwayUrl);
            } catch (URISyntaxException e) {
                throw new IllegalStateException("Invalid DATABASE_URL format", e);
            }
        } else {
            // Otherwise use application.properties
            hikariDataSource = DataSourceBuilder
                    .create()
                    .type(HikariDataSource.class)
                    .url(datasourceUrl)
                    .username(datasourceUsername)
                    .password(datasourcePassword)
                    .build();
        }

        // Configure HikariCP
        hikariDataSource.setMaximumPoolSize(10);
        hikariDataSource.setConnectionTimeout(30000);
        hikariDataSource.setIdleTimeout(600000);
        hikariDataSource.setMaxLifetime(1800000);
        hikariDataSource.setConnectionTestQuery("SELECT 1");

        // Wrap with DataSource Proxy
        return ProxyDataSourceBuilder
                .create(hikariDataSource)
                .name("bibliogere-proxy")
                .logQueryBySlf4j(SLF4JLogLevel.INFO)
                .multiline()
                .build();
    }

    private HikariDataSource createRailwayDataSource(String databaseUrl) throws URISyntaxException {
        URI dbUri = new URI(databaseUrl);

        String username = dbUri.getUserInfo().split(":")[0];
        String password = dbUri.getUserInfo().split(":")[1];
        String jdbcUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();

        if (dbUri.getQuery() != null) {
            jdbcUrl += "?" + dbUri.getQuery();
        }

        return DataSourceBuilder
                .create()
                .type(HikariDataSource.class)
                .url(jdbcUrl)
                .username(username)
                .password(password)
                .build();
    }
}