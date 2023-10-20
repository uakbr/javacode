/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openspaceservices.user.app.dao.impl;

import com.openspaceservices.user.apigateway.model.AuthUser;
import com.openspaceservices.user.app.dao.CompanyRiskConfigDao;
import com.openspaceservices.user.app.dao.UserDao;
import com.openspaceservices.user.app.model.CompanyRiskConfig;
import com.openspaceservices.user.app.model.DefaultIndustryScore;
import com.openspaceservices.user.app.model.RoleModel;
import com.openspaceservices.user.util.CommonUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * @author pradeep
 */
@Log4j2
@Repository
public class CompanyRiskConfigDaoImpl implements CompanyRiskConfigDao {

    @Autowired
    public UserDao userDao;
    @PersistenceContext
    EntityManager entityManager;

    @Override
    public int riskSourceVersionIdByRiskSource(String riskSource, String riskSourceType, Long companyId) {
        int riskSourceVersionId = 0;
        try {
            StringBuilder query = new StringBuilder();
            query.append("SELECT risk_source_json_details.risk_source_version_id FROM optimeyesai_company_riskconfig cr, ");
            query.append("JSON_TABLE(risk_source_details, '$[*]' COLUMNS( ");
            query.append("risk_source_version_id      int         PATH '$.risk_source_version_id', ");
            query.append("risk_source                 varchar(50) PATH '$.risk_source', ");
            query.append("riskSourceType              varchar(50) PATH '$.riskSourceType' ");
            query.append(")) as risk_source_json_details ");
            query.append("where cr.id_company =").append(companyId).append(" and cr.active=0 and ");
            query.append("risk_source_json_details.risk_source = '").append(riskSource).append("' and ");
            query.append("risk_source_json_details.riskSourceType = '").append(riskSourceType).append("' ");

            Query qry = entityManager.createNativeQuery(query.toString());
            riskSourceVersionId = (int) qry.getSingleResult();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return riskSourceVersionId;
    }

    public List<CompanyRiskConfig> fetchRiskSourceOnTypeAndCompanyId(String riskSourceType, AuthUser userModel) {
        List<CompanyRiskConfig> companyRiskConfigList = new ArrayList();
        boolean isUserLogin = false;
        try {
            for (RoleModel role : userModel.getRoleModelSet()) {
                if (!"SUPERADMIN".equals(role.getRoleName()) && !"ADMIN".equals(role.getRoleName())) {
                    isUserLogin = true;
                }
            }
            StringBuilder sqlQuery = new StringBuilder();
            sqlQuery.append("SELECT id_company,rsdetails.risk_source ");
            sqlQuery.append("FROM optimeyesai_company_riskconfig cr, ");
            sqlQuery.append("JSON_TABLE(risk_source_details, '$[*]' COLUMNS( ");
            sqlQuery.append("Owner_Name varchar(50)     PATH '$.Owner_Name', ");
            sqlQuery.append("Owner_desig  varchar(50) PATH '$.Owner_desig', ");
            sqlQuery.append("Owner_emailid  varchar(50)  PATH '$.Owner_emailid', ");
            sqlQuery.append("industry_impact_id  int  PATH '$.industry_impact_id', ");
            sqlQuery.append("riskSourceType  varchar(50)  PATH '$.riskSourceType', ");
            sqlQuery.append("riskSourceVersion  varchar(50)  PATH '$.riskSourceVersion', ");
            sqlQuery.append("risk_source  varchar(50)  PATH '$.risk_source', ");
            sqlQuery.append("risk_source_version_id  int  PATH '$.risk_source_version_id', ");
            sqlQuery.append("id_user int PATH '$.id_user' ");
            sqlQuery.append(")) as rsdetails where id_company=:companyId and active=0 ");
            if (!"NA".equals(riskSourceType)) {
                sqlQuery.append("and rsdetails.riskSourceType =:riskSourceType ");
            }

            if (isUserLogin) {
                sqlQuery.append(" and rsdetails.id_user=:idUser");
            }

            Query qry = entityManager.createNativeQuery(sqlQuery.toString());
            qry.setParameter("companyId", userModel.getCompanyId());
            if (!"NA".equals(riskSourceType)) {
                qry.setParameter("riskSourceType", riskSourceType);
            }
            if (isUserLogin) {
                qry.setParameter("idUser", userModel.getIdUser());
            }
            List list = qry.getResultList();
            ListIterator listIter = list.listIterator();
            while (listIter.hasNext()) {
                CompanyRiskConfig companyRiskConfigModel = new CompanyRiskConfig();
                Object[] objs = (Object[]) listIter.next();
                companyRiskConfigModel.setIdCompany(objs[0] != null ? Long.parseLong(objs[0].toString()) : 0);
                companyRiskConfigModel.setRuleSource(objs[1] != null ? objs[1].toString() : "");
                companyRiskConfigList.add(companyRiskConfigModel);
            }
        } catch (Exception e) {
            log.info("Exception caught in [CompanyRiskConfigDaoImpl]..[fetchRiskSourceOnTypeAndCompanyId]..." + e.getMessage());
            e.printStackTrace();
        }
        return companyRiskConfigList;
    }

    @Override
    public List<DefaultIndustryScore> findRiskConfigDetails(Long companyId) {
        List<DefaultIndustryScore> defaultIndustryScoresList = new ArrayList<>();
        try {
            StringBuilder query = new StringBuilder();
            query.append("SELECT id_company, id_industry,risk_source_version_id,risk_source,id_risk_tolerance,industry_impact_id,riskSourceId ");
            query.append("FROM optimeyesai_company_riskconfig cr, ");
            query.append("JSON_TABLE(risk_source_details, '$[*]' COLUMNS( ");
            query.append("riskSourceId                int         PATH '$.riskSourceId', ");
            query.append("risk_source_version_id      int         PATH '$.risk_source_version_id', ");
            query.append("risk_source                 varchar(50) PATH '$.risk_source', ");
            query.append("industry_impact_id          int         PATH '$.industry_impact_id' ");
            query.append(")) as risk_detail where id_company=:companyId and active=0 ");

            Query qry = entityManager.createNativeQuery(query.toString());
            qry.setParameter("companyId", companyId);

            List<Object> list = qry.getResultList();
            if (CommonUtil.isListNotNullAndEmpty(list)) {
                Iterator it = list.iterator();
                while (it.hasNext()) {
                    Object[] objs = (Object[]) it.next();
                    DefaultIndustryScore defaultIndustryScoreObj = new DefaultIndustryScore();
                    defaultIndustryScoreObj.setIdCompany(Long.valueOf((Integer) objs[0]));
                    defaultIndustryScoreObj.setIndustryId(Long.valueOf((Integer) objs[1]));
                    defaultIndustryScoreObj.setRiskSourceVersionId(Long.valueOf((Integer) objs[2]));
                    defaultIndustryScoreObj.setRiskSource((String) objs[3]);
                    defaultIndustryScoreObj.setIdRiskTolerance(Double.valueOf(String.valueOf(objs[4])));
                    defaultIndustryScoreObj.setIndustryImpactId(Double.valueOf(String.valueOf(objs[5])));
                    defaultIndustryScoreObj.setRiskSourceId(Long.valueOf((Integer) objs[6]));
                    defaultIndustryScoresList.add(defaultIndustryScoreObj);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defaultIndustryScoresList;
    }

}
