package com.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.demo.domain.Member;

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
        return "main"; // main.html 파일을 반환합니다.
    }
}
