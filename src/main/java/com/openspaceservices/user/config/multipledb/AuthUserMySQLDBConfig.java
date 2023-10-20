package com.openspaceservices.user.config.multipledb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.amazonaws.auth.AWSCredentials;
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
        entityManagerFactoryRef = "authUserEntityManagerFactory",
        transactionManagerRef = "authuserTransactionManager",
        basePackages = "com.openspaceservices.user.apigateway.repository")
public class AuthUserMySQLDBConfig {

    @Autowired
    AWSCredentialsManagerJavaConfig config;

    @Bean
    @ConfigurationProperties(prefix = "spring.authuser.datasource")
    public DataSourceProperties authUserDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    public DataSource authUserDataSource(@Qualifier("authUserDataSourceProperties") DataSourceProperties dataSourceProperties) {
        return dataSourceProperties.initializeDataSourceBuilder()
//                .username(config.getSecretValue().getUsername())
//                .password(config.getSecretValue().getPassword())
                .username("root")
                .password("OpenSpace")
                .build();
    }

    @Bean(name = "authUserEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean authUserEntityManagerFactory(
            @Qualifier("authUserDataSource") DataSource sqlServerDataSource,
            EntityManagerFactoryBuilder builder) {

        return builder.dataSource(sqlServerDataSource)
                .packages("com.openspaceservices.user.apigateway.model")
                .persistenceUnit("optimeyesai_auth_db")
                .build();
    }

    @Bean
    public PlatformTransactionManager authuserTransactionManager(
            @Qualifier("authUserEntityManagerFactory") EntityManagerFactory factory) {
        return new JpaTransactionManager(factory);
    }
}
