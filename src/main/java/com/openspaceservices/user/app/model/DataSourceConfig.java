/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openspaceservices.user.app.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "datasourceconfig")
public class DataSourceConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "url")
    private String url;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "driver_class_name")
    private String driverClassName;

    @Column(name = "initialize")
    private boolean initialize;

    @Column(name = "company_id")
    private Long companyId;
//
//    @Override
//    public String toString() {
//        final StringBuilder sb = new StringBuilder("DataSourceConfig{");
//        sb.append("id=").append(id);
//        sb.append(", name='").append(name).append('\'');
//        sb.append(", url='").append(url).append('\'');
//        sb.append(", username='").append(username).append('\'');
//        sb.append(", password='").append(password).append('\'');
//        sb.append(", driverClassName='").append(driverClassName).append('\'');
//        sb.append(", initialize=").append(initialize);
//        sb.append('}');
//        return sb.toString();
//    }
}
