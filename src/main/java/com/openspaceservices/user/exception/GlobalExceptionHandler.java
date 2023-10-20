/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openspaceservices.user.exception;

import com.openspaceservices.user.app.model.Response;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * @author Vishal
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
@Log4j2
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    //Exception handling for request param and path variable
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<?> handleMethodArgumentTypeMismatch(
            MethodArgumentTypeMismatchException ex, WebRequest request) {
        Response genericServiceResponse = new Response();
        String error = ex.getName() + " should be of type " + ex.getRequiredType().getName();
        genericServiceResponse.setResponseMessage(error);
        genericServiceResponse.setHttpStatus(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<Object>(
                genericServiceResponse, new HttpHeaders(), genericServiceResponse.getHttpStatus());
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        Response genericServiceResponse = new Response();
        genericServiceResponse.setResponseMessage(ex.getMessage());
        genericServiceResponse.setHttpStatus(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<Object>(
                genericServiceResponse, new HttpHeaders(), genericServiceResponse.getHttpStatus());
    }

    //Handling bad request body
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Response genericServiceResponse = new Response();
        genericServiceResponse.setResponseMessage(ex.getLocalizedMessage());
        genericServiceResponse.setHttpStatus(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<Object>(
                genericServiceResponse, new HttpHeaders(), genericServiceResponse.getHttpStatus());
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Response genericServiceResponse = new Response();
        genericServiceResponse.setResponseMessage(ex.getLocalizedMessage());
        genericServiceResponse.setHttpStatus(HttpStatus.METHOD_NOT_ALLOWED);
        return new ResponseEntity<Object>(
                genericServiceResponse, new HttpHeaders(), genericServiceResponse.getHttpStatus());
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Response genericServiceResponse = new Response();
        genericServiceResponse.setResponseMessage(ex.getLocalizedMessage());
        genericServiceResponse.setHttpStatus(HttpStatus.NOT_FOUND);
        return new ResponseEntity<Object>(
                genericServiceResponse, new HttpHeaders(), genericServiceResponse.getHttpStatus());
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex, HttpHeaders headers,
            HttpStatus status, WebRequest request) {
        Response genericServiceResponse = new Response();
        String error = ex.getParameterName() + " parameter is missing";
        genericServiceResponse.setResponseMessage(error);
        genericServiceResponse.setHttpStatus(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<Object>(
                genericServiceResponse, new HttpHeaders(), genericServiceResponse.getHttpStatus());
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Response response = new Response();
        response.setHttpStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE);
        response.setResponseMessage("Media Type Not Supported");
        return handleExceptionInternal(ex, response, headers, status, request);
    }

}
