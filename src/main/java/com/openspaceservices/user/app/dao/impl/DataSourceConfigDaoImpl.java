/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openspaceservices.user.app.dao.impl;

import com.openspaceservices.user.app.dao.DataSourceConfigDao;
import com.openspaceservices.user.app.model.DataSourceConfig;
import com.openspaceservices.user.app.repository.DataSourceConfigRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @author nehakasar
 */
@Log4j2
@Repository
public class DataSourceConfigDaoImpl implements DataSourceConfigDao {

    @Autowired
    DataSourceConfigRepository dataSourceRepository;

    @Override
    public DataSourceConfig findByName(String Name) {
        DataSourceConfig dataSource = new DataSourceConfig();
        try {
            dataSource = dataSourceRepository.findByNameAndInitialize(Name,true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataSource;
    }

}
