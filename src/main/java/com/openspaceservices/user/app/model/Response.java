package com.openspaceservices.user.app.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@Getter
@Setter
public class Response {

    private String responseCode;
    private String responseMessage;
    private String responseType;
    private List<String> errors;
    private Object data;
    private HttpStatus httpStatus;

    public void setHttpStatus(HttpStatus httpStatus) {
        this.responseCode = String.valueOf(httpStatus.value());
        this.httpStatus = httpStatus;
    }

}
