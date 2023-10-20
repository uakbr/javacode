/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openspaceservices.user.app.service;

import com.openspaceservices.user.apigateway.model.AuthUser;
import com.openspaceservices.user.app.model.CompanyDTO;
import com.openspaceservices.user.app.model.CompanyModel;
import com.openspaceservices.user.app.model.Response;

import java.util.Map;

import org.springframework.http.ResponseEntity;

/**
 * @author Vishal Manval <a href="https://www.openspaceservices.com">Openspace
 * Services Pvt. Ltd.</a>
 */
public interface CompanyService {

    public Response searchCompanies(CompanyDTO companyDTO, AuthUser user);

    public CompanyModel getCompanyDetailsById(Long id);

    public ResponseEntity<Response> updateCompanyDetail(Long companyId, CompanyModel companyModel);

    public Response findCompany(Long companyId);
    
    public ResponseEntity<Response> updateCompanyActiveDirectoryDetails(Long companyId, CompanyModel companyModel);

	public Map<Object, Object> findCompanyDetailsByCompanyId(int companyId);

}
