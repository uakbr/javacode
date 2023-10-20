/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openspaceservices.user.app.service.impl;

import com.openspaceservices.user.apigateway.model.AuthUser;
import com.openspaceservices.user.app.dao.CompanyDao;
import com.openspaceservices.user.app.dao.RoleDao;
import com.openspaceservices.user.app.model.CompanyModel;
import com.openspaceservices.user.app.model.Response;
import com.openspaceservices.user.app.model.RoleDTO;
import com.openspaceservices.user.app.model.RoleModel;
import com.openspaceservices.user.app.service.RoleService;
import com.openspaceservices.user.constants.Constants;
import com.openspaceservices.user.util.CommonUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.List;

/**
 * @author Vishal Manval <a href="https://www.openspaceservices.com">Openspace
 * Services Pvt. Ltd.</a>
 */
@Service
@Log4j2
public class RoleServiceImpl implements RoleService {

    private final RoleDao roleDao;

    private final CompanyDao companyDao;

    @Autowired
    WebClient.Builder webClientBuilder;

    @Autowired
    public RoleServiceImpl(RoleDao roleDao, CompanyDao companyDao) {
        this.companyDao = companyDao;
        this.roleDao = roleDao;
    }

    @Override
    public Response add(AuthUser authUser, RoleModel role) {
        Response response = new Response();
        try {
            role.setCreatedBy(authUser.getIdUser());
            response = webClientBuilder.build()
                    .post()
                    .uri("http://rbac-service/roles")
                    .body(Mono.just(role), RoleModel.class)
                    .retrieve()
                    .bodyToMono(Response.class)
                    .block();

        } catch (Exception e) {
            e.printStackTrace();
            response.setResponseCode(Constants.ERROR_CODE);
            response.setResponseType(Constants.ERROR);
            response.setResponseMessage("Failed to add role");
            response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    @Override
    public Response update(AuthUser authUser, RoleModel role) {
        Response response = new Response();
        try {
            role.setUpdatedBy(authUser.getIdUser());
            response = webClientBuilder.build()
                    .put()
                    .uri("http://rbac-service/roles")
                    .body(Mono.just(role), RoleModel.class)
                    .retrieve()
                    .bodyToMono(Response.class)
                    .block();
        } catch (Exception e) {
            e.printStackTrace();
            response.setResponseCode(Constants.ERROR_CODE);
            response.setResponseType(Constants.ERROR);
            response.setResponseMessage("Failed to update role");
            response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    @Override
    public Response toggleStatus(Integer roleId, Boolean status) {
        Response response = new Response();
        try {
            Character active = status ? 'Y' : 'N';
            response = webClientBuilder.build()
                    .delete()
                    .uri("http://rbac-service/roles/" + roleId + "/" + active)
                    .retrieve()
                    .bodyToMono(Response.class)
                    .block();
        } catch (Exception e) {
            e.printStackTrace();
            response.setResponseCode(Constants.ERROR_CODE);
            response.setResponseType(Constants.ERROR);
            response.setResponseMessage("Failed to update status");
        }
        return response;
    }

    @Override
    public Response createRoles(AuthUser authUser, List<RoleDTO> roleDTOs) {
        Response response = new Response();
        RoleModel roleModel;
        try {
            for (RoleDTO roleDTO : roleDTOs) {
                if (!CommonUtil.isNullAndEmpty(roleDTO.getRoleName()) && !CommonUtil.isNull(roleDTO.getActive())
                ) {
                    roleDTO.setIdCompany(authUser.getCompanyId());
                    roleModel = CommonUtil.copyBeanProperties(roleDTO, RoleModel.class);
                    CompanyModel companyModel = companyDao.findByCompanyId(roleDTO.getIdCompany());
                    roleModel.setCompanyModel(companyModel);
                    roleModel.setCompanyId(Integer.parseInt(authUser.getCompanyId().toString()));
                    roleModel.setCreatedBy(authUser.getIdUser());
                    roleModel.setUpdatedBy(authUser.getIdUser());
                    roleModel.setTxTimestamp(CommonUtil.toTimeStamp(CommonUtil.toDateStringFormat(new Date(), "yyyy-MM-dd HH:mm:ss"), "yyyy-MM-dd HH:mm:ss"));
                    roleModel.setRoleGroupingId(100L);
                    roleModel.setRoleGroupingName("Technology and Security");
                    response = webClientBuilder.build()
                            .post()
                            .uri("http://rbac-service/roles")
                            .body(Mono.just(roleModel), RoleModel.class)
                            .retrieve()
                            .bodyToMono(Response.class)
                            .block();
                }
                response.setHttpStatus(HttpStatus.OK);
                response.setResponseMessage("Role added successfully");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setResponseCode(Constants.ERROR_CODE);
            response.setResponseType(Constants.ERROR);
            response.setResponseMessage(e.getMessage());
            response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    @Override
    public Response findByCompanyIDandActiveFlag(Long companyId, Character activeFlag) {
        return roleDao.findByCompanyIDandActiveFlag(companyId, activeFlag);
    }

    @Override
    public Response findFunctionAccessByModuleAndRoleId(List<Integer> roleIdList, String moduleName, String functionName, Long companyId) {
        return roleDao.findFunctionAccessByModuleAndRoleId(roleIdList, moduleName, functionName, companyId);
    }
}
