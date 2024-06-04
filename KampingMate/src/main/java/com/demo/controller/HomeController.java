package com.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {

	@GetMapping("/")
    public String index() {
        return "index"; // index.html 파일을 반환합니다.
    }

    @GetMapping("/main")
    public String main(Model model, HttpSession session) {
        return "main";
    }
    
    @GetMapping("/introduce")
    public String introduce() {
    	return "introduce";
    }
    
}
