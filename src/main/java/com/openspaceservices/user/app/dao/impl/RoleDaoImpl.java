package com.openspaceservices.user.app.dao.impl;

import com.openspaceservices.user.app.dao.RoleDao;
import com.openspaceservices.user.app.model.Response;
import com.openspaceservices.user.app.model.RoleAccessRight;
import com.openspaceservices.user.app.model.RoleModel;
import com.openspaceservices.user.util.CommonUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Vishal
 */
@Repository
@Log4j2
public class RoleDaoImpl implements RoleDao {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public Response findByCompanyIDandActiveFlag(Long companyId, Character activeFlag) {
        Response response = new Response();
        List<RoleModel> roleModels = new ArrayList<>();
        StringBuffer query = new StringBuffer();
        query.append("SELECT * FROM optimeyesai_rbac.optimeyesai_role where optimeyesai_role.id_company=:companyId ");
//        query.append(" and optimeyesai_role.active like :falg");
        Query qry = entityManager.createNativeQuery(query.toString());
        qry.setParameter("companyId", companyId);
//        qry.setParameter("falg", "%" + activeFlag + "%");
        try {
            List<Object> list = qry.getResultList();
            if (CommonUtil.isListNotNullAndEmpty(list)) {
                Iterator it = list.iterator();
                while (it.hasNext()) {
                    try {
                        Object[] objs = (Object[]) it.next();
                        RoleModel model = new RoleModel();
                        model.setIdRole((Integer) objs[0]);
                        model.setRoleName((String) objs[1]);
                        model.setActive((Character) objs[2]);
                        StringBuffer query2 = new StringBuffer();
                        query2.append("SELECT count(user_id) FROM optimeyesai_user_role_map where role_id=:roleId ");
//        query.append(" and optimeyesai_role.active like :falg");
                        Query qry2 = entityManager.createNativeQuery(query2.toString());
                        qry2.setParameter("roleId", model.getIdRole());
                        Object count = qry2.getSingleResult();
                        if (Integer.parseInt(String.valueOf(count)) == 0) {
                            model.setDeleteShowFlag(true);
                        } else {
                            model.setDeleteShowFlag(false);
                        }
                        roleModels.add(model);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            response.setHttpStatus(HttpStatus.OK);
            response.setData(roleModels);
        } catch (Exception e) {
            log.info("Exception in [CompanyDaoImpl] [findByCompanyIDandActiveFlag]:" + e.getMessage());
            response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            response.setData(new ArrayList());
            e.printStackTrace();
        }
        return response;
    }

    @Override
    public Response findFunctionAccessByModuleAndRoleId(List<Integer> roleIdList, String moduleName, String functionName, Long companyId) {
        Response response = new Response();
        List<RoleAccessRight> functionAccessRights = new ArrayList<>();
        try {
            String roleIdCommaSeparated = roleIdList.stream().map(String::valueOf).collect(Collectors.joining(","));
            StringBuilder query = new StringBuilder();
            query.append("SELECT distinct role_access.service_manager_config_id,m.module_name, role_access.function_name, role_access.access_rights ");
            query.append("FROM optimeyesai_rbac.optimeyesai_role r, optimeyesai_service_manager_config m, ");
            query.append("JSON_TABLE(role_access_rights, '$[*]' COLUMNS( ");
            query.append("service_manager_config_id  int         PATH '$.service_manager_config_id', ");
            query.append("function_name              varchar(20) PATH '$.function_name', ");
            query.append("access_rights              varchar(5)  PATH '$.access_rights' ");
            query.append(")) role_access where role_access.service_manager_config_id = m.service_manager_config_id ");
            query.append("AND id_company=:companyId AND id_role in (").append(roleIdCommaSeparated).append(") ");

            if (!"ALL".equalsIgnoreCase(moduleName) && !"".equalsIgnoreCase(moduleName)) {
                query.append(" AND m.module_name =:moduleName ");
            }

            if (!"ALL".equalsIgnoreCase(functionName) && !"".equalsIgnoreCase(functionName)) {
                query.append(" AND role_access.function_name=:functionName ");
            }

            Query qry = entityManager.createNativeQuery(query.toString());
            qry.setParameter("companyId", companyId);

            if (!"ALL".equalsIgnoreCase(moduleName) && !"".equalsIgnoreCase(moduleName)) {
                qry.setParameter("moduleName", moduleName);
            }

            if (!"ALL".equalsIgnoreCase(functionName) && !"".equalsIgnoreCase(functionName)) {
                qry.setParameter("functionName", functionName);
            }

            List<Object> list = qry.getResultList();
            if (CommonUtil.isListNotNullAndEmpty(list)) {
                Iterator it = list.iterator();
                while (it.hasNext()) {
                    try {
                        Object[] objs = (Object[]) it.next();
                        RoleAccessRight roleAccessRightObj = new RoleAccessRight();
                        roleAccessRightObj.setService_manager_config_id((int) objs[0]);
                        roleAccessRightObj.setModuleName((String) objs[1]);
                        roleAccessRightObj.setFunction_name((String) objs[2]);
                        roleAccessRightObj.setAccess_rights((String) objs[3]);
                        functionAccessRights.add(roleAccessRightObj);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            response.setHttpStatus(HttpStatus.OK);
            response.setData(functionAccessRights);
        } catch (Exception e) {
            log.info("Exception in [RoleDaoImpl] [findFunctionAccessByModuleAndRoleId]:" + e.getMessage());
            response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            response.setData(functionAccessRights);
            e.printStackTrace();
        }
        return response;
    }

}
