package com.demo.controller;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.demo.domain.MemberData;
import com.demo.domain.User;
import com.demo.helper.constants.SocialLoginType;
import com.demo.model.GoogleUser;
import com.demo.persistence.MemberRepository;
import com.demo.service.MemberService;
import com.demo.service.OauthService;
import com.demo.service.PasswordGenerator;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/auth")
public class LoginController {

    private final OauthService oauthService;

    @Autowired
    MemberRepository memberRepo;
    
    @Autowired
    MemberService memberService;

    private String refreshToken;
    private String accessToken;

    @GetMapping("/login")
    public String login() {
        return "loginForm";
    }

    @GetMapping(value = "/{socialLoginType}")
    public String socialLoginType(
            @PathVariable(name = "socialLoginType") SocialLoginType socialLoginType,
            HttpServletResponse response) {
        log.info(">> 사용자로부터 SNS 로그인 요청을 받음 :: {} Social Login", socialLoginType);
        return oauthService.request(socialLoginType, response);
    }

    @PostMapping(value = "/{socialLoginType}/callback")
    public String callback(
            @PathVariable(name = "socialLoginType") SocialLoginType socialLoginType,
            @RequestParam Map<String, String> params,
            Model model, HttpSession session) {
        log.info(">> 소셜 로그인 API 서버로부터 받은 사용자 정보 :: {}", params);
        accessToken = params.get("access_token"); // access_token이 포함되어 있는지 확인 필요
        refreshToken = params.get("refresh_token"); // refresh_token이 포함되어 있는지 확인 필요
        GoogleUser user = new GoogleUser();
        user.setId(params.get("id"));
        user.setName(params.get("username"));
        user.setEmail(params.get("email"));
        user.setPicture(params.get("img"));
        session.setAttribute("user", user);
        model.addAttribute("user", user);
        return "redirect:/auth/contract"; // 약관 페이지로 리다이렉트
    }

    @GetMapping("/contract")
    public String contractPage(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            String id = user.getId();
            int exists = memberService.confirmID(id);
            if (exists == 1) {
                return "redirect:/auth/autoLogin";
            }
            model.addAttribute("id", id);
        }
        return "contract"; // templates/contract.html로 이동
    }

    @PostMapping("/saveUserSession")
    @ResponseBody
    public String saveUserSession(@RequestBody User user, HttpSession session) {
        session.setAttribute("user", user);
        return "success";
    }

    @GetMapping("/joinForm")
    public String joinFormPage(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            model.addAttribute("email", user.getEmail());
            model.addAttribute("id", user.getId());
            model.addAttribute("provider", user.getProvider());
        }
        return "joinForm";
    }

    @PostMapping("/confirmEmail")
    @ResponseBody
    public int confirmEmail(@RequestParam String email) {
        return memberService.confirmEmail(email);
    }

    @PostMapping("/joinAction")
    public String joinAction(MemberData vo) {
        MemberData member = new MemberData();
        String randomPassword = PasswordGenerator.generateRandomPassword();
        
        // long 타입의 생년월일을 문자열로 변환
        String birthDateString = Long.toString(vo.getAge());

        // 문자열을 "yyyyMMdd" 형식으로 받아서 LocalDate 객체로 변환
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate birthDate = LocalDate.parse(birthDateString, formatter);

        // 현재 날짜
        LocalDate currentDate = LocalDate.now();

        // 나이 계산
        int age = Period.between(birthDate, currentDate).getYears();

        member.setId(vo.getId());
        member.setName(vo.getName());
        member.setAge((long) age);
        member.setEmail(vo.getEmail());
        member.setTelephone(vo.getTelephone());
        member.setProvider(vo.getProvider());
        member.setPassword(randomPassword);
        
        memberRepo.save(member);
        
        return "redirect:/";
    }

    @GetMapping("/autoLogin")
    public String autoLogin(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            String id = user.getId();
            MemberData loginUser = memberService.getMember(id);
            session.setAttribute("loginUser", loginUser);
        }
        return "redirect:/";
    }
}
