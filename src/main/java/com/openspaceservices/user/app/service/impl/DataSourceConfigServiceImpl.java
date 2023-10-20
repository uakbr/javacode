/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openspaceservices.user.app.service.impl;

import com.openspaceservices.user.app.dao.DataSourceConfigDao;
import com.openspaceservices.user.app.model.DataSourceConfig;
import com.openspaceservices.user.app.service.DataSourceConfigService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author nehakasar
 */
@Service
@Log4j2
public class DataSourceConfigServiceImpl implements DataSourceConfigService {

    @Autowired
    DataSourceConfigDao dataSourceConfigDao;

    @Override
    public DataSourceConfig getDataSourceDetails(String Name) {
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        try {
            dataSourceConfig = dataSourceConfigDao.findByName(Name);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataSourceConfig;
    }

}
