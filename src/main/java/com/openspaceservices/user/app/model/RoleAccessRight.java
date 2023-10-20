/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openspaceservices.user.app.model;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * @author pradeep
 */
public class RoleAccessRight {

    private int service_manager_config_id;
    private String function_name;
    private String access_rights;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private transient String moduleName;

    public int getService_manager_config_id() {
        return service_manager_config_id;
    }

    public void setService_manager_config_id(int service_manager_config_id) {
        this.service_manager_config_id = service_manager_config_id;
    }

    public String getFunction_name() {
        return function_name;
    }

    public void setFunction_name(String function_name) {
        this.function_name = function_name;
    }

    public String getAccess_rights() {
        return access_rights;
    }

    public void setAccess_rights(String access_rights) {
        this.access_rights = access_rights;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }
}
