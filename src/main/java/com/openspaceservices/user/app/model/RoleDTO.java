/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openspaceservices.user.app.model;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author admin
 */
public class RoleDTO {

    private Integer idRole;

    private String roleName;

    private Character active;

    private Long createdBy;

    private Long updatedBy;

    private Timestamp activeFrom;

    private Timestamp activeTo;

    private Long roleGroupingId;

    private String roleGroupingName;

    private Long idCompany;

    private String roleAccessRights;

    private List roleAccessRightsList;

    public Integer getIdRole() {
        return idRole;
    }

    public void setIdRole(Integer idRole) {
        this.idRole = idRole;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
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

    public Long getRoleGroupingId() {
        return roleGroupingId;
    }

    public void setRoleGroupingId(Long roleGroupingId) {
        this.roleGroupingId = roleGroupingId;
    }

    public String getRoleGroupingName() {
        return roleGroupingName;
    }

    public void setRoleGroupingName(String roleGroupingName) {
        this.roleGroupingName = roleGroupingName;
    }

    public Long getIdCompany() {
        return idCompany;
    }

    public void setIdCompany(Long idCompany) {
        this.idCompany = idCompany;
    }

    public String getRoleAccessRights() {
        return roleAccessRights;
    }

    public void setRoleAccessRights(String roleAccessRights) {
        this.roleAccessRights = roleAccessRights;
    }

    public List getRoleAccessRightsList() {
        return roleAccessRightsList;
    }

    public void setRoleAccessRightsList(List roleAccessRightsList) {
        this.roleAccessRightsList = roleAccessRightsList;
    }

}
