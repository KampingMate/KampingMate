package com.demo.service.social;

import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class NaverOauth implements SocialOauth {
    @Override
    public String getOauthRedirectURL() {
        return "";
    }

    @Override
    public Map<String, String> requestAccessToken(String code) {
        return null;
    }
}
