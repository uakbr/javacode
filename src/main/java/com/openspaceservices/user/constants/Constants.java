package com.openspaceservices.user.constants;

public interface Constants {

    //Success codes
    public static final String SUCCESS_CODE = "200";
    public static final String SUCCESS = "SUCCESS";
    public static final String ERROR_CODE = "400";
    public static final String ERROR = "ERROR";
    public static final String NOT_FOUND_CODE = "404";
    public static final String NOT_FOUND = "NOT_FOUND";
    public static final String UNAUTHORIZED_CODE = "401";
    public static final String UNAUTHORIZED = "UNAUTHORIZED";
    public static final String INTERNAL_SERVER_ERROR_CODE = "500";
    public static final String INTERNAL_SERVER_ERROR = "INTERNAL_SERVER_ERROR";
    public static final String NOT_ACCEPTABLE_CODE = "406";
    public static final String NOT_ACCEPTABLE = "NOT_ACCEPTABLE";

    //JWT header and secret
    public static final String AUTHERIZATION_HEADER = "Authorization";
    public static final String SECRET = "jwtOptimeyesaiSecretKey";

}
