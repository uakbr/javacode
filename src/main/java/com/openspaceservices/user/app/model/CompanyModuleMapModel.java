/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openspaceservices.user.app.model;

import com.openspaceservices.user.util.CommonUtil;
import lombok.Data;
import org.json.JSONObject;

import javax.persistence.Transient;
import java.sql.Timestamp;

/**
 * @author Pradeep
 */
@Data
public class CompanyModuleMapModel {

    private int idAdminOptimeyesaiCompanyModuleMapping;

    private String moduleDetails;

    private int companyId;

    private int active;

    private int createdBy;

    private int updatedBy;

    private Timestamp txTimestamp;

    private Timestamp subscriptionStartDate;

    private Timestamp subscriptionEndDate;

    private Integer noOfInstances;

    @Transient
    private int moduleId;

    @Transient
    private String moduleName;

    @Transient
    private String riskSourceVersion;

    @Transient
    private String riskSourceType;

    @Transient
    private int riskSourceId;

    @Transient
    private String mannualAutomatic;

    public CompanyModuleMapModel setDefaultTestValues(int moduleId, String moduleName, int noOfInstances, Timestamp subscriptionStartDate, Timestamp subscriptionEndDate, int companyId) {
        CompanyModuleMapModel companyModuleMapModel = new CompanyModuleMapModel();

        JSONObject jsonModuleObject = new JSONObject();
        jsonModuleObject.put("moduleId", moduleId);
        jsonModuleObject.put("moduleName", moduleName);

        companyModuleMapModel.setNoOfInstances(noOfInstances);
        companyModuleMapModel.setSubscriptionStartDate(CommonUtil.getStartDayTimestamp(
                CommonUtil.toDateStringFormat(subscriptionStartDate, "yyyy-MM-dd HH:mm:ss"),
                "yyyy-MM-dd HH:mm:ss"));
        companyModuleMapModel.setSubscriptionEndDate(CommonUtil.getEndDayTimestamp(
                CommonUtil.toDateStringFormat(subscriptionEndDate, "yyyy-MM-dd HH:mm:ss"),
                "yyyy-MM-dd HH:mm:ss"));
//        companyModuleMapModel.setSubscriptionStartDate(subscriptionStartDate);
//        companyModuleMapModel.setSubscriptionStartDate(subscriptionStartDate);
        companyModuleMapModel.setCompanyId(companyId);

        return companyModuleMapModel;
    }
}
