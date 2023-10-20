/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openspaceservices.user.app.repository;

import com.openspaceservices.user.app.model.UserModel;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

/**
 * @author Vishal Manval <a href="https://www.openspaceservices.com">Openspace
 * Services Pvt. Ltd.</a>
 */
public interface UserModelRepository extends CrudRepository<UserModel, Long> {

    public UserModel findByUserEmail(String email);

    public UserModel findByIdUser(long userId);

    public UserModel findByUserEmailAndIdUserIsNot(String email, Long id);

    @Query(value = "SELECT id_company FROM optimeyesai_users where id_user=:idUser", nativeQuery = true)
    public Long findCompanyIdByUserId(Long idUser);

    @Transactional
    @Modifying
    @Query("UPDATE UserModel  SET active =:active WHERE idUser=:id")
    int toggleUserStatus(@Param("id") Long id, @Param("active") Character active);

    public List<UserModel> findAllByCompanyModelUserIdCompanyAndActive(Long idCompany, Character active);

    List<UserModel> findByCompanyModelUserIdCompanyAndActiveAndRoleModelSetIn(Long idCompany, Character active, List<Integer> ids);

    @Query(value = "SELECT id_user as userId,user_name as userName FROM optimeyesai_users", nativeQuery = true)
	public List<Map<Object, Object>> findAllUserModel();

    @Query(value = "SELECT id_user as userId,user_name as userName,user_email as userEmail FROM optimeyesai_users where id_user=:idUser", nativeQuery = true)
    public Map<Object, Object> findUserNameAndEmail(Long idUser);

}
