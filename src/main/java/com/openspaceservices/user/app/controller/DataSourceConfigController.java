/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openspaceservices.user.app.controller;

import com.openspaceservices.user.app.model.DataSourceConfig;
import com.openspaceservices.user.app.service.DataSourceConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author nehakasar
 */
@RestController
@RequestMapping("/dataSourceConfig")
public class DataSourceConfigController {

    @Autowired
    DataSourceConfigService dataSourceConfigService;

    @GetMapping("/{name}")
    public DataSourceConfig getCompany(@PathVariable String name) {
        DataSourceConfig dataSourceConfig = dataSourceConfigService.getDataSourceDetails(name);
        return dataSourceConfig;
    }

}
