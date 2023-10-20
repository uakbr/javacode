/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openspaceservices.user.app.service.impl;

import com.openspaceservices.user.apigateway.model.AuthUser;
import com.openspaceservices.user.app.dao.CompanyDao;
import com.openspaceservices.user.app.model.CompanyDTO;
import com.openspaceservices.user.app.model.CompanyModel;
import com.openspaceservices.user.app.model.Response;
import com.openspaceservices.user.app.model.RoleModel;
import com.openspaceservices.user.app.repository.CompanyRepository;
import com.openspaceservices.user.app.service.CompanyService;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * @author Vishal Manval <a href="https://www.openspaceservices.com">Openspace
 * Services Pvt. Ltd.</a>
 */
@Service
public class CompanyServiceImpl implements CompanyService {

    private final CompanyDao companyDao;
    
    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    public CompanyServiceImpl(CompanyDao companyDao) {
        this.companyDao = companyDao;
    }

    @Override
    public Response searchCompanies(CompanyDTO companyDTO, AuthUser user) {
        String userRole = "";
        for (RoleModel role : user.getRoleModelSet()) {
            if ("ADMIN".equalsIgnoreCase(role.getRoleName())) {
                userRole = "ADMIN";
            }
        }
        return this.companyDao.searchCompanies(companyDTO, userRole, user);
    }

    @Override
    public CompanyModel getCompanyDetailsById(Long id) {
        return companyDao.findByCompanyId(id);
    }

    @Override
    public ResponseEntity<Response> updateCompanyDetail(Long companyId, CompanyModel companyModel) {
        return companyDao.updateCompanyDetail(companyId, companyModel);
    }

    public Response findCompany(Long companyId) {
        Response response = new Response();
        try {
            CompanyModel company = this.companyDao.findByCompanyId(companyId);
            response.setData(company);
            response.setHttpStatus(HttpStatus.OK);
        } catch (Exception e) {
            response.setData(null);
            response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            e.printStackTrace();
        }
        return response;
    }

    @Override
    public ResponseEntity<Response> updateCompanyActiveDirectoryDetails(Long companyId, CompanyModel companyModel) {
        return companyDao.updateCompanyActiveDirectoryDetails(companyId, companyModel);
    }

	@Override
	public Map<Object, Object> findCompanyDetailsByCompanyId(int companyId) {
			
			return companyRepository.findCompanyDetailsByCompanyId(companyId);
	
	}

}
