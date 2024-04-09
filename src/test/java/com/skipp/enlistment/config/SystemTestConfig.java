package com.skipp.enlistment.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;

import javax.sql.DataSource;

@Configuration
@Profile("dev")
public class SystemTestConfig {

    @Bean
    @ConfigurationProperties("spring.datasource")
    public DataSource dataSource(){
        return DataSourceBuilder.create().build();
    }
}
