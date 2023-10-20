/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openspaceservices.user.app.dao.impl;

import com.openspaceservices.user.app.dao.UserDao;
import com.openspaceservices.user.app.model.Response;
import com.openspaceservices.user.app.model.UserDTO;
import com.openspaceservices.user.app.model.UserModel;
import com.openspaceservices.user.app.repository.UserModelRepository;
import com.openspaceservices.user.constants.Constants;
import com.openspaceservices.user.util.CommonUtil;
import java.sql.Timestamp;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.*;

/**
 * @author Vishal Manval <a href="https://www.openspaceservices.com">Openspace
 * Services Pvt. Ltd.</a>
 */
@Repository
@Log4j2
public class UserDaoImpl implements UserDao {

    private final UserModelRepository userModelRepository;

    @PersistenceContext
    EntityManager entityManager;

    public UserDaoImpl(UserModelRepository userModelRepository) {
        this.userModelRepository = userModelRepository;
    }

    @Override
    @Transactional
    public Response add(UserModel user) {
        Response response = new Response();
        try {
            UserModel userModelEmailExist = this.userModelRepository.findByUserEmail(user.getUserEmail());
             if (!CommonUtil.isNull(userModelEmailExist)) {
                response.setHttpStatus(HttpStatus.CONFLICT);
                response.setResponseCode(Constants.ERROR_CODE);
                response.setResponseType(Constants.ERROR);
                response.setResponseMessage("Email id already exist");
                
            } else {
                user = this.userModelRepository.save(user);
                response.setHttpStatus(HttpStatus.OK);
                response.setResponseMessage("User details added successfully");
                response.setData(user);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            response.setResponseMessage("Error while add user");
            response.setData(user);
            log.info("Exception in [UserDaoImpl] [add]:" + ex.getMessage());
            ex.printStackTrace();
        }
        return response;
    }

    @Override
    @Transactional
    public Response update(UserModel user) {
        Response response = new Response();
        try {
            UserModel userModelEmailExist = this.userModelRepository.findByUserEmailAndIdUserIsNot(user.getUserEmail(), user.getIdUser());
            if (!CommonUtil.isNull(userModelEmailExist)) {
                response.setResponseCode(Constants.ERROR_CODE);
                response.setResponseType(Constants.ERROR);
                response.setResponseMessage("Email id already exist");
            } else {
                user = this.userModelRepository.save(user);
                response.setHttpStatus(HttpStatus.OK);
                response.setResponseMessage("User details updated successfully");
                response.setData(user);
            }
        } catch (Exception ex) {
            response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            response.setResponseMessage("Error while update user");
            response.setData(user);
            log.info("Exception in [UserDaoImpl] [update]:" + ex.getMessage());
            ex.printStackTrace();
        }
        return response;
    }

    @Override    
    @Transactional
    public Response toggleStatus(Long userId, Character active) {
        Response response = new Response();
        try {
            this.userModelRepository.toggleUserStatus(userId, active);
            response.setHttpStatus(HttpStatus.OK);
            response.setResponseMessage("User status updated successfully");
            response.setData(userId);
        } catch (Exception ex) {
            response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            response.setResponseMessage("Error while update  status");
            log.info("Exception in [UserDaoImpl] [toggleStatus]:" + ex.getMessage());
            ex.printStackTrace();
        }
        return response;
    }

    @Override
    public Response getUser(Long userId) {
        Response response = new Response();
        try {
            Optional<UserModel> user = this.userModelRepository.findById(userId);
            if (user.isPresent()) {
                response.setHttpStatus(HttpStatus.OK);
                response.setData(user);
            } else {
                response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
                response.setResponseMessage("No user found with given id");
            }
        } catch (Exception ex) {
            response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            response.setResponseMessage("Error while fetch user");
            log.info("Exception in [UserDaoImpl] [getUser]:" + ex.getMessage());
            ex.printStackTrace();
        }
        return response;
    }

    @Override
    public List<UserDTO> searchUsers(UserDTO userDTO) {
        List<UserDTO> userDTOs = new ArrayList<>();
        StringBuilder query = new StringBuilder();
        query.append("select u.id_user,u.user_email,u.user_name,u.active,u.active_from,");
        query.append(" u.active_to,c.name as companyName,u.role_id,u.id_company ");
        query.append(" from optimeyesai_users u");
        query.append(" join optimeyesai_company c on c.id_company = u.id_company ");
        if ("ADMIN".equalsIgnoreCase(userDTO.getUserRole())) {
            query.append(" AND c.id_company =").append(userDTO.getIdCompany());
        }

        if (!CommonUtil.isNull(userDTO.getUserEmail())) {
            query.append(" AND u.user_email like :userMail");
        }

        if (!CommonUtil.isNull(userDTO.getUserName())) {
            query.append(" AND u.user_name like :userName");
        }

        if (!CommonUtil.isNull(userDTO.getIdCompany())) {
            query.append(" AND u.id_company = :companyId");
        }
        if (!CommonUtil.isNull(userDTO.getActive())) {
            query.append(" AND u.active like :activeFlag");
        }

        if (!CommonUtil.isNull(userDTO.getActiveFrom()) && CommonUtil.isNull(userDTO.getActiveTo())) {
            query.append(" AND u.active_from between :startDate AND :startDate");
        } else if (CommonUtil.isNull(userDTO.getActiveFrom()) && !CommonUtil.isNull(userDTO.getActiveTo())) {
            query.append(" AND u.active_to between :finishDate AND :finishDate");
        } else if (!CommonUtil.isNull(userDTO.getActiveFrom()) && !CommonUtil.isNull(userDTO.getActiveTo())) {
            query.append(" AND u.active_from between :startDate AND :startDate");
            query.append(" AND u.active_to between :finishDate AND :finishDate");
        }
        Query qry = entityManager.createNativeQuery(query.toString());

        if (!CommonUtil.isNullAndEmpty(userDTO.getUserEmail())) {
            qry.setParameter("userMail", (CommonUtil.isNullAndEmpty(userDTO.getUserEmail()) ? "%%" : "%" + userDTO.getUserEmail() + "%"));
        }
        if (!CommonUtil.isNullAndEmpty(userDTO.getUserName())) {
            qry.setParameter("userName", (CommonUtil.isNullAndEmpty(userDTO.getUserName()) ? "%%" : "%" + userDTO.getUserName() + "%"));
        }
        if (!CommonUtil.isNull(userDTO.getIdCompany())) {
            qry.setParameter("companyId", userDTO.getIdCompany());
        }
        if (!CommonUtil.isNull(userDTO.getActive())) {
            qry.setParameter("activeFlag", (CommonUtil.isNull(userDTO.getActive()) ? "%%" : "%" + userDTO.getActive() + "%"));
        }
        if (!CommonUtil.isNull(userDTO.getActiveFrom()) && CommonUtil.isNull(userDTO.getActiveTo())) {
            qry.setParameter("startDate", userDTO.getActiveFrom());
        } else if (CommonUtil.isNull(userDTO.getActiveFrom()) && !CommonUtil.isNull(userDTO.getActiveTo())) {
            qry.setParameter("finishDate", userDTO.getActiveTo());
        } else if (!CommonUtil.isNull(userDTO.getActiveFrom()) && !CommonUtil.isNull(userDTO.getActiveTo())) {
            qry.setParameter("startDate", userDTO.getActiveFrom());
            qry.setParameter("finishDate", userDTO.getActiveTo());
        }

        List<Object> list = qry.getResultList();
        if (CommonUtil.isListNotNullAndEmpty(list)) {
            Iterator it = list.iterator();
            while (it.hasNext()) {
                try {
                    Object[] objs = (Object[]) it.next();
                    UserDTO dto = new UserDTO();
                    dto.setIdUser(Long.valueOf((Integer) objs[0]));
                    dto.setUserEmail((String) objs[1]);
                    dto.setUserName((String) objs[2]);
                    dto.setActive((Character) objs[3]);
                    dto.setActiveFrom(CommonUtil.toTimeStamp(CommonUtil.toDateStringFormat((Date) objs[4], "yyyy-MM-dd HH:mm:ss"), "yyyy-MM-dd HH:mm:ss"));
                    dto.setActiveTo(CommonUtil.toTimeStamp(CommonUtil.toDateStringFormat((Date) objs[5], "yyyy-MM-dd HH:mm:ss"), "yyyy-MM-dd HH:mm:ss"));
                    dto.setCompanyName((String) objs[6]);
                    List listOfRole = entityManager.createNativeQuery("select ur.role_id,r.role_name from optimeyesai_user_role_map ur left join optimeyesai_rbac.optimeyesai_role r on ur.role_id=r.id_role where ur.user_id=" + dto.getIdUser()).getResultList();
                    Iterator itrRoles = listOfRole.iterator();
                    ArrayList<Integer> roleList = new ArrayList<>();
                    String roles = "";
                    while (itrRoles.hasNext()) {
                        Object[] objsRole = (Object[]) itrRoles.next();

                        roleList.add((Integer) objsRole[0]);

                        roles = roles + objsRole[1] + ", ";
                    }
                    if (!CommonUtil.isNullAndEmpty(roles)) {
                        roles = roles.substring(0, roles.lastIndexOf(","));
                    }
                    dto.setUserRole(roles);
                    dto.setRoleId(roleList);
                    dto.setIdCompany(Long.valueOf((Integer) objs[8]));
                    userDTOs.add(dto);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return userDTOs;
    }

    @Override

    public List<UserModel> getAllUsers(Long idCompany) {
        return userModelRepository.findAllByCompanyModelUserIdCompanyAndActive(idCompany, 'Y');
    }
    
    @Override
    public List<Map<Object,Object>> findAllUser() {
        return  userModelRepository.findAllUserModel();
    }

    public List<UserDTO> checkUser(UserDTO userDTO) {
        List<UserDTO> userDTOs = new ArrayList<>();
        StringBuilder query = new StringBuilder();
        query.append("select u.id_user,u.user_email,u.user_name,u.active,u.active_from,");
        query.append(" u.active_to,c.name as companyName,u.role_id,u.id_company ");
        query.append(" from optimeyesai_users u");
        query.append(" join optimeyesai_company c on c.id_company = u.id_company ");
        query.append(" where u.active = 'Y' AND ");
        if (!CommonUtil.isNull(userDTO.getUserEmail())) {
            query.append("(  lower(u.user_email)=:userMail");
        }

        if (!CommonUtil.isNull(userDTO.getUserName())) {
            query.append(" OR  lower(u.user_name) =:userName )");
        }
        Query qry = entityManager.createNativeQuery(query.toString());

        if (!CommonUtil.isNullAndEmpty(userDTO.getUserEmail())) {
            qry.setParameter("userMail", userDTO.getUserEmail().toLowerCase());
        }
        if (!CommonUtil.isNullAndEmpty(userDTO.getUserName())) {
            qry.setParameter("userName", userDTO.getUserName().toLowerCase());
        }
        List<Object> list = qry.getResultList();
        if (CommonUtil.isListNotNullAndEmpty(list)) {
            Iterator it = list.iterator();
            while (it.hasNext()) {
                try {
                    Object[] objs = (Object[]) it.next();
                    UserDTO dto = new UserDTO();
                    dto.setIdUser(Long.valueOf((Integer) objs[0]));
                    dto.setUserEmail((String) objs[1]);
                    dto.setUserName((String) objs[2]);
                    dto.setActive((Character) objs[3]);
                    dto.setActiveFrom(CommonUtil.toTimeStamp(CommonUtil.toDateStringFormat((Date) objs[4], "yyyy-MM-dd HH:mm:ss"), "yyyy-MM-dd HH:mm:ss"));
                    dto.setActiveTo(CommonUtil.toTimeStamp(CommonUtil.toDateStringFormat((Date) objs[5], "yyyy-MM-dd HH:mm:ss"), "yyyy-MM-dd HH:mm:ss"));
                    dto.setCompanyName((String) objs[6]);
                    List listOfRole = entityManager.createNativeQuery("select ur.role_id,r.role_name from optimeyesai_user_role_map ur left join optimeyesai_role r on ur.role_id=r.id_role where ur.user_id=" + dto.getIdUser()).getResultList();
                    Iterator itrRoles = listOfRole.iterator();
                    ArrayList<Integer> roleList = new ArrayList<>();
                    String roles = "";
                    while (itrRoles.hasNext()) {
                        Object[] objsRole = (Object[]) itrRoles.next();
                        roleList.add((Integer) objsRole[0]);
                        roles = roles + objsRole[1] + ", ";
                    }
                    if (!CommonUtil.isNullAndEmpty(roles)) {
                        roles = roles.substring(0, roles.lastIndexOf(","));
                    }
                    dto.setUserRole(roles);
                    dto.setRoleId(roleList);
                    dto.setIdCompany(Long.valueOf((Integer) objs[8]));
                    userDTOs.add(dto);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return userDTOs;
    }

    @Override
    public List<UserModel> getUserByRoleIds(Long companyId, List<Integer> roleIds) {
        return userModelRepository.findByCompanyModelUserIdCompanyAndActiveAndRoleModelSetIn(companyId, 'Y', roleIds);
    }

    @Override
    public Long findCompanyIdByUserId(Long userId) {
        Long companyId = Long.parseLong("0");
        try {
            companyId = userModelRepository.findCompanyIdByUserId(userId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return companyId;
    }

    @Override
    public int validateUser(Long userId) {
        int count = 0;
        try {
            String sqlQuery = "SELECT count(id_user) FROM optimeyesai_users WHERE date(active_from)<=curdate() AND date(active_to)>=curdate() AND id_user=" + userId;
            Query query = entityManager.createNativeQuery(sqlQuery);
            count = Integer.parseInt(query.getSingleResult().toString());
            System.out.println("sqlQuery:::"+sqlQuery);
                    
            if (count == 0) {
                UserModel user = userModelRepository.findByIdUser(userId);
                if (user != null) {
                    Timestamp currentTimestamp = CommonUtil.getStartDayTimestamp(
                            CommonUtil.toDateStringFormat(new Timestamp(new Date().getTime()), "yyyy-MM-dd "),
                            "yyyy-MM-dd");
                    Timestamp startTimestamp = CommonUtil.getStartDayTimestamp(
                            CommonUtil.toDateStringFormat(user.getActiveFrom(), "yyyy-MM-dd "),
                            "yyyy-MM-dd");

                    Timestamp endTimestamp = CommonUtil.getStartDayTimestamp(
                            CommonUtil.toDateStringFormat(user.getActiveTo(), "yyyy-MM-dd "),
                            "yyyy-MM-dd");
                    int t3 = startTimestamp.compareTo(currentTimestamp);
                    int t4 = currentTimestamp.compareTo(endTimestamp);

                    if (t3 > 0) {
                        count = -1;
                    }

                    if (t4 > 0) {
                        count = 0;
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("Exception caught in [UserDaoImpl]..[validateUser]..." + e.getMessage());
            e.printStackTrace();
        }
        return count;
    }
    
	@Override
	public Map<Object, Object> findUserNameAndEmail(Long idUser) {
		
		return userModelRepository.findUserNameAndEmail(idUser);
	}

	@Override
	public List<UserModel> getAllUserModel() {
		return (List<UserModel>) userModelRepository.findAll();
	}

}
