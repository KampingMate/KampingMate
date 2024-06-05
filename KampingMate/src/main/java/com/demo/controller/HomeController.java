package com.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.demo.service.GoCampingAPI;
import com.demo.service.GoCampingAPI.CampingApiResponse;

import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {

	@GetMapping("/")
    public String index() {
        return "index"; // index.html 파일을 반환합니다.
    }

    @GetMapping("/main")
    public String main() {
    	
        return "main";
    }
    @GetMapping("/main2")
    public String main2() {
    	
        return "main2";
    }
    
    @GetMapping("/introduce")
    public String introduce() {
    	return "introduce";
    }
    @GetMapping("/login")
    public String login() {
        return "login/loginForm"; // loginForm.html을 렌더링합니다.
    }
    
}
