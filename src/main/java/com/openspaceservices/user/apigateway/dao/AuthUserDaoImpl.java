/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openspaceservices.user.apigateway.dao;

import com.openspaceservices.user.apigateway.model.AuthUser;
import com.openspaceservices.user.apigateway.repository.AuthUserRepository;
import com.openspaceservices.user.app.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author nehakasar
 */
@Repository
public class AuthUserDaoImpl implements AuthUserDao {

    @Autowired
    AuthUserRepository authUserRepository;

    @Override
    @Transactional
    public Response saveAuthUSer(AuthUser user) {
        Response response = new Response();
        try {
            user = authUserRepository.save(user);
            response.setData(user);
            response.setHttpStatus(HttpStatus.OK);
            response.setResponseMessage("User details added successfully");
        } catch (Exception e) {
            response.setData(user);
            response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            response.setResponseMessage("Failed to add");
            e.printStackTrace();
        }
        return response;
    }

    @Override
    public Response findUserByUserName(String userName) {
        Response response = new Response();
        AuthUser user = new AuthUser();
        try {
            user = authUserRepository.findByUserName(userName);
            response.setData(user);
            response.setHttpStatus(HttpStatus.OK);
            response.setResponseMessage("Fetch data Successfully");
        } catch (Exception e) {
            response.setData(user);
            response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            response.setResponseMessage("Failed to fetch");
            e.printStackTrace();
        }
        return response;
    }

    @Override
    @Transactional
    public Response toggleUser(long userId, Character active) {
        Response response = new Response();
        try {
            authUserRepository.toggleUserStatus(userId, active);
            response.setHttpStatus(HttpStatus.OK);
            response.setResponseMessage("Delete data Successfully");
        } catch (Exception e) {
            response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            response.setResponseMessage("Failed to Delete");
            e.printStackTrace();
        }
        return response;
    }

    @Override
    @Transactional
    public Response updateAuthUSer(long userId,Character active) {
        Response response = new Response();
        try {
            AuthUser user = authUserRepository.findByIdUser(userId);
            user.setActive(active);
            authUserRepository.save(user);
            response.setHttpStatus(HttpStatus.OK);
            response.setResponseMessage("Delete data Successfully");
        } catch (Exception e) {
            response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            response.setResponseMessage("Failed to Delete");
            e.printStackTrace();
        }
        return response;
    }

}
