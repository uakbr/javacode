package com.openspaceservices.user.app.dao;

import com.openspaceservices.user.apigateway.model.AuthUser;
import com.openspaceservices.user.app.model.CompanyDTO;
import com.openspaceservices.user.app.model.CompanyModel;
import com.openspaceservices.user.app.model.Response;
import org.springframework.http.ResponseEntity;

public interface CompanyDao {

    public CompanyModel findByCompanyId(Long companyId);

    public Response searchCompanies(CompanyDTO companyDTO, String userRole, AuthUser user);

    public ResponseEntity<Response> updateCompanyDetail(Long companyId, CompanyModel companyModel);
    
    public ResponseEntity<Response> updateCompanyActiveDirectoryDetails(Long companyId, CompanyModel companyModel);
     

}
