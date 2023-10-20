/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openspaceservices.user.app.dao;

import com.openspaceservices.user.app.model.DataSourceConfig;

/**
 * @author nehakasar
 */
public interface DataSourceConfigDao {

    public DataSourceConfig findByName(String Name);

}
