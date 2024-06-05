package com.demo.service;

import com.demo.domain.MemberData;

public interface MemberService {

	public int confirmID(String id);
	
	public MemberData getMember(String id);
	
	public int confirmEmail(String email);
}
