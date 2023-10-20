/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openspaceservices.user.custom.annotations;

import com.google.gson.*;
import com.openspaceservices.user.apigateway.model.AuthUser;
import com.openspaceservices.user.constants.Constants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.joda.time.DateTime;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Type;
import java.util.Date;

/**
 * @author Vishal Manval
 * <a href="https://www.openspaceservices.com">Openspace Services Pvt. Ltd.</a>
 * This class is for getting login user details from the token When we include
 * @AuthUser annotation in controller method as a method signature
 */
public class WithUserHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        // We check if our parameter is exactly what we need:
        return parameter.hasParameterAnnotation(User.class)
                && parameter.getParameterType().equals(AuthUser.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws Exception {

        //take the request
        HttpServletRequest httpServletRequest = (HttpServletRequest) webRequest.getNativeRequest();
        //take our Authorization header from request and get access token
        AuthUser user = new AuthUser();
        try {
            String authorizationHeader = httpServletRequest.getHeader(Constants.AUTHERIZATION_HEADER);
            String token = authorizationHeader.replace("Bearer ", "");
            Claims claims = Jwts.parser()
                    .setSigningKey(Constants.SECRET)
                    .parseClaimsJws(token)
                    .getBody();

            final Date expirationDate = claims.getExpiration();
            //Deserialize the date
            final Gson gson = new GsonBuilder()
                    .registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                        public Date deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
                            return new Date(jsonElement.getAsJsonPrimitive().getAsLong());
                        }
                    })
                    .create();
            if (expirationDate.compareTo(generateCurrentDate()) > 0) {
                user = gson.fromJson(claims.getSubject(), AuthUser.class);
            } else {
                user.setIdUser(0L);
            }
        } catch (Exception ex) {
            System.out.print(ex.getMessage());
            ex.printStackTrace();
            user.setIdUser(0L);
        }
        return user;
    }

    private long getCurrentTimeMillis() {
        return DateTime.now().getMillis();
    }

    private Date generateCurrentDate() {
        return new Date(getCurrentTimeMillis());
    }
}
