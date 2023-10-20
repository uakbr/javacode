/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openspaceservices.user.app.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.validation.ConstraintViolation;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.openspaceservices.user.apigateway.model.AuthUser;
import com.openspaceservices.user.app.dao.UserDao;
import com.openspaceservices.user.app.model.Response;
import com.openspaceservices.user.app.model.RoleModel;
import com.openspaceservices.user.app.model.UserDTO;
import com.openspaceservices.user.app.model.UserModel;
import com.openspaceservices.user.app.repository.UserModelRepository;
import com.openspaceservices.user.app.service.UserService;
import com.openspaceservices.user.custom.annotations.User;

/**
 * @author Vishal
 */
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    
    private UserModelRepository userModelRepository;
    
    @Autowired
    private UserDao userDao;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity add(@User AuthUser user, @Valid @RequestBody UserModel userModel, Errors errors) {
        Response response = new Response();
        if (errors.hasErrors()) {
            List<String> errorList = new ArrayList();
            List<ConstraintViolation<?>> violationsList = new ArrayList<>();
            for (ObjectError e : errors.getAllErrors()) {
                violationsList.add(e.unwrap(ConstraintViolation.class));
            }
            for (ConstraintViolation<?> violation : violationsList) {
                errorList.add(violation.getPropertyPath() + " " + violation.getMessage());
            }
            response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            response.setErrors(errorList);
        } else {
            response = this.userService.add(user, userModel);
        }
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @PutMapping
    public ResponseEntity update(@User AuthUser user, @Valid @RequestBody UserModel userModel, Errors errors) {
        Response response = new Response();
        if (errors.hasErrors()) {
            List<String> errorList = new ArrayList();
            List<ConstraintViolation<?>> violationsList = new ArrayList<>();
            for (ObjectError e : errors.getAllErrors()) {
                violationsList.add(e.unwrap(ConstraintViolation.class));
            }
            for (ConstraintViolation<?> violation : violationsList) {
                errorList.add(violation.getPropertyPath() + " " + violation.getMessage());
            }
            response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            response.setErrors(errorList);
        } else {
            response = this.userService.update(user, userModel);
        }
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @DeleteMapping("/{userId}/{status}")
    public ResponseEntity delete(@User AuthUser user, @PathVariable Long userId, @PathVariable Boolean status) {
        Response response = this.userService.toggleStatus(userId, status);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @PostMapping("/multiple")
    public ResponseEntity addMultipleUsers(@User AuthUser user, @RequestBody List<UserDTO> userDTOs, @RequestParam("appUrl") String appUrl) {
        Response response = this.userService.createUsers(user, userDTOs, appUrl);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @PostMapping("/searchUser")
    public ResponseEntity searchUser(@User AuthUser user, @RequestBody UserDTO userDTO) {
        for (RoleModel role : user.getRoleModelSet()) {
            if ("ADMIN".equalsIgnoreCase(role.getRoleName())) {
                userDTO.setUserRole(role.getRoleName());
                userDTO.setIdCompany(user.getCompanyId());
            }
        }
        Response response = this.userService.searchUsers(userDTO);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }


    @GetMapping
    public List<UserModel> getAllUsers(@User AuthUser user) {
//        Long id = Long.parseLong("1");
        return userService.getAllUsers(user.getCompanyId());
    }
    
    @GetMapping("/{idUser}")
    public UserModel findUserById(@PathVariable Long idUser) {
        return userService.findUserById(idUser);
    }

    @GetMapping("/{userId}/company")
    public Long getCompanyIdByUser(@PathVariable Long userId) {
        return userService.findCompanyIdByUserId(userId);
    }

    @GetMapping("/role")
    public List<UserModel> getUserByRoleIds(
            @RequestParam(
                    required = true,
                    name = "companyId"
            ) Long companyId,
            @RequestParam(
                    required = true,
                    name = "roleIds"
            ) List<Integer> roleIds) {
        return userService.getUserByRoleIds(companyId, roleIds);
    }

    @PostMapping("/checkUser")
    public ResponseEntity checkUser(@User AuthUser user, @RequestBody UserDTO userDTO) {
        for (RoleModel role : user.getRoleModelSet()) {
            if ("ADMIN".equalsIgnoreCase(role.getRoleName())) {
                userDTO.setUserRole(role.getRoleName());
                userDTO.setIdCompany(user.getCompanyId());
            }
        }
        Response response = this.userService.checkUser(userDTO);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @GetMapping("/searchResponce")
    public ResponseEntity<?> searchResponce(@User AuthUser user, @RequestParam("givenName") String givenName, @RequestParam(value = "department", required = false) String department) {
        return userService.searchResponce(givenName, department, user);
    }
 /**
     * Validate Company
     *
     * @param userId
     * @return
     */
    @GetMapping("/validate")
    public int validateUser(@RequestParam("userId") Long userId) {
        int count = 0;
        try {
            
            count = userService.validateUser(userId);
            return count;
        } catch (Exception e) {
//            log.info("Exception [SubscriptionController][validateCompany] : " + e.getMessage());
            e.getMessage();
            return 0;
        }
    }
    @GetMapping("/getAll")
	public List<Map<Object,Object>> getAllUser(){
    	
    	return userDao.findAllUser();
    	
    }
    
    @GetMapping("/getAllUserModel")
    public List<UserModel> getAllUserModel(){
	return userDao.getAllUserModel();
    	
    }
    
    @GetMapping("/getUserNameEmail/{idUser}")
	public Map<Object,Object> getUserNameAndEmail(@PathVariable Long idUser){
    	
    	return userDao.findUserNameAndEmail(idUser);
    	
    }
    
}
