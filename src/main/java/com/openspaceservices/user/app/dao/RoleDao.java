package com.openspaceservices.user.app.dao;

import com.openspaceservices.user.app.model.Response;

import java.util.List;

public interface RoleDao {

    public Response findByCompanyIDandActiveFlag(Long companyId, Character activeFlag);

    public Response findFunctionAccessByModuleAndRoleId(List<Integer> roleIdList, String moduleName, String functionName, Long companyId);

}
