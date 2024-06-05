package com.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.domain.MemberData;
import com.demo.persistence.MemberRepository;

@Service
public class MemberServiceImpl implements MemberService {

	@Autowired
	private MemberRepository memberRepo;
	
	@Override
	public int confirmID(String id) {
		int result = 0;
		
		MemberData member = memberRepo.findByLoginId(id);
		
		if(member != null) {
			result = 1;
		} else {
			result = -1;
		}
		
		return result;
	}

	@Override
	public int confirmEmail(String email) {
		int result = 0;
		
		MemberData member = memberRepo.findByEmail(email);
		
		if(member != null) {
			result = 1;
		} else {
			result = -1;
		}
		
		return result;
	}
	
	
	@Override
	public MemberData getMember(String id) {
		 return memberRepo.findByLoginId(id);
	}
	
}
