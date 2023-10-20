/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openspaceservices.user.app.service.impl;

import com.openspaceservices.user.apigateway.model.AuthUser;
import com.openspaceservices.user.app.dao.CompanyRiskConfigDao;
import com.openspaceservices.user.app.model.CompanyRiskConfig;
import com.openspaceservices.user.app.model.DefaultIndustryScore;
import com.openspaceservices.user.app.repository.CompanyRiskConfigRepository;
import com.openspaceservices.user.app.service.CompanyRiskConfigService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author pradeep
 */
@Log4j2
@Service
public class CompanyRiskConfigServiceImpl implements CompanyRiskConfigService {

    @Autowired
    private CompanyRiskConfigRepository companyRiskConfigRepository;

    @Autowired
    private CompanyRiskConfigDao companyRiskConfigDao;

    @Override
    public List<CompanyRiskConfig> findAllCompanyRiskConfig(Long companyId) {
        List<CompanyRiskConfig> companyRiskConfigsList = new ArrayList<>();
        try {
            companyRiskConfigsList = companyRiskConfigRepository.findAllByIdCompanyAndActive(companyId, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return companyRiskConfigsList;
    }

    @Override
    public int riskSourceVersionIdByRiskSource(String riskSource, String riskSourceType, Long companyId) {
        return companyRiskConfigDao.riskSourceVersionIdByRiskSource(riskSource, riskSourceType, companyId);
    }

    public List<CompanyRiskConfig> fetchRiskSourceOnTypeAndCompanyId(String riskSourceType, AuthUser user) {
        List<CompanyRiskConfig> companyRiskConfigRiskSourceList = new ArrayList();
        try {
            companyRiskConfigRiskSourceList = companyRiskConfigDao.fetchRiskSourceOnTypeAndCompanyId(riskSourceType, user);
        } catch (Exception e) {
            log.info("Exception caught in [CompanyRiskConfigServiceIMpl]..[fetchRiskSourceOnTypeAndCompanyId]..." + e.getMessage());
            e.printStackTrace();
        }
        return companyRiskConfigRiskSourceList;
    }

    @Override
    public List<DefaultIndustryScore> findRiskConfigDetails(Long companyId) {
        return companyRiskConfigDao.findRiskConfigDetails(companyId);
    }

}
