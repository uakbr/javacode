/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openspaceservices.user.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import springfox.documentation.annotations.ApiIgnore;

/**
 * @author Vishal Manval
 */
@Controller
public class indexController {

    @ApiIgnore
    @RequestMapping(value = "/")
    public String index() {
        return "redirect:swagger-ui.html";
    }
}
