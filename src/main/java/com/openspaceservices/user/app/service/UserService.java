/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openspaceservices.user.app.service;

import com.openspaceservices.user.apigateway.model.AuthUser;
import com.openspaceservices.user.app.model.Response;
import com.openspaceservices.user.app.model.UserDTO;
import com.openspaceservices.user.app.model.UserModel;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * @author Vishal Manval <a href="https://www.openspaceservices.com">Openspace
 * Services Pvt. Ltd.</a>
 */
public interface UserService {

    public Response add(AuthUser authUser, UserModel user);

    public Response update(AuthUser authUser, UserModel user);

    public Response toggleStatus(Long userId, Boolean status);

    public Response createUsers(AuthUser authUser, List<UserDTO> userDTOs, String url);

    public Response searchUsers(UserDTO userDTO);

    public Response findUserByUserName(String userName);

    public Response checkUser(UserDTO userDTO);

    public List<UserModel> getAllUsers(Long idCompany);

    public Long findCompanyIdByUserId(Long userId);

    public ResponseEntity<?> searchResponce(String givenName, String department, AuthUser user);

    public ResponseEntity<?> getAdLDapDetails(long companyId);

    List<UserModel> getUserByRoleIds(Long companyId, List<Integer> roleIds);

    public int validateUser(Long userId);

    public UserModel findUserById(Long idUser);
}
