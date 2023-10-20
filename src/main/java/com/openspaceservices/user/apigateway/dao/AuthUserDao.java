/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openspaceservices.user.apigateway.dao;

import com.openspaceservices.user.apigateway.model.AuthUser;
import com.openspaceservices.user.app.model.Response;

/**
 * @author nehakasar
 */
public interface AuthUserDao {

    public Response saveAuthUSer(AuthUser user);

    public Response findUserByUserName(String userName);

    public Response toggleUser(long userId, Character active);

    public Response updateAuthUSer(long userId,Character active);

}
