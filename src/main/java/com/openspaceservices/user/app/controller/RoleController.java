/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openspaceservices.user.app.controller;

import com.openspaceservices.user.apigateway.model.AuthUser;
import com.openspaceservices.user.app.model.Response;
import com.openspaceservices.user.app.model.RoleDTO;
import com.openspaceservices.user.app.model.RoleModel;
import com.openspaceservices.user.app.service.RoleService;
import com.openspaceservices.user.app.service.UserService;
import com.openspaceservices.user.custom.annotations.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Vishal Manval <a href="https://www.openspaceservices.com">Openspace
 * Services Pvt. Ltd.</a>
 */
@RestController
@RequestMapping("/roles")
public class RoleController {

    private final RoleService roleService;
    private final UserService userService;

    @Autowired
    public RoleController(RoleService roleService, UserService userService) {
        this.roleService = roleService;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity add(@User AuthUser user, @Valid @RequestBody RoleModel roleModel, Errors errors) {
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
            response = this.roleService.add(user, roleModel);
        }
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @PutMapping
    public ResponseEntity update(@User AuthUser user, @Valid @RequestBody RoleModel roleModel, Errors errors) {
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
            response = this.roleService.update(user, roleModel);
        }
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @DeleteMapping("/{roleId}/{status}")
    public ResponseEntity delete(@User AuthUser user, @PathVariable Integer roleId, @PathVariable Boolean status) {
        Response response = this.roleService.toggleStatus(roleId, status);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @PostMapping("/multiple")
    public ResponseEntity addMultipleRoles(@User AuthUser user, @RequestBody List<RoleDTO> roleDTOs) {
        Response response = this.roleService.createRoles(user, roleDTOs);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @GetMapping
    public ResponseEntity getAllRoles(@User AuthUser user) {
        Response responseObj = roleService.findByCompanyIDandActiveFlag(user.getCompanyId(), 'Y');
        return new ResponseEntity<>(responseObj, responseObj.getHttpStatus());
    }

    @GetMapping("/module/{moduleName}/{functionName}")
    public Response findFunctionAccessByModuleAndRoleId(@User AuthUser user, @PathVariable String moduleName, @PathVariable String functionName) {
        List<Integer> roleIdList = new ArrayList<>();
        user.getRoleModelSet().forEach((role) -> roleIdList.add(role.getIdRole()));
        return roleService.findFunctionAccessByModuleAndRoleId(roleIdList, moduleName, functionName, user.getCompanyId());
    }

}
