package com.pete.bibliogere.config;

import com.zaxxer.hikari.HikariDataSource;
import net.ttddyy.dsproxy.listener.logging.SLF4JLogLevel;
import net.ttddyy.dsproxy.support.ProxyDataSourceBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

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
        HikariDataSource hikariDataSource = DataSourceBuilder
                .create()
                .type(HikariDataSource.class)
                .url(datasourceUrl)
                .username(datasourceUsername)
                .password(datasourcePassword)
                .build();

        // Configure HikariCP
        hikariDataSource.setMaximumPoolSize(5);
        hikariDataSource.setMinimumIdle(1);
        hikariDataSource.setConnectionTimeout(30000);
        hikariDataSource.setIdleTimeout(600000);
        hikariDataSource.setMaxLifetime(1800000);
        hikariDataSource.setConnectionTestQuery("SELECT 1");

        return ProxyDataSourceBuilder
                .create(hikariDataSource)
                .name("bibliogere-proxy")
                .logQueryBySlf4j(SLF4JLogLevel.INFO)
                .multiline()
                .build();
    }
}