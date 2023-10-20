package com.openspaceservices.user.config.multipledb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.openspaceservices.user.config.AWSCredentialsManagerJavaConfig;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

/**
 * @author +IT KHAMBE working in
 * <a href="https://www.openspaceservices.com">Openspace Services Pvt. Ltd.</a>
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "userEntityManagerFactory",
        transactionManagerRef = "userTransactionManager",
        basePackages = {"com.openspaceservices.user.app.repository"})

public class USERMySQLDBConfig {

    @Autowired
    AWSCredentialsManagerJavaConfig config;

    @Primary
    @Bean
    @ConfigurationProperties(prefix = "spring.user.datasource")
    public DataSourceProperties userDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Primary
    @Bean
    public DataSource userDataSource(
            @Qualifier("userDataSourceProperties") DataSourceProperties dataSourceProperties) {
        return dataSourceProperties.initializeDataSourceBuilder()
//                .username(config.getSecretValue().getUsername())
//                .password(config.getSecretValue().getPassword())
                .username("root")
                .password("OpenSpace")
                .build();
    }

    @Primary
    @Bean
    public LocalContainerEntityManagerFactoryBean userEntityManagerFactory(
            @Qualifier("userDataSource") DataSource hubDataSource,
            EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(hubDataSource)
                .packages("com.openspaceservices.user.app.model")
                .persistenceUnit("optimeyesai_user_db")
                .build();
    }

    @Primary
    @Bean
    public PlatformTransactionManager userTransactionManager(
            @Qualifier("userEntityManagerFactory") EntityManagerFactory factory) {
        return new JpaTransactionManager(factory);
    }

}
