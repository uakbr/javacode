package com.openspaceservices.user.app.repository;

import com.openspaceservices.user.app.model.CompanyModel;

import java.util.Map;

import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;


public interface CompanyRepository extends CrudRepository<CompanyModel, Long> {

    public CompanyModel findByIdCompanyAndDeleteFlag(Long companyId, Boolean active);
    
    
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value ="UPDATE optimeyesai_user_db.optimeyesai_company SET activity_directory_server =:activityDirectoryServer, "
            + "  activity_directory_url =:activityDirectoryUrl, "
            + "  activity_directory_password =:activityDirectoryPassword "
            + " WHERE id_company =:idCompany" , nativeQuery = true)
     public void UpdateActiveDirectoryDetails(Long idCompany, 
                                     String activityDirectoryServer,
                                     String activityDirectoryUrl,
                                     String activityDirectoryPassword);


    @Query(value = "SELECT id_company as companyId,name,industry,revenue FROM optimeyesai_company where id_company=:companyId", nativeQuery = true)
	public Map<Object, Object> findCompanyDetailsByCompanyId(int companyId);
}
