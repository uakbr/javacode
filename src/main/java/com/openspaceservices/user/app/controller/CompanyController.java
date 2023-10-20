/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openspaceservices.user.app.controller;

import com.openspaceservices.user.apigateway.model.AuthUser;
import com.openspaceservices.user.app.model.CompanyDTO;
import com.openspaceservices.user.app.model.CompanyModel;
import com.openspaceservices.user.app.model.Response;
import com.openspaceservices.user.app.service.CompanyService;
import com.openspaceservices.user.custom.annotations.User;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author Vishal Manval <a href="https://www.openspaceservices.com">Openspace
 * Services Pvt. Ltd.</a>
 */
@RestController
@RequestMapping("/companies")
public class CompanyController {
    private final CompanyService companyService;

    @Autowired
    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping("searchCompanies")
    public ResponseEntity getAllCompanies(@User AuthUser user, @RequestBody CompanyDTO companyDTO) {
        Response responseObj = companyService.searchCompanies(companyDTO, user);
        return new ResponseEntity<>(responseObj, responseObj.getHttpStatus());
    }

    @GetMapping("/{id}")
    public CompanyModel getCompanyDetailsById(@PathVariable Long id) {
        return companyService.getCompanyDetailsById(id);
    }

    @PutMapping
    public ResponseEntity<Response> updateCompanyDetail(@User AuthUser user, @RequestBody CompanyModel companyModel) {
        return companyService.updateCompanyDetail(user.getCompanyId(), companyModel);
    }

    @GetMapping("/{companyId}/details")
    public ResponseEntity getCompany(@PathVariable Long companyId) {
        Response responseObj = companyService.findCompany(companyId);
        return new ResponseEntity<>(responseObj, responseObj.getHttpStatus());
    }

    @PutMapping("updateCompanyActiveDirectory")
    public ResponseEntity<Response> updateCompanyActiveDirectory(@User AuthUser user, @RequestBody CompanyModel companyModel) 
    {
     return companyService.updateCompanyActiveDirectoryDetails(user.getCompanyId(), companyModel);
    }
    
    @GetMapping("/getCompanyDetail/{companyId}")
   	public Map<Object,Object> getCompanyDetailsByCompanyId(@PathVariable int companyId){
   		return companyService.findCompanyDetailsByCompanyId(companyId);
 }
}
