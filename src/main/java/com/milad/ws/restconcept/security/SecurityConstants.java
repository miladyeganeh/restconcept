package com.milad.ws.restconcept.security;

import com.milad.ws.restconcept.SpringApplicationContext;

public class SecurityConstants {
    public static final Long EXPIRATIOM_TIME = 864000000L; //10 days
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String SING_UP_URL = "/users";

    public static String getTokenSecret(){
        AppProperties appProperties = (AppProperties) SpringApplicationContext.getBean("appProperties");
        return appProperties.getTokenSecret();
    }
}
