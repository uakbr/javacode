
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openspaceservices.user.app.dao;

import com.openspaceservices.user.apigateway.model.AuthUser;
import com.openspaceservices.user.app.model.CompanyRiskConfig;
import com.openspaceservices.user.app.model.DefaultIndustryScore;

import java.util.List;

/**
 * @author pradeep
 */
public interface CompanyRiskConfigDao {

    public int riskSourceVersionIdByRiskSource(String riskSource, String riskSourceType, Long companyId);

    public List<CompanyRiskConfig> fetchRiskSourceOnTypeAndCompanyId(String riskSourceType, AuthUser user);

    public List<DefaultIndustryScore> findRiskConfigDetails(Long companyId);

}
