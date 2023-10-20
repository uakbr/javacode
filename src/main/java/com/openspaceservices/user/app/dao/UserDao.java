/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openspaceservices.user.app.dao;

import com.openspaceservices.user.app.model.Response;
import com.openspaceservices.user.app.model.UserDTO;
import com.openspaceservices.user.app.model.UserModel;

import java.util.List;
import java.util.Map;

/**
 * @author Vishal Manval <a href="https://www.openspaceservices.com">Openspace
 * Services Pvt. Ltd.</a>
 */
public interface UserDao {

    public Response add(UserModel user);

    public Response update(UserModel user);

    public Response toggleStatus(Long userId, Character active);

    public Response getUser(Long userId);

    public Long findCompanyIdByUserId(Long userId);

    public List<UserDTO> searchUsers(UserDTO userDTO);

    public List<UserModel> getAllUsers(Long idCompany);

    public List<UserDTO> checkUser(UserDTO userDTO);

    List<UserModel> getUserByRoleIds(Long companyId, List<Integer> roleIds);

    public int validateUser(Long userId);

    public List<Map<Object, Object>> findAllUser();
    
    public Map<Object, Object> findUserNameAndEmail(Long idUser);

	public List<UserModel> getAllUserModel();
    
}
