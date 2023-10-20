/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openspaceservices.user.app.service;

import com.openspaceservices.user.app.model.DataSourceConfig;

/**
 * @author nehakasar
 */
public interface DataSourceConfigService {

    public DataSourceConfig getDataSourceDetails(String Name);

}
