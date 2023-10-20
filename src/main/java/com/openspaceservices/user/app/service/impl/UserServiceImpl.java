/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openspaceservices.user.app.service.impl;

import com.openspaceservices.user.apigateway.dao.AuthUserDao;
import com.openspaceservices.user.apigateway.model.AuthUser;
import com.openspaceservices.user.app.dao.CompanyDao;
import com.openspaceservices.user.app.dao.UserDao;
import com.openspaceservices.user.app.model.*;
import com.openspaceservices.user.app.repository.CompanyRepository;
import com.openspaceservices.user.app.service.UserService;
import com.openspaceservices.user.constants.Constants;
import com.openspaceservices.user.util.CommonUtil;
import com.openspaceservices.user.util.JwtProvider;
import com.openspaceservices.user.util.SendMailService;
import lombok.extern.log4j.Log4j2;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import javax.transaction.Transactional;
import java.util.*;

/**
 * @author Vishal Manval <a href="https://www.openspaceservices.com">Openspace
 * Services Pvt. Ltd.</a>
 */
@Service
@Log4j2
public class UserServiceImpl implements UserService {

    @Autowired
    WebClient.Builder webClientBuilder;

    private final UserDao userDao;
    private final AuthUserDao authUserDao;
    private final CompanyDao companyDao;
    private final SendMailService sendMailService;
    private final CompanyRepository companyRepository;
    private final JwtProvider jwtProvider;

    public UserServiceImpl(UserDao userDao, AuthUserDao authUserDao, CompanyDao companyDao, SendMailService sendMailService, CompanyRepository companyRepository, JwtProvider jwtProvider) {
        this.userDao = userDao;
        this.authUserDao = authUserDao;
        this.companyDao = companyDao;
        this.sendMailService = sendMailService;
        this.companyRepository = companyRepository;
        this.jwtProvider = jwtProvider;
    }

    @Override
    public Response add(AuthUser authUser, UserModel user) {
        Response response = new Response();
        try {
            user.setCreatedBy(authUser.getIdUser());
            user.setPassword(CommonUtil.encryptPassword(user.getPassword()));
            response = this.userDao.add(user);
            if (HttpStatus.OK.equals(response.getHttpStatus())) {
                AuthUser authUsr = new AuthUser();
                authUsr.setUserEmail(user.getUserEmail());
                authUsr.setUserName(user.getUserName());
                authUsr.setActive('Y');
                authUsr.setActiveFrom(user.getActiveFrom());
                authUsr.setActiveTo(user.getActiveTo());
                authUsr.setIdUser(user.getIdUser());
                response = authUserDao.saveAuthUSer(authUsr);
                if (!HttpStatus.OK.equals(response.getHttpStatus())) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setResponseCode(Constants.ERROR_CODE);
            response.setResponseType(Constants.ERROR);
            response.setResponseMessage("Failed to add user");
        }
        return response;
    }

    @Override
    public Response toggleStatus(Long userId, Boolean status) {
        Response response = new Response();
        try {
            Character active = status ? 'Y' : 'N';
            response = this.userDao.toggleStatus(userId, active);
            System.out.println("response.getHttpStatus()::" + response.getHttpStatus());
            if (HttpStatus.OK.equals(response.getHttpStatus())) {
                response = authUserDao.toggleUser(userId, active);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setResponseCode(Constants.ERROR_CODE);
            response.setResponseType(Constants.ERROR);
            response.setResponseMessage("Failed to update status");
        }
        return response;
    }

    @Override
    public Response update(AuthUser authUser, UserModel user) {
        Response response = new Response();
        try {
            user.setCreatedBy(authUser.getIdUser());
            user.setRoleModelSet(user.getRoleId());
            CompanyModel companyModel = this.companyDao.findByCompanyId(user.getIdCompany());
            user.setCompanyModelUser(companyModel);
            response = this.userDao.update(user);
            authUserDao.updateAuthUSer(user.getIdUser(), user.getActive());
        } catch (Exception e) {
            e.printStackTrace();
            response.setResponseCode(Constants.ERROR_CODE);
            response.setResponseType(Constants.ERROR);
            response.setResponseMessage("Failed to add user");
        }
        return response;
    }

    @Override
    @Transactional
    public Response createUsers(AuthUser authUser, List<UserDTO> userDTOs, String url) {
        Response response = new Response();
        boolean createFlag = false;
        boolean allowCreate = false;
        try {
            for (UserDTO dto : userDTOs) {
                Response response123;
                UserModel userModel;
                AuthUser authUsr = new AuthUser();
                if (!CommonUtil.isNullAndEmpty(dto.getUserEmail()) && !CommonUtil.isNullAndEmpty(dto.getUserName())
                        && CommonUtil.isListNotNullAndEmpty(dto.getRoleId()) && !CommonUtil.isNull(dto.getIdCompany())
                        && !CommonUtil.isNull(dto.getActive()) && !CommonUtil.isNull(dto.getActiveFrom()) && !CommonUtil.isNull(dto.getActiveTo())) {
//                    response = this.checkUser(dto);

//                    if (!response.getResponseType().equals(Constants.SUCCESS)) {
//                        return response;
//                    }
                    userModel = CommonUtil.copyBeanProperties(dto, UserModel.class);

                    HashSet<Integer> roleModelSet = new HashSet<>();
                    for (Object o : dto.getRoleId()) {
                        RoleModel roleModel = webClientBuilder.build()
                                .get()
                                .uri("http://rbac-service/roles/" + o + "/role")
                                .retrieve()
                                .bodyToMono(RoleModel.class)
                                .block();
                        roleModelSet.add(roleModel.getIdRole());
                    }

                    // Check Active From for mandatory
                    if (CommonUtil.isNull(userModel.getActiveFrom())) {
                        response.setResponseCode(Constants.ERROR_CODE);
                        response.setResponseType(Constants.ERROR);
                        response.setResponseMessage("Active From is mandatory");
                        response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
                        return response;
                    }
                    // Check To From for mandatory
                    if (CommonUtil.isNull(userModel.getActiveTo())) {
                        response.setResponseCode(Constants.ERROR_CODE);
                        response.setResponseType(Constants.ERROR);
                        response.setResponseMessage("Active To is mandatory");
                        response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
                        return response;
                    }
                    // Check Company is active
                    CompanyModel companyModel = companyDao.findByCompanyId(dto.getIdCompany());
                    if (!companyModel.getActive().equals('Y')) {
                        response.setResponseCode(Constants.ERROR_CODE);
                        response.setResponseType(Constants.ERROR);
                        response.setResponseMessage("Selected company not in active state");
                        response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
                        return response;
                    }
                    userModel.setCompanyModelUser(companyModel);
                    userModel.setRoleModelSet(roleModelSet);
                    userModel.setCreatedBy(authUser.getIdUser());
                    userModel.setUpdatedBy(authUser.getIdUser());
                    userModel.setTxTimestamp(CommonUtil.toTimeStamp(CommonUtil.toDateStringFormat(new Date(), "yyyy-MM-dd HH:mm:ss"), "yyyy-MM-dd HH:mm:ss"));
                    // check if company has active to then user active to
                    // becomes mandatory
                    if (!CommonUtil.isNull(companyModel.getActiveFrom())
                            && !CommonUtil.isNull(companyModel.getActiveTo())) {
                        if (CommonUtil.isNull(userModel.getActiveTo())) {
                            response.setResponseCode(Constants.ERROR_CODE);
                            response.setResponseType(Constants.ERROR);
                            response.setResponseMessage("Active To is mandatory as Company has Active To");
                            response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
                            return response;
                        }
                        if (!CommonUtil.isNull(userModel.getActiveTo()) && !CommonUtil.isNull(userModel.getActiveFrom())
                                && userModel.getActiveTo().before(userModel.getActiveFrom())) {
                            response.setResponseCode(Constants.ERROR_CODE);
                            response.setResponseType(Constants.ERROR);
                            response.setResponseMessage("Active To is before Active From");
                            response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
                            return response;

                        }
                        // check if active from is within company active dates
                        if (!CommonUtil.isNull(userModel.getActiveFrom())
                                && userModel.getActiveFrom().before(companyModel.getActiveFrom())
                                || userModel.getActiveFrom().after(companyModel.getActiveTo())) {
                            response.setResponseCode(Constants.ERROR_CODE);
                            response.setResponseType(Constants.ERROR);
                            response.setResponseMessage("User Active From should be within Company Active From");
                            response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
                            return response;
                        }
                        // check if active to is within company active dates
                        if (!CommonUtil.isNull(userModel.getActiveTo())
                                && userModel.getActiveTo().before(companyModel.getActiveFrom())
                                || userModel.getActiveTo().after(companyModel.getActiveTo())) {
                            response.setResponseCode(Constants.ERROR_CODE);
                            response.setResponseType(Constants.ERROR);
                            response.setResponseMessage("User Active To should be within Company Active To");
                            response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
                            return response;
                        }
                    } else {
                        if (!CommonUtil.isNull(userModel.getActiveTo()) && !CommonUtil.isNull(userModel.getActiveFrom())
                                && userModel.getActiveTo().before(userModel.getActiveFrom())) {
                            response.setResponseCode(Constants.ERROR_CODE);
                            response.setResponseType(Constants.ERROR);
                            response.setResponseMessage("Active To is before Active From");
                            response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
                            return response;
                        }
                        if (!CommonUtil.isNull(userModel.getActiveFrom())
                                && userModel.getActiveFrom().before(companyModel.getActiveFrom())) {
                            response.setResponseCode(Constants.ERROR_CODE);
                            response.setResponseType(Constants.ERROR);
                            response.setResponseMessage("User Active From should be within Company Active From");
                            response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
                            return response;
                        }
                        // check if active to is within company active dates
                        if (!CommonUtil.isNull(userModel.getActiveTo())
                                && userModel.getActiveTo().before(companyModel.getActiveFrom())) {
                            response.setResponseCode(Constants.ERROR_CODE);
                            response.setResponseType(Constants.ERROR);
                            response.setResponseMessage("User Active To should be within Company Active To");
                            response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
                            return response;
                        }
                    }
                    createFlag = true;
                    userModel.setCreatedBy(authUser.getIdUser());
                    response123 = userDao.add(userModel);
                    if (HttpStatus.OK.equals(response123.getHttpStatus())) {
                        // Set Auth User Properties
                        UserModel userModelSaved = (UserModel) response123.getData();

                        authUsr.setUserName(userModelSaved.getUserName());
                        authUsr.setUserEmail(userModelSaved.getUserEmail());
                        authUsr.setActiveFrom(userModelSaved.getActiveFrom());
                        authUsr.setActiveTo(userModelSaved.getActiveTo());
                        authUsr.setIdUser(userModelSaved.getIdUser());
                        authUsr.setCompanyId(companyModel.getIdCompany());
                        String token = jwtProvider.generateToken(authUsr);
                        authUsr.setAuthToken(token);
                        authUsr.setActive(dto.getActive());
                        response = authUserDao.saveAuthUSer(authUsr);
                        if (HttpStatus.OK.equals(response.getHttpStatus())) {
                            //For Sending mail to user On Add...............
                            if (companyModel.getAuthenticationType() == 2) {
                                Boolean result = this.sendMailService.sendLonginMailForADUser(authUsr, url + "#/login");
                            } else {
                                Boolean result = this.sendMailService.sendSetPasswordMail(authUsr, url + "#/forgot?token=" + token);
                            }
                        } else {
                            TransactionAspectSupport.currentTransactionStatus().isRollbackOnly();
                            return response;
                        }
                    } else {
//                        TransactionAspectSupport.currentTransactionStatus().isRollbackOnly();
                        response.setResponseCode(Constants.ERROR_CODE);
                        response.setResponseType(Constants.ERROR);
                        response.setResponseMessage(response123.getResponseMessage());
                        response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
                        return response;
                    }
                }
            }
        } catch (Exception e) {
//            TransactionAspectSupport.currentTransactionStatus().isRollbackOnly();
            e.printStackTrace();
            response.setResponseType(Constants.ERROR);
            response.setResponseMessage(e.getMessage());
            response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            response.setResponseMessage(e.getMessage());
            return response;
        }
        return response;
    }

    @Override
    public Response searchUsers(UserDTO userDTO) {
        Response response = new Response();
        try {
            List<UserDTO> userDTOs = this.userDao.searchUsers(userDTO);
            response.setHttpStatus(HttpStatus.OK);
            response.setData(userDTOs);
        } catch (Exception ex) {
            ex.printStackTrace();
            response.setResponseType(Constants.ERROR);
            response.setResponseMessage(ex.getMessage());
            response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            response.setResponseMessage(ex.getMessage());
        }
        return response;
    }

    @Override
    public Response findUserByUserName(String userName) {
        Response response = authUserDao.findUserByUserName(userName);
        return response;
    }

    @Override
    public List<UserModel> getAllUsers(Long idCompany) {
        return userDao.getAllUsers(idCompany);
    }

    public Response checkUser(UserDTO userDTO) {
        Response response = new Response();
        try {
            List<UserDTO> userDTOs = this.userDao.checkUser(userDTO);
            response.setHttpStatus(HttpStatus.OK);
            userDTOs.forEach(user -> {
                if (user.getUserEmail().equalsIgnoreCase(userDTO.getUserEmail())) {
                    response.setResponseMessage("Email ID " + userDTO.getUserEmail() + " already exists");
                    response.setResponseType(Constants.ERROR);
                } else if (user.getUserName().equalsIgnoreCase(userDTO.getUserName())) {
                    response.setResponseMessage("User Name " + userDTO.getUserName() + " already exists");
                    response.setResponseType(Constants.ERROR);
                }
            });
            if (userDTOs.isEmpty()) {
                response.setResponseType(Constants.SUCCESS);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            response.setResponseType(Constants.ERROR);
            response.setResponseMessage(ex.getMessage());
            response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            response.setResponseMessage(ex.getMessage());
        }
        return response;
    }

    @Override
    public Long findCompanyIdByUserId(Long userId) {
        return userDao.findCompanyIdByUserId(userId);
    }

    @Override
    public ResponseEntity<?> searchResponce(String givenName, String department, AuthUser user) {
        Response response = new Response();
        try {
            String url = "";
            String server = "";
            String password = "";
//          String url = "http://34.214.146.234:5000";
//          String server = "ldap://SERVER01.optimeyes.local";
//          System.out.println("companyId >> "+companyId);
            Optional<CompanyModel> optional = companyRepository.findById(user.getCompanyId());
            if (optional.isPresent()) {
                url = optional.get().getActivityDirectoryUrl();
                server = optional.get().getActivityDirectoryServer();
                password = optional.get().getActivityDirectoryPassword();
            }
            List<SearchResponce> responces = new ArrayList<>();
            department = department != null ? department : "";

//            RestTemplate ldapAuthRestTemplates = new RestTemplate();
//            HashMap map = new HashMap();
//            map.put("username", user.getUserEmail());
//            map.put("password", password);
//            String authResult = ldapAuthRestTemplates.postForObject(
//                    url + "/authenticate?server=" + server, map, String.class);
//            System.out.println(" ## authResult: " + authResult);
            String token = "3fz99AIL8aY19x1yr4jTiIib3fLfSM7DvACeJE2YhdbSoEqgT7SI3lJVWCr5kltTbPbOlYfKlYHYMemvOo8Nbg";
            UserSearch search = new UserSearch();
            search.setToken(token);
            search.setDc1("Birdsource");
            search.setDc2("com");
            RestTemplate restTemplates = new RestTemplate();
//            System.out.println("url >>> "+url);
            String result = restTemplates.postForObject(
                    url + "/search?givenName=" + givenName + "&OU=&department=" + department + "&company=&location=&server=" + server, search, String.class);

//            System.out.println("## result " + result);
            JSONArray jsonDataResponseList = new JSONArray(result);
            for (int i = 0, size = jsonDataResponseList.length(); i < size; i++) {
                JSONObject objectInArray = jsonDataResponseList.getJSONObject(i);
                SearchResponce searchResponce = new SearchResponce();
                searchResponce.setGivenName(objectInArray.getString("givenName"));
                searchResponce.setUserPrincipalName(objectInArray.getString("userPrincipalName"));
                searchResponce.setDepartment(objectInArray.getString("department"));
                responces.add(searchResponce);
            }
            response.setData(responces);
        } catch (Exception e) {
            e.getMessage();
            e.printStackTrace();
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getAdLDapDetails(long companyId) {
        Response response = new Response();
        try {
            Optional<CompanyModel> Optional = companyRepository.findById(companyId);
            response.setData(Optional);
        } catch (Exception e) {
            e.printStackTrace();
            response.setResponseType(Constants.ERROR);
            response.setResponseMessage(e.getMessage());
            response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    @Override
    public List<UserModel> getUserByRoleIds(Long companyId, List<Integer> roleIds) {
        return userDao.getUserByRoleIds(companyId, roleIds);
    }

    @Override
    public int validateUser(Long userId) {
        return userDao.validateUser(userId);
    }

    @Override
    public UserModel findUserById(Long idUser) {
        Response response = new Response();
        UserModel userModel = new UserModel();
        try {
            Response rs = userDao.getUser(idUser);
            Optional<UserModel> user = (Optional<UserModel>) rs.getData();
            userModel = user.get();
//            response.setData(Optional);
        } catch (Exception e) {
            e.printStackTrace();
            response.setResponseType(Constants.ERROR);
            response.setResponseMessage(e.getMessage());
            response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return userModel;
//        return new ResponseEntity<Response>(response, HttpStatus.OK);

    }

}
