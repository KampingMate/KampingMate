package com.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.client.RestTemplate;

import com.demo.domain.MemberData;

import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {

    @GetMapping("/")
    public String index() {
        return "index"; // index.html 파일을 반환합니다.
    }

    @GetMapping("/main")
    public String main(Model model, HttpSession session) {
        // 세션에서 loginUser를 가져옵니다.
        MemberData loginUser = (MemberData) session.getAttribute("loginUser");
        
        // 로그인되지 않은 경우 로그인 페이지로 리다이렉트합니다.
        if (loginUser == null) {
            return "main";
        }
        
        // 로그인된 사용자의 이름을 모델에 추가합니다.
        model.addAttribute("name", loginUser.getName());

        // API를 호출하여 데이터를 가져옵니다.
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:8090/api/data", String.class);
        String data = response.getBody();
        
        // 가져온 데이터를 모델에 추가합니다.
        model.addAttribute("data", data);
        
        return "main"; // main.html 파일을 반환합니다.
    }
    
    @GetMapping("/login")
    public String login() {
        return "loginForm"; // loginForm.html을 렌더링합니다.
    }
    
 // 로그아웃 처리
    @GetMapping("/logout")
    public String logout(SessionStatus status) {
        status.setComplete(); // 세션 완료 상태로 설정
        return "main"; // 로그아웃 후 로그인 화면으로 리다이렉트
    }
}
