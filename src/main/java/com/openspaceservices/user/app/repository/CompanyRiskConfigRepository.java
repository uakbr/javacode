/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openspaceservices.user.app.repository;

import com.openspaceservices.user.app.model.CompanyRiskConfig;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * @author pradeep
 */
public interface CompanyRiskConfigRepository extends CrudRepository<CompanyRiskConfig, Long> {

    public List<CompanyRiskConfig> findAllByIdCompanyAndActive(Long companyId, int active);

}
