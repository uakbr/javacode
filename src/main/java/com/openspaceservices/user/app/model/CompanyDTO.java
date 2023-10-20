package com.openspaceservices.user.app.model;

import java.sql.Timestamp;

public class CompanyDTO {

	private Long idCompany;

	private String name;

	private Character active;

	private Boolean deleteFlag;

	private Long createdBy;

	private String createdName;

	private Long updatedBy;

	private String updatedName;

	private Timestamp activeFrom;

	private Timestamp activeTo;

	private String companyLogo;

	private String address;

	private String fEIN;

	private String formOfOrganization;

	private String industry;

	private String website;

	private Double riskAppetite;

	public Double getRiskAppetite() {
		return riskAppetite;
	}

	public void setRiskAppetite(double objs) {
		this.riskAppetite = objs;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getfEIN() {
		return fEIN;
	}

	public void setfEIN(String fEIN) {
		this.fEIN = fEIN;
	}

	public String getFormOfOrganization() {
		return formOfOrganization;
	}

	public void setFormOfOrganization(String formOfOrganization) {
		this.formOfOrganization = formOfOrganization;
	}

	public String getIndustry() {
		return industry;
	}

	public void setIndustry(String industry) {
		this.industry = industry;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public Long getIdCompany() {
		return idCompany;
	}

	public void setIdCompany(Long idCompany) {
		this.idCompany = idCompany;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Character getActive() {
		return active;
	}

	public void setActive(Character active) {
		this.active = active;
	}

	public Long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	public Long getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Long updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Timestamp getActiveFrom() {
		return activeFrom;
	}

	public void setActiveFrom(Timestamp activeFrom) {
		this.activeFrom = activeFrom;
	}

	public Timestamp getActiveTo() {
		return activeTo;
	}

	public void setActiveTo(Timestamp activeTo) {
		this.activeTo = activeTo;
	}

	public String getCreatedName() {
		return createdName;
	}

	public void setCreatedName(String createdName) {
		this.createdName = createdName;
	}

	public String getUpdatedName() {
		return updatedName;
	}

	public void setUpdatedName(String updatedName) {
		this.updatedName = updatedName;
	}

	public String getCompanyLogo() {
		return companyLogo;
	}

	public void setCompanyLogo(String companyLogo) {
		this.companyLogo = companyLogo;
	}

}
