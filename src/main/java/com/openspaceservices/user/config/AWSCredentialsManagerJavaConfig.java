package com.openspaceservices.user.config;

import java.sql.Connection;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.SystemPropertiesCredentialsProvider;
import com.amazonaws.services.secretsmanager.AWSSecretsManager;
import com.amazonaws.services.secretsmanager.AWSSecretsManagerClientBuilder;
import com.amazonaws.services.secretsmanager.model.GetSecretValueRequest;
import com.amazonaws.services.secretsmanager.model.GetSecretValueResult;
import com.google.gson.Gson;

@Configuration
public class AWSCredentialsManagerJavaConfig {
    private Gson gson = new Gson();
    @Value("${aws.accessKeyId}")
    private String accessKey;

    @Value("${aws.secretKey}")
    private String secretKey;
    
    @Value("${aws.secretName}")
    private String secretName;

    @Value("${aws.region}")
    private String region;

    @PostConstruct
    public void setSystemProperty(){
     
    }
    /**
     * Customize the data source config values reading from Bean class
     * Instead of reading from application.yaml
     */

    public SecretValue getSecretValue() {
    	   SystemPropertiesCredentialsProvider systemPropertiesCredentialsProvider=new SystemPropertiesCredentialsProvider();
           System.setProperty("aws.accessKeyId",accessKey);
           System.setProperty("aws.secretKey",secretKey);

        // Create a Secrets Manager client
        AWSSecretsManager client = AWSSecretsManagerClientBuilder.standard()
                .withRegion(region)
                .withCredentials(systemPropertiesCredentialsProvider)
                .build();

        String secret;
        GetSecretValueRequest getSecretValueRequest = new GetSecretValueRequest()
                .withSecretId(secretName);
        GetSecretValueResult getSecretValueResult = null;

        try {
            getSecretValueResult = client.getSecretValue(getSecretValueRequest);
        } catch (Exception e) {
            throw e;
        }

        if (getSecretValueResult.getSecretString() != null) {
            secret = getSecretValueResult.getSecretString();
            return gson.fromJson(secret, SecretValue.class);
        }
        return null;
    }
}
