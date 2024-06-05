//package com.demo.controller;
//
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//
//import com.demo.domain.MemberData;
//
//import jakarta.servlet.http.HttpSession;
//
//@Controller
//public class MainController {
//
//	@GetMapping("/login")
//	public String MainView(HttpSession session, Model model) {
//		MemberData member = (MemberData) session.getAttribute("loginUser");
//		model.addAttribute("loginUser", member);
//		return "login/loginForm";
//	}
//	
//	
//	
//	
//	
//	
//}
