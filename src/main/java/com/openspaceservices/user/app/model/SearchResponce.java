/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openspaceservices.user.app.model;

import lombok.Data;

/**
 * @author OSS
 */
@Data
public class SearchResponce {

    private String givenName;
    private String CN;
    private String CN_OU;
    private String DC;
    private String department;
    private String company;
    private String physicalDeliveryOfficeName;
    private String userPrincipalName;


}
