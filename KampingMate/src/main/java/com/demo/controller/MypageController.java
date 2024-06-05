package com.demo.controller;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


import com.demo.domain.MemberData;
import com.demo.service.MemberService;

import jakarta.servlet.http.HttpSession;

@Controller
public class MypageController {

	@Autowired
	private MemberService memberService;

	
	// 마이페이지 메인화면
	@GetMapping("/mypage")
	public String mypageView(HttpSession session, Model model) {
		MemberData loginUser = (MemberData)session.getAttribute("loginUser");
		
		if(loginUser == null) {
			return "redirect:/login";
		} 
			return "mypage/mypageMain";
	}
	
	// 내 정보 화면
	@GetMapping("/infoView")
	public String myInfoView(HttpSession session, Model model) {
		MemberData loginUser = (MemberData)session.getAttribute("loginUser");
		
		if(loginUser == null) {
			return "redirect:/login";
		} else {
			 // 최신 사용자 정보를 데이터베이스에서 가져오기
	        MemberData userInfo = memberService.getMember(loginUser.getId());

	        // 정보를 Thymeleaf 템플릿에 전달
	        model.addAttribute("name", userInfo.getName());
	        model.addAttribute("id", userInfo.getId());
	        model.addAttribute("********", "********");
	        model.addAttribute("email", userInfo.getEmail());
		}
		return "mypage/infoView";
	}
	
	// 내 정보 수정 화면
	@GetMapping("/infoUpdate")
	public String infoUpdateView(HttpSession session, Model model) {
		MemberData loginUser = (MemberData)session.getAttribute("loginUser");
		
		if(loginUser == null) {
			return "redirect:/login";
		} else {
			// 최신 사용자 정보를 데이터베이스에서 가져오기
	        MemberData userInfo = memberService.getMember(loginUser.getId());
	        
			model.addAttribute("loginUser", userInfo);
			
		}
		return "mypage/infoUpdate";
	}
	
	// 개인정보 수정
	@PostMapping("/update_info")
	public String infoUpdateAction(HttpSession session, MemberData vo) {
	    MemberData loginUser = (MemberData) session.getAttribute("loginUser");
	    
	    if(loginUser == null) {
			return "redirect:/login";
		} else {
			// 로그인한 회원수정
			memberService.changeInfo(vo);
			
		}
	    return "redirect:/infoView";
	}

	
		@GetMapping("/Outdoor")
		public String outdoor(HttpSession session, Model model) {
			MemberData loginUser = (MemberData)session.getAttribute("loginUser");
			
			if(loginUser == null) {
				return "redirect:/login";
			} 
				return "mypage/Outdoor";
		}
	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
}