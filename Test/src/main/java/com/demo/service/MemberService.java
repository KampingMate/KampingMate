package com.demo.service;

import com.demo.domain.Member;

public interface MemberService {

	// 회원가입
	void insertMember(Member vo);
	
	// 로그인
	public int login(Member member);
	
	// 아이디 찾기
	Member getId(String name, String email);
	
	// 비밀번호 찾기
	Member getPassword(String id, String name, String email);
	
	// 중복 체크
	public int confirmId(String id);
	public int confirmEmail(String email);

	
}
