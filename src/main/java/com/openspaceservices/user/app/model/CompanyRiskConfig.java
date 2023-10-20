/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openspaceservices.user.app.model;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

/**
 * @author pradeep
 */
@Data
@Entity
@Table(name = "optimeyesai_company_riskconfig")
public class CompanyRiskConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_company_riskconfig")
    private Long idCompanyRiskconfig;

    @Column(name = "id_company")
    private Long idCompany;

    @Column(name = "company_riskconfig_version")
    private String companyRiskConfigVersion;

    @Column(name = "id_industry")
    private Long industryId;

    @Column(name = "id_risk_tolerance")
    private Long idRiskTolerance;

    @Column(name = "risk_source_details")
    private String riskSourceDetails;

    @CreationTimestamp
    @Column(name = "Tx_timestamp")
    private Timestamp txTimestamp;

    @Column(name = "phase_flag")
    private Integer phaseFlag;

    @Column(name = "active")
    private int active;

    @Column(name = "reports_assessment_postive_negative_row_count")
    private int reportsAssessmentPostiveNegativeRowCount;

    @Transient
    private List<CompanyModuleMapModel> companyModuleMapModelsList;

    @Transient
    private String appUrl;

    @Transient
    private String ruleSource;

}
