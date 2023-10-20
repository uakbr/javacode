package com.openspaceservices.user.app.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

/**
 * @author admin
 */
@Entity
@Table(name = "optimeyesai_users")
public class UserModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user")
    private Long idUser;

    @Column(name = "user_email")
    private String userEmail;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "password")
    private String password;

    //	@Column(name = "role_id")
//	private HashMap<String, Object> roleId;
    @Column(name = "active")
    private Character active;

    @Column(name = "created_by", updatable = false)
    private Long createdBy;

    @Column(name = "updated_by")
    private Long updatedBy;

    @Column(name = "active_from")
    private Timestamp activeFrom;

    @Column(name = "active_to")
    private Timestamp activeTo;

    @Column(name = "questionnaire_dept_id")
    private int questionnaireDeptId;

    //Added By Vineet On 09-01-2019
//    @Column(name = "ldap_user")
//    private int ldapUser;
//    
//    @Column(name = "ldap_group")
//    private String ldapGroup;
//    
//    @Column(name= "ldap_dc")
//    private String ldapDc;
    @Column(name = "ldap_credential")
    private String ldapCredential;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_company")
    private CompanyModel companyModelUser;
    @Transient
    private long idCompany;
    //    @JsonIgnore
//    @JoinTable(name = "optimeyesai_user_role_map",
//            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id_user"))
//    @Column(name = "role_id",nullable=true)
//    private Set<Integer> roleModelSet=new HashSet<>();
    @Transient
    private Set<Integer> roleId;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "optimeyesai_user_role_map", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id_user"))
    @Column(name = "role_id", nullable = true)
    private Set<Integer> roleModelSet = new HashSet<>();
    @Column(name = "Tx_timestamp")
    private Timestamp txTimestamp;

    public long getIdCompany() {
        return idCompany;
    }

    public void setIdCompany(long idCompany) {
        this.idCompany = idCompany;
    }

    public Set<Integer> getRoleId() {
        return roleId;
    }

    public void setRoleId(Set<Integer> roleId) {
        this.roleId = roleId;
    }

    public Set<Integer> getRoleModelSet() {
        return roleModelSet;
    }

    public void setRoleModelSet(Set<Integer> roleModelSet) {
        this.roleModelSet = roleModelSet;
    }

    /**
     * @return
     */
    public Timestamp getTxTimestamp() {
        return txTimestamp;
    }

    /**
     * @param txTimestamp
     */
    public void setTxTimestamp(Timestamp txTimestamp) {
        this.txTimestamp = txTimestamp;
    }

    /**
     * @return
     */
    public Long getIdUser() {
        return idUser;
    }

    /**
     * @param idUser
     */
    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    /**
     * @return
     */
    public String getUserEmail() {
        return userEmail;
    }

    /**
     * @param userEmail
     */
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    /**
     * @return
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return
     */
    public Long getCreatedBy() {
        return createdBy;
    }

    /**
     * @param createdBy
     */
    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * @return
     */
    public Long getUpdatedBy() {
        return updatedBy;
    }

    /**
     * @param updatedBy
     */
    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }

    /**
     * @return
     */
    public Timestamp getActiveFrom() {
        return activeFrom;
    }

    /**
     * @param activeFrom
     */
    public void setActiveFrom(Timestamp activeFrom) {
        this.activeFrom = activeFrom;
    }

    /**
     * @return
     */
    public Timestamp getActiveTo() {
        return activeTo;
    }

    /**
     * @param activeTo
     */
    public void setActiveTo(Timestamp activeTo) {
        this.activeTo = activeTo;
    }

    /**
     * @return
     */
    public CompanyModel getCompanyModelUser() {
        return companyModelUser;
    }

    /**
     * @param companyModelUser
     */
    public void setCompanyModelUser(CompanyModel companyModelUser) {
        this.companyModelUser = companyModelUser;
    }

    /**
     * @return
     */
    public Character getActive() {
        return active;
    }

    /**
     * @param active
     */
    public void setActive(Character active) {
        this.active = active;
    }

    public int getQuestionnaireDeptId() {
        return questionnaireDeptId;
    }

    public void setQuestionnaireDeptId(int questionnaireDeptId) {
        this.questionnaireDeptId = questionnaireDeptId;
    }

    public String getLdapCredential() {
        return ldapCredential;
    }

    public void setLdapCredential(String ldapCredential) {
        this.ldapCredential = ldapCredential;
    }

//    /**
//     *
//     * @return
//     */
//    public Set<RoleModel> getRoleModelSet() {
//        return roleModelSet;
//    }
//
//    /**
//     *
//     * @param roleModelSet
//     */
//    public void setRoleModelSet(Set<RoleModel> roleModelSet) {
//        this.roleModelSet = roleModelSet;
//    }
}
