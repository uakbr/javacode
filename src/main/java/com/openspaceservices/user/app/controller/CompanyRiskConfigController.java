
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openspaceservices.user.app.controller;

import com.openspaceservices.user.apigateway.model.AuthUser;
import com.openspaceservices.user.app.model.CompanyRiskConfig;
import com.openspaceservices.user.app.model.DefaultIndustryScore;
import com.openspaceservices.user.app.service.CompanyRiskConfigService;
import com.openspaceservices.user.custom.annotations.User;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Welcome Screen
 *
 * @author pradeep
 */
//@CrossOrigin
@RestController
@Log4j2
@RequestMapping("/companyRiskConfigController")
public class CompanyRiskConfigController {

    @Autowired
    private CompanyRiskConfigService companyRiskConfigService;

    @GetMapping("/companyRiskConfig")
    public List<CompanyRiskConfig> findAllCompanyRiskConfig(@User AuthUser user) {
        return companyRiskConfigService.findAllCompanyRiskConfig(user.getCompanyId());
    }

    @GetMapping("/risk-config-details/{companyId}")
    public List<DefaultIndustryScore> findRiskConfigDetails(@PathVariable("companyId") Long companyId) {
        return companyRiskConfigService.findRiskConfigDetails(companyId);
    }

    @GetMapping("/riskSourceVersionIdByRiskSource/{riskSource}/{riskSourceType}")
    public int riskSourceVersionIdByRiskSource(@PathVariable("riskSource") String riskSource, @PathVariable("riskSourceType") String riskSourceType, @User AuthUser user) {
        return companyRiskConfigService.riskSourceVersionIdByRiskSource(riskSource, riskSourceType, user.getCompanyId());
    }

    @GetMapping("/companyRiskConfigRiskSource/{riskSourceType}")
    public ResponseEntity<?> fetchRiskSourceOnTypeAndCompanyId(@PathVariable("riskSourceType") String riskSourceType, @User AuthUser user) {
        List<CompanyRiskConfig> companyRiskConfigRiskSourceList = new ArrayList();
        try {
            companyRiskConfigRiskSourceList = companyRiskConfigService.fetchRiskSourceOnTypeAndCompanyId(riskSourceType, user);
        } catch (Exception e) {
            log.info("Ëxception caught in [CompanyRiskConfigController]..[fetchRiskSourceOnType]..." + e.getMessage());
            e.printStackTrace();
        }
        return new ResponseEntity<List<CompanyRiskConfig>>(companyRiskConfigRiskSourceList, HttpStatus.OK);
    }

}
