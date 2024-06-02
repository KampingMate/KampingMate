package com.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {

	@GetMapping("/")
    public String index() {
        return "index"; // index.html 파일을 반환합니다.
    }

    @GetMapping("/main")
    public String main(Model model, HttpSession session) {
//    	Member loginUser = (Member)session.getAttribute("loginUser");
//    	model.addAttribute("name", loginUser.getName());
    	
    	 // API를 호출하여 데이터를 가져옵니다.
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:8090/api/data", String.class);
        String data = response.getBody();
        
        // 가져온 데이터를 모델에 추가합니다.
        model.addAttribute("data", data);
        return "main"; // main.html 파일을 반환합니다.
    }
}
