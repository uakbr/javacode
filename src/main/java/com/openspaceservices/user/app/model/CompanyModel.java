package com.openspaceservices.user.app.model;

import java.math.BigInteger;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Pradeep
 */
@Getter
@Setter
@Entity
@Table(name = "optimeyesai_company")
public class CompanyModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_company")
    private Long idCompany;

    @Column(name = "name")
    private String name;

    @Column(name = "active")
    private Character active;

    @Column(name = "delete_flag")
    private Boolean deleteFlag;

    @Column(name = "address")
    private String address;

    @Column(name = "FEIN")
    private String fEIN;

    @Column(name = "form_of_organization")
    private String formOfOrganization;

    @Column(name = "industry")
    private Long industry;
    
    @Column(name = "revenue")
    private BigInteger revenue;

    @Column(name = "tolerance")
    private Integer tolerance;
    
    @Column(name = "risk_appetite")
    private Double riskAppetite;

    @Column(name = "website")
    private String website;

    @Column(name = "created_by", updatable = false)
    private Long createdBy;

    @Column(name = "updated_by")
    private Long updatedBy;

    @Column(name = "active_from")
    private Timestamp activeFrom;

    @Column(name = "active_to")
    private Timestamp activeTo;

    @Column(name = "Tx_timestamp")
    private Timestamp txTimestamp;

    @Column(name = "company_logo")
    private String companyLogo;

    @Column(name = "watermark")
    private String watermark;

    @Column(name = "authentication_type")
    private int authenticationType;

    @Column(name = "activity_directory_url")
    private String activityDirectoryUrl;

    @Column(name = "activity_directory_password")
    private String activityDirectoryPassword;
    
    @Column(name = "activity_directory_server")
    private String activityDirectoryServer;
}
