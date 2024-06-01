package com.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.demo.domain.Member;
import com.demo.service.MemberServiceImpl;

@Controller
@SessionAttributes("loginUser")
public class LoginController {

	@Autowired
	MemberServiceImpl memberService;
	// 로그인 화면
	@GetMapping("/login")
	public String loginView() {
		return "member/login";
	}
	
	// 로그인
	@PostMapping("/login")
	public String loginAction(Member vo, Model model) {
		int loginResult = memberService.login(vo);

        if (loginResult == 1) {
            // 로그인 성공
            model.addAttribute("member", vo);
            return "redirect:/main";  // 홈 페이지로 리다이렉트
        } else if (loginResult == 0) {
            // 비밀번호 불일치
            //model.addAttribute("loginError", "비밀번호가 잘못되었습니다.");
            return "login";  // 로그인 페이지로 다시 돌아감
        } else {
            // 아이디 없음
            //model.addAttribute("loginError", "아이디가 존재하지 않습니다.");
            return "login";  // 로그인 페이지로 다시 돌아감
        }
	}
	
	// 회원가입
	@PostMapping("join")
	public String joinAction(Member vo) {
		memberService.insertMember(vo);
		return "redirect:/main";
	}
	
	// ID 중복 확인 화면
    @GetMapping("/id_check_form")
    public String idCheckView(@RequestParam("id")String id, Model model) {
        int result = memberService.confirmId(id);
        
        model.addAttribute("message", result);
        model.addAttribute("id", id);
        
        return "member/idcheck";
    }
    // ID 중복 처리
    //@GetMapping("/check_id")
    //public String
    
    // 이메일 중복 화면
    @GetMapping("/email_check_form")
    public String emailCheckView(@RequestParam("email") String email, Model model) {
        int result = memberService.confirmEmail(email);
        model.addAttribute("message", result);
        model.addAttribute("email", email);
        return "member/emailcheck";
    }
    // 이메일 중복 처리
	@GetMapping("/check_email")
	//public String 
	// 아이디 찾기
	@PostMapping("find_id")
	public String findIdAction(Member vo, Model model) {
		// 입력된 이름과 이메일을 가지고 조회해서 member에 저장
		Member member = memberService.getId(vo.getName(), vo.getEmail());
		
		if(member != null) {// 회원이 존재
			model.addAttribute("message", 1);
			model.addAttribute("id", member.getId());
		} else {
			model.addAttribute("message", -1);
		}
		return "member/findIdResult";
	}
	
	// 비밀번호 찾기
	@PostMapping("find_pwd")
	public String findPwdAction(Member vo, Model model) {
		// 입력된 아이디, 이름, 이메일을 가지고 조회해서 member에 저장
		Member member = memberService.getPassword(vo.getId(), vo.getName(), vo.getEmail());
		
		if(member != null) {// 회원이 존재
			model.addAttribute("message", 1);
 			model.addAttribute("id", member.getId());
 			model.addAttribute("password", member.getPassword());
 		} else {
 			model.addAttribute("message", -1);
 		}
		return "member/findPwdResult";
	}
}





































