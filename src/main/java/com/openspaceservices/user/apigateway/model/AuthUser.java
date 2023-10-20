/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openspaceservices.user.apigateway.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.openspaceservices.user.app.model.RoleModel;
import com.openspaceservices.user.app.model.UserModel;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

/**
 * @author Vishal Manval <a href="https://www.openspaceservices.com">Openspace
 * Services Pvt. Ltd.</a>
 */
@Data
@Entity
@Table(name = "optimeyesai_user_auth")
@ToString
public class AuthUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user_auth")
    private Long idUserAuth;

    @Column(name = "id_user")
    private Long idUser;

    @Column(name = "user_email")
    private String userEmail;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "user_password")
    private String password;

    @JsonIgnore
    @Column(name = "auth_token")
    private String authToken;

    @Column(name = "active")
    private Character active;

    @Column(name = "active_from")
    private Timestamp activeFrom;

    @Column(name = "active_to")
    private Timestamp activeTo;

    @Column(name = "Tx_timestamp")
    private Timestamp txTimestamp;

    @Column(name = "otp")
    private String otp;

    @Transient
    private List<RoleModel> roleModelSet;

    @Transient
    private Long companyId;

    @Transient
    private String domainName;

    @Transient
    private String adToken;

    public UserModel setDefaultTestValues(String userEmail, String password) {
        UserModel model = new UserModel();
        model.setUserEmail(userEmail);
        model.setPassword(password);
        return model;
    }

}
