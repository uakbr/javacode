/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openspaceservices.user.app.model;

import lombok.Getter;

import java.util.List;
import java.util.Map;

/**
 * @author Vishal Manval working in
 * <a href="https://www.openspaceservices.com">Openspace Services Pvt. Ltd.</a>
 */
@Getter
public class Mail {

    private String subject;

    private String message;

    private String recipient;

    private String ccRecipient;

    private String bccRecipient;

    private List<String> attachments;

    private Map<String, Object> model;

    private String templateName;

    public Mail setSubject(String subject) {
        this.subject = subject;
        return this;
    }

    public Mail setMessage(String message) {
        this.message = message;
        return this;
    }

    public Mail setRecipient(String recipient) {
        this.recipient = recipient;
        return this;
    }

    public Mail setCcRecipient(String ccRecipient) {
        this.ccRecipient = ccRecipient;
        return this;
    }

    public Mail setBccRecipient(String bccRecipient) {
        this.bccRecipient = bccRecipient;
        return this;
    }

    public Mail setAttachments(List<String> attachments) {
        this.attachments = attachments;
        return this;
    }

    public Mail setModel(Map<String, Object> model) {
        this.model = model;
        return this;
    }

    public Mail setTemplateName(String templateName) {
        this.templateName = templateName;
        return this;
    }

}
