package com.demo.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.demo.helper.constants.SocialLoginType;
import com.demo.model.GoogleUser;
import com.demo.service.social.SocialOauth;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OauthService {
    private final List<SocialOauth> socialOauthList;
    private final RestTemplate restTemplate;

    @Value("${sns.google.userinfo.url}")
    private String googleUserinfoUrl;

    @Value("${sns.google.client.id}")
    private String clientId;

    @Value("${sns.google.client.secret}")
    private String clientSecret;

    @Value("${sns.google.token.url}")
    private String tokenUrl;

    public String request(SocialLoginType socialLoginType, HttpServletResponse response) {
        SocialOauth socialOauth = this.findSocialOauthByType(socialLoginType);
        String redirectURL = socialOauth.getOauthRedirectURL();
        try {
            response.sendRedirect(redirectURL);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Map<String, String> requestAccessToken(SocialLoginType socialLoginType, String code) {
        SocialOauth socialOauth = this.findSocialOauthByType(socialLoginType);
        return socialOauth.requestAccessToken(code);
    }

    private SocialOauth findSocialOauthByType(SocialLoginType socialLoginType) {
        return socialOauthList.stream()
                .filter(x -> x.type() == socialLoginType)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("알 수 없는 SocialLoginType 입니다."));
    }

    public GoogleUser getGoogleUserInfo(String accessToken) {
        String url = googleUserinfoUrl + "?access_token=" + accessToken;
        return restTemplate.getForObject(url, GoogleUser.class);
    }

    // 액세스 토큰 갱신 메서드 추가
    public String refreshAccessToken(String refreshToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        body.add("refresh_token", refreshToken);
        body.add("grant_type", "refresh_token");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(tokenUrl, request, Map.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            Map<String, String> responseBody = response.getBody();
            return responseBody.get("access_token");
        } else {
            throw new RuntimeException("Failed to refresh access token");
        }
    }

    // 로그인한 사용자를 가져오는 메서드를 정의합니다.
    public GoogleUser getLoggedInUser() {
        // 로그인한 사용자를 가져오는 로직을 구현합니다.
        // 이는 데이터베이스에서 사용자 정보를 가져오거나 다른 서비스에서 정보를 가져오는 등의 작업을 수행할 수 있습니다.
        // 여기서는 간단한 예제로 더미 사용자를 반환합니다.
        GoogleUser user = new GoogleUser();
        user.setId("123456");
        user.setEmail("example@gmail.com");
        user.setName("John Doe");
        // 사용자의 다른 속성을 필요에 따라 설정합니다.

        return user;
    }
}
