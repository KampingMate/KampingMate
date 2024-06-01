package com.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.domain.Member;
import com.demo.persistence.MemberRepository;

@Service
public class MemberServiceImpl implements MemberService {
	
	@Autowired
	private MemberRepository memberRepo;

	// 회원가입
	@Override
	public void insertMember(Member member) {
		memberRepo.save(member);
	}
	
	// 로그인
	@Override
	public int login(Member vo) {
		int result = -1;
		
		// 회원 조회
		Member member = memberRepo.findByUserId(vo.getId());
		
		if(member != null) {	// id 존재
			if(member.getPassword().equals(vo.getPassword())) {		// 비밀번호 일치
				result = 1;
			} else {	// 비밀번호 불일치
				result = 0;
			}
		} else {	// id 없음
			result = -1;
		}
		return result;
	}

	// 아이디 찾기
	@Override
	public Member getId(String name, String email) {
		return memberRepo.findId(name, email);
	}

	// 비밀번호 찾기
	@Override
	public Member getPassword(String id, String name, String email) {
		return memberRepo.findPassword(id, name, email);
	}

	// 아이디 중복체크
	@Override
	public int confirmId(String id) {
		int result = 0;
		
		Member member = memberRepo.findByUserId(id);
		
		if(member != null) {	// 아이디 존재
			result = 1;
		} else {	// 아이디 없음
			result = -1;
		}
		return result;
	}

	// 이메일 중복체크
	@Override
	public int confirmEmail(String email) {
		int result = 0;
		
		Member member = memberRepo.findByEmail(email);
		
		if(member != null) {
			result = 1;
		} else {
			result = -1;
		}
		return result;
	}

}
