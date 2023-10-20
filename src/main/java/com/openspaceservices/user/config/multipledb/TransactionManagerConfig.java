package com.openspaceservices.user.config.multipledb;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.transaction.ChainedTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * @author +IT KHAMBE working in
 * <a href="https://www.openspaceservices.com">Openspace Services Pvt. Ltd.</a>
 */
@Configuration
public class TransactionManagerConfig {

    @Bean(name = "chainedTransactionManager")
    public ChainedTransactionManager transactionManager(
            @Qualifier("authuserTransactionManager") PlatformTransactionManager authuserTransactionManager,
            @Qualifier("userTransactionManager") PlatformTransactionManager userTransactionManager) {
        return new ChainedTransactionManager(userTransactionManager,
                authuserTransactionManager);
    }

}
