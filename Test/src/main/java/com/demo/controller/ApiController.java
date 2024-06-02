package com.demo.controller;

import java.net.URI;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/api")
public class ApiController {

    @Value("${api.service.key1}")
    private String serviceKey;
    
    private static final Logger logger = Logger.getLogger(ApiController.class.getName());

    @GetMapping("/data")
    public ResponseEntity<String> callApi() {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://apis.data.go.kr/B551011/GoCamping/locationBasedList";
        URI uri = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("serviceKey", serviceKey)
                .queryParam("numOfRows", 99) // 한페이지결과수
                .queryParam("pageNo", 1) // 페이지번호
                .queryParam("MobileOS", "ETC")
                .queryParam("MobileApp", "AppTest")
                .queryParam("mapX", 128.6142847) // 경도
                .queryParam("mapY", 36.0345423) // 위도
                .queryParam("radius", 2000) // 거리 반경
                .build()
                .toUri();

        try {
            ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                return response;
            } else {
                logger.severe("Failed to call API: " + response.getStatusCode());
                return new ResponseEntity<>("Failed to call API", response.getStatusCode());
            }
        } catch (RestClientException e) {
            logger.severe("Exception occurred while calling API: " + e.getMessage());
            return new ResponseEntity<>("Exception occurred while calling API: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
