package com.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;

import com.demo.domain.MemberData;
import com.demo.dto.CampingItem;
import com.demo.service.GoCampingAPI;

import jakarta.servlet.http.HttpSession;

@Controller
public class MainController {

    @Autowired
    private GoCampingAPI goCampingAPI;
    
	@GetMapping("/")
    public String index() {
        return "index"; // index.html 파일을 반환합니다.
    }
	
	@GetMapping("/main")
    public String main(@RequestParam(defaultValue = "1") int page, Model model, HttpSession session) {
        // 세션에서 loginUser를 가져옵니다.
        MemberData loginUser = (MemberData) session.getAttribute("loginUser");
        
        // 로그인되지 않은 경우 로그인 페이지로 리다이렉트합니다.
        if (loginUser == null) {
        	try {
                GoCampingAPI.CampingApiResponse response = goCampingAPI.getCampingSites(page, 100);
                List<CampingItem> campingItems = response.getItems();
                
                model.addAttribute("items", campingItems);
                model.addAttribute("loginUserNumberData", session.getAttribute("loginUserNumberData"));
                // 로그인된 사용자의 이름을 모델에 추가합니다.
                return "main";
            } catch (Exception e) {
                e.printStackTrace();
                model.addAttribute("error", "Failed to load camping sites.");
                return "error";
            }
        }

        try {
            GoCampingAPI.CampingApiResponse response = goCampingAPI.getCampingSites(page, 100);
            List<CampingItem> campingItems = response.getItems();
            
            model.addAttribute("items", campingItems);
            model.addAttribute("loginUserNumberData", session.getAttribute("loginUserNumberData"));
         
            // 로그인된 사용자의 이름을 모델에 추가합니다.
            model.addAttribute("name", loginUser.getName());
            // 로그인된 사용자의 이름을 모델에 추가합니다.
            return "main";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Failed to load camping sites.");
            return "error";
        }
    }
	
	@GetMapping("/introduce")
    public String introduce() {
    	return "customer_service/introduce";
    }
	
	@GetMapping("/recommend")
	public String LoginAfterView(HttpSession session, Model model) {
		MemberData member = (MemberData) session.getAttribute("loginUser");
		model.addAttribute("loginUser", member);
		return "camping/mainRecommend";
	}
	
	
    @GetMapping("/login")
    public String login() {
        return "loginForm"; // loginForm.html을 렌더링합니다.
    }
    
    // 로그아웃 처리
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("loginUserNumberData");
    	session.removeAttribute("loginUser");
        return "main"; // 로그아웃 후 로그인 화면으로 리다이렉트
    }
	
	
	
}