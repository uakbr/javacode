package com.openspaceservices.user.app.dao.impl;

import com.openspaceservices.user.apigateway.model.AuthUser;
import com.openspaceservices.user.app.dao.CompanyDao;
import com.openspaceservices.user.app.model.CompanyDTO;
import com.openspaceservices.user.app.model.CompanyModel;
import com.openspaceservices.user.app.model.Response;
import com.openspaceservices.user.app.repository.CompanyRepository;
import com.openspaceservices.user.constants.Constants;
import com.openspaceservices.user.util.CommonUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * @author Vishal Manval
 */
@Log4j2
@Repository
public class CompanyDaoImpl implements CompanyDao {

    @Autowired
    CompanyRepository companyRepository;

    @PersistenceContext
    EntityManager entityManager;

    /**
     * @param companyId
     * @return
     */
    @Override
    public CompanyModel findByCompanyId(Long companyId) {
        CompanyModel companyModel = new CompanyModel();
        try {
            companyModel = companyRepository.findByIdCompanyAndDeleteFlag(companyId, true);
        } catch (Exception e) {
            log.info("Exception in [CompanyDaoImpl] [findByCompanyId]:" + e.getMessage());
            e.printStackTrace();
        }
        return companyModel;
    }

    @Override
    public Response searchCompanies(CompanyDTO companyDTO, String userRole, AuthUser user) {
        List<CompanyDTO> companyDTOs = new ArrayList<>();
        Response response = new Response();

        StringBuilder query = new StringBuilder();
        query.append("select c.id_company,c.name,c.active,c.active_from,c.active_to, ");
        query.append(" c.FEIN, c.form_of_organization, c.industry, c.website, c.risk_appetite ");
        query.append(" from optimeyesai_company c");
        query.append(" where (1=1) ");

        // If Role is ADMIN then show only his company
        if ("ADMIN".equalsIgnoreCase(userRole)) {
            query.append(" AND c.id_company =").append(user.getCompanyId());
        }

        if (!CommonUtil.isNull(companyDTO.getName())) {
            query.append(" AND c.name like :companyName");
        }
        if (!CommonUtil.isNull(companyDTO.getActive())) {
            query.append(" AND c.active like :activeFlag");
        }
        if (!CommonUtil.isNull(companyDTO.getActiveFrom()) && CommonUtil.isNull(companyDTO.getActiveTo())) {
            query.append(" AND c.active_from between :startDate AND :startDate");
        } else if (CommonUtil.isNull(companyDTO.getActiveFrom()) && !CommonUtil.isNull(companyDTO.getActiveTo())) {
            query.append(" AND c.active_to between :finishDate AND :finishDate");
        } else if (!CommonUtil.isNull(companyDTO.getActiveFrom()) && !CommonUtil.isNull(companyDTO.getActiveTo())) {
            query.append(" AND c.active_from between :startDate AND :startDate");
            query.append(" AND c.active_to between :finishDate AND :finishDate");
        }

        Query qry = entityManager.createNativeQuery(query.toString());

        if (!CommonUtil.isNullAndEmpty(companyDTO.getName())) {
            qry.setParameter("companyName", (CommonUtil.isNullAndEmpty(companyDTO.getName()) ? "%%" : "%" + companyDTO.getName() + "%"));
        }
        if (!CommonUtil.isNull(companyDTO.getActive())) {
            qry.setParameter("activeFlag", (CommonUtil.isNull(companyDTO.getActive()) ? "%%" : "%" + companyDTO.getActive() + "%"));
        }
        if (!CommonUtil.isNull(companyDTO.getActiveFrom()) && CommonUtil.isNull(companyDTO.getActiveTo())) {
            qry.setParameter("startDate", companyDTO.getActiveFrom());
        } else if (CommonUtil.isNull(companyDTO.getActiveFrom()) && !CommonUtil.isNull(companyDTO.getActiveTo())) {
            qry.setParameter("finishDate", companyDTO.getActiveTo());
        } else if (!CommonUtil.isNull(companyDTO.getActiveFrom()) && !CommonUtil.isNull(companyDTO.getActiveTo())) {
            qry.setParameter("startDate", companyDTO.getActiveFrom());
            qry.setParameter("finishDate", companyDTO.getActiveTo());
        }

        List<Object> list = qry.getResultList();
        if (CommonUtil.isListNotNullAndEmpty(list)) {
            Iterator it = list.iterator();
            while (it.hasNext()) {
                try {
                    Object[] objs = (Object[]) it.next();
                    CompanyDTO dto = new CompanyDTO();
                    dto.setIdCompany(Long.valueOf((Integer) objs[0]));
                    dto.setName((String) objs[1]);
                    dto.setActive((Character) objs[2]);
                    dto.setActiveFrom(CommonUtil.toTimeStamp(CommonUtil.toDateStringFormat((Date) objs[3], "yyyy-MM-dd HH:mm:ss"), "yyyy-MM-dd HH:mm:ss"));
                    dto.setActiveTo(CommonUtil.toTimeStamp(CommonUtil.toDateStringFormat((Date) objs[4], "yyyy-MM-dd HH:mm:ss"), "yyyy-MM-dd HH:mm:ss"));
                    dto.setfEIN((String) objs[5]);
                    dto.setFormOfOrganization((String) objs[6]);
                    dto.setIndustry(String.valueOf(objs[7]));
                    dto.setWebsite((String) objs[8]);
                    dto.setRiskAppetite((double) objs[9]);
                    companyDTOs.add(dto);
                    response.setHttpStatus(HttpStatus.OK);
                    response.setData(companyDTOs);
                } catch (Exception e) {
                    log.info("Exception in [CompanyDaoImpl] [searchCompanies]:" + e.getMessage());
                    response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
                    response.setData(new ArrayList());
                    e.printStackTrace();
                }
            }
        }
        return response;
    }

    @Override
    //@Transactional
    public ResponseEntity<Response> updateCompanyDetail(Long companyId, CompanyModel companyModel) {
        Response responseData = new Response();
        try {
            companyRepository.save(companyModel);
            responseData.setResponseCode(Constants.SUCCESS_CODE);
            responseData.setResponseType(Constants.SUCCESS);
            responseData.setResponseMessage("Details saved successfully");
            return new ResponseEntity<Response>(responseData, HttpStatus.OK);
        } catch (Exception e) {
            log.info("Exception in [CompanyDoaImpl] [updateCompanyDetail]:" + e.getMessage());
            e.printStackTrace();
            responseData.setResponseCode(Constants.ERROR_CODE);
            responseData.setResponseType(Constants.ERROR);
            responseData.setResponseMessage("Error while saving details");
            return new ResponseEntity<Response>(responseData, HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @Override
    public ResponseEntity<Response> updateCompanyActiveDirectoryDetails(Long companyId, CompanyModel companyModel) {
      Response responseData = new Response();
        try {
            companyRepository.UpdateActiveDirectoryDetails(companyId, companyModel.getActivityDirectoryServer(),
                                                                      companyModel.getActivityDirectoryUrl(),
                                                                      companyModel.getActivityDirectoryPassword() );
            responseData.setResponseCode(Constants.SUCCESS_CODE);
            responseData.setResponseType(Constants.SUCCESS);
            responseData.setResponseMessage("Details update Successfully");
            return new ResponseEntity<Response>(responseData, HttpStatus.OK);
        } catch (Exception e) {
            log.info("Exception in [CompanyDoaImpl] [updateCompanyActiveDirectoryDetails]:" + e.getMessage());
            e.printStackTrace();
            responseData.setResponseCode(Constants.ERROR_CODE);
            responseData.setResponseType(Constants.ERROR);
            responseData.setResponseMessage("Error while updating details");
            return new ResponseEntity<Response>(responseData, HttpStatus.NOT_ACCEPTABLE);
        }
    }
}
