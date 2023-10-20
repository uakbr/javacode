package com.openspaceservices.user.util;

import com.openspaceservices.user.apigateway.model.AuthUser;
import com.openspaceservices.user.app.model.Mail;
import com.openspaceservices.user.app.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * @author Vishal Manval working in
 * <a href="https://www.openspaceservices.com">Openspace Services Pvt. Ltd.</a>
 */
@Component
public class SendMailService {

    @Autowired
    WebClient.Builder webClientBuilder;

    @Async
    public Boolean sendSetPasswordMail(AuthUser user, String url) {
        Mail mail = new Mail();
        HashMap<String, Object> mapModel = new HashMap<>();
        try {
            Thread mailthread = new Thread(
                    () -> {
                        mapModel.put("name", user.getUserName());
                        mapModel.put("url", url);
                        mapModel.put("header", "Set Password");
                        mapModel.put("footer", "OptymeEyes");
                        mapModel.put("buttonName", "Set Password");
                        mail.setSubject("Set Password")
                                .setRecipient(user.getUserEmail())
                                .setModel(mapModel)
                                .setTemplateName("set-password.ftl");

                        webClientBuilder.build()
                                .post()
                                .uri("http://email-service/mail/sendMailWithTemplate")
                                .body(Mono.just((mail)), Response.class)
                                .retrieve()
                                .bodyToMono(String.class)
                                .block();

                    });
            mailthread.start();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
     
    @Async
    public Boolean sendLonginMailForADUser(AuthUser user, String url) {
        Mail mail = new Mail();
        HashMap<String, Object> mapModel = new HashMap<>();
        try {
            Thread mailthread = new Thread(
                    () -> {
                        mapModel.put("name", user.getUserName());
                        mapModel.put("url", url);
                        mapModel.put("header", "Registered");
                        mapModel.put("footer", "OptymeEyes");
                        mapModel.put("buttonName", "Log In");
                        mail.setSubject("Registartion Completed")
                                .setRecipient(user.getUserEmail())
                                .setModel(mapModel)
                                .setTemplateName("login-ad-ldap.ftl");

                        webClientBuilder.build()
                                .post()
                                .uri("http://email-service/mail/sendMailWithTemplate")
                                .body(Mono.just((mail)), Response.class)
                                .retrieve()
                                .bodyToMono(String.class)
                                .block();

                    });
            mailthread.start();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
