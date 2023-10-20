package com.openspaceservices.user.app.model;

import java.sql.Timestamp;
import java.util.Set;

/**
 * @author Vishal
 */
//@Entity
//@Table(name = "optimeyesai_role")
public class RoleModel {

    //    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "id_role")
    private Integer idRole;

    //    @Column(name = "role_name")
    private String roleName;

    //    @Column(name = "active")
    private Character active;

    //    @Column(name = "created_by", updatable = false)
    private Long createdBy;

    //    @Column(name = "updated_by")
    private Long updatedBy;

    //    @Column(name = "Tx_timestamp")
    private Timestamp txTimestamp;

    //    @Column(name = "active_from")
    private Timestamp activeFrom;

    //    @Column(name = "active_to")
    private Timestamp activeTo;

    //    @Column(name = "role_grouping_id")
    private Long roleGroupingId;

    //    @Column(name = "role_grouping_name")
    private String roleGroupingName;

    //    @Column(name = "role_access_rights")
    private String roleAccessRights;

    //    @JsonIgnore
//    @ManyToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "id_company")
    private CompanyModel companyModel;
    private int companyId;
    //    @JsonIgnore
//    @ManyToMany(mappedBy = "roleModelSet")
    private Set<UserModel> userModelSet;

    private boolean deleteShowFlag = false;

    public boolean isDeleteShowFlag() {
        return deleteShowFlag;
    }

    public void setDeleteShowFlag(boolean deleteShowFlag) {
        this.deleteShowFlag = deleteShowFlag;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public Set<UserModel> getUserModelSet() {
        return userModelSet;
    }

    public void setUserModelSet(Set<UserModel> userModelSet) {
        this.userModelSet = userModelSet;
    }

    public Timestamp getTxTimestamp() {
        return txTimestamp;
    }

    public void setTxTimestamp(Timestamp txTimestamp) {
        this.txTimestamp = txTimestamp;
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

    public CompanyModel getCompanyModel() {
        return companyModel;
    }

    public void setCompanyModel(CompanyModel companyModel) {
        this.companyModel = companyModel;
    }

    public String getRoleAccessRights() {
        return roleAccessRights;
    }

    public void setRoleAccessRights(String roleAccessRights) {
        this.roleAccessRights = roleAccessRights;
    }

}
