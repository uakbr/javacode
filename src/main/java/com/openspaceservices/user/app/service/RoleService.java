/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openspaceservices.user.app.service;

import com.openspaceservices.user.apigateway.model.AuthUser;
import com.openspaceservices.user.app.model.Response;
import com.openspaceservices.user.app.model.RoleDTO;
import com.openspaceservices.user.app.model.RoleModel;

import java.util.List;

/**
 * @author Vishal Manval <a href="https://www.openspaceservices.com">Openspace
 * Services Pvt. Ltd.</a>
 */
public interface RoleService {

    public Response add(AuthUser authUser, RoleModel role);

    public Response update(AuthUser authUser, RoleModel role);

    public Response toggleStatus(Integer roleId, Boolean status);

    public Response createRoles(AuthUser authUser, List<RoleDTO> roleDTOs);

    public Response findByCompanyIDandActiveFlag(Long companyId, Character activeFlag);

    public Response findFunctionAccessByModuleAndRoleId(List<Integer> roleIdList, String moduleName, String functionName, Long companyId);
}
