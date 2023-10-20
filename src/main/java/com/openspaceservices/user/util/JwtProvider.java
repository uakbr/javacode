/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openspaceservices.user.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openspaceservices.user.apigateway.model.AuthUser;
import com.openspaceservices.user.app.model.UserModel;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * @author +IT KHAMBE working in
 * <a href="https://www.openspaceservices.com">Openspace Services Pvt. Ltd.</a>
 */
@Component
@Log4j2
public class JwtProvider implements Serializable {

    private static final long serialVersionUID = -2550185165626007488L;

    @Value("${auth.secret.key}")
    private String secret;

    /**
     * *************************************************************************
     * Retrieve Email from JWT token
     * *************************************************************************
     *
     * @param token
     * @return Email
     */
    public String getEmailFromToken(String token) {
        try {
            AuthUser user = this.getUserFromToken(token);
            return user.getUserEmail();
        } catch (Exception e) {

            return null;
        }

    }

    /**
     * *************************************************************************
     * Retrieve Email from JWT token
     * *************************************************************************
     *
     * @param token
     * @return Username
     */
    public String getUsernameFromToken(String token) {
        try {
            AuthUser user = this.getUserFromToken(token);
            return user.getUserName();
        } catch (Exception e) {
            return null;
        }

    }

    /**
     * *************************************************************************
     * Retrieve User from JWT token
     * *************************************************************************
     *
     * @param token
     * @return user
     */
    public AuthUser getUserFromToken(String token) {
        AuthUser user = new AuthUser();
        try {
            String subject = this.getClaimFromToken(token, Claims::getSubject);
            ObjectMapper mapper = new ObjectMapper();
            user = mapper.readValue(subject, AuthUser.class);

        } catch (JsonProcessingException e) {
            log.info("Error in getUserFromToken " + e.getMessage());
        }
        return user;
    }

    /**
     * *************************************************************************
     * Retrieve expiration date from JWT Token
     * *************************************************************************
     *
     * @param token
     * @return ExpirationDate
     */
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    /**
     * *************************************************************************
     * for Retrieving any information from token we will need the secret key
     * *************************************************************************
     */
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    /**
     * *************************************************************************
     * Check if the token has expired
     * *************************************************************************
     */
    public Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    /**
     * *************************************************************************
     * Generate token for user
     * *************************************************************************
     *
     * @param user
     * @return token
     * @throws com.fasterxml.jackson.core.JsonProcessingException
     */
    public String generateToken(AuthUser user) throws JsonProcessingException {

        Map<String, Object> claims = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = mapper.writeValueAsString(user);
        return doGenerateToken(claims, jsonInString);
    }

    /**
     * *************************************************************************
     * while creating the token ------------------------------------------------
     * 1.Define claims of the token, like Issuer, Expiration, Subject, and the
     * ID ----------------------------------------------------------------------
     * 2. Sign the JWT using the HS512 algorithm and secret key.----------------
     * 3. According to JWS Compact
     * Serialization(https://tools.ietf.org/html/draft-ietf-jose-json-web-signature-41#section-3.1)
     * compaction of the JWT to a URL-safe string
     * *************************************************************************
     */
    private String doGenerateToken(Map<String, Object> claims, String subject) {
        Long days = 1 * 24 * 60 * 60 * 1000l;
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + days))
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    /**
     * *************************************************************************
     * validate Token
     * *************************************************************************
     *
     * @param token
     * @param user
     * @return Boolean
     */
    public Boolean validateToken(String token, UserModel user) {
        try {
            String email = this.getEmailFromToken(token);
            return (email.equals(user.getUserEmail()) && !isTokenExpired(token));
        } catch (Exception e) {
            log.info(" Exception in validateToken" + e.getMessage());
            return false;
        }

    }
}
