package com.demo.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.demo.domain.MemberData;

public interface MemberRepository extends JpaRepository<MemberData, Long> {
	
	@Query(value="SELECT * FROM member_data WHERE id =:id", nativeQuery = true)
	MemberData findByLoginId(String id);
	
	@Query(value="SELECT * FROM member_data WHERE email =:email", nativeQuery = true)
	MemberData findByEmail(String email);
	
}
