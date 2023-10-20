/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openspaceservices.user.apigateway.repository;

import com.openspaceservices.user.apigateway.model.AuthUser;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author nehakasar
 */
public interface AuthUserRepository extends CrudRepository<AuthUser, Long> {

    AuthUser findByUserName(String name);

    AuthUser findByIdUser(long userId);

    @Modifying
    @Transactional
    @Query("UPDATE AuthUser  SET active =:active WHERE idUser=:id")
    int toggleUserStatus(@Param("id") Long id, @Param("active") Character active);

}
