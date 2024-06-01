package com.demo.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.demo.domain.Member;

public interface MemberRepository extends JpaRepository<Member, String> {

	// 회원 조회
	@Query(value="SELECT * FROM member WHERE id =:id", nativeQuery=true)
	Member findByUserId(String id);
	
	// 아이디 찾기
	@Query(value="SELECT * FROM member WHERE name =:name AND email =:email", nativeQuery=true)
	Member findId(String name, String email);
	
	// 비밀번호 찾기
	@Query(value="SELECT * FROM member WHERE id =:id AND name =:name AND email =:email", nativeQuery=true)
	Member findPassword(String id, String name, String email);
	
	// 이메일 중복체크용
	@Query(value = "SELECT * FROM member_data WHERE email =:email", nativeQuery=true)
	Member findByEmail(String email);
}
