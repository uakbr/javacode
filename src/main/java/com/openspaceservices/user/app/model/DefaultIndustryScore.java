/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openspaceservices.user.app.model;

import lombok.Data;

import java.sql.Timestamp;

/**
 * Used to save Phase 1 chart data
 *
 * @author pradeep
 */
@Data
public class DefaultIndustryScore {


    private Long idDefaultIndustryScore;

    private Long industryId;

    private Long riskSourceId;

    private Long riskSourceVersionId;

    private String riskSource;

    private double idRiskTolerance;

    private double industryImpactId;

    private Long idCompany;

    private String refParameters;

    private String discoveryScore;

    private Timestamp txTimestamp;

}
