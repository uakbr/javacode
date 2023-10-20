package com.openspaceservices.user.app.model;

import java.sql.Timestamp;
import java.util.ArrayList;

public class UserDTO {

    public String userRole;
    private Long idUser;
    private String userEmail;
    private String userName;
    private String password;
    private ArrayList<Integer> roleId;
    private Character active;
    private Long createdBy;
    private String createdName;
    private Long updatedBy;
    private String updatedName;
    private Timestamp activeFrom;
    private Timestamp activeTo;
    private Long idCompany;
    private String companyName;

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
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

    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /*public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}*/
    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Character getActive() {
        return active;
    }

    public void setActive(Character active) {
        this.active = active;
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

    public Long getIdCompany() {
        return idCompany;
    }

    public void setIdCompany(Long idCompany) {
        this.idCompany = idCompany;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public ArrayList<Integer> getRoleId() {
        return roleId;
    }

    public void setRoleId(ArrayList<Integer> roleId) {
        this.roleId = roleId;
    }
}
