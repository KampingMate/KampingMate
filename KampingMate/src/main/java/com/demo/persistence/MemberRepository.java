package com.demo.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.demo.domain.MemberData;

public interface MemberRepository extends JpaRepository<MemberData, Long> {

	@Query(value="SELECT * FROM member_data ORDER BY no_data DESC", nativeQuery = true)
	List<MemberData> getAllMember();
	
	@Query(value = "SELECT * FROM member_data WHERE name =:name AND email =:email", nativeQuery=true)
	MemberData findByNameAndEmail(String name, String email);

	@Query(value = "SELECT * FROM member_data WHERE id =:id", nativeQuery=true)
	MemberData findByLoginId(String id);
	
	@Query(value = "SELECT * FROM member_data WHERE email =:email", nativeQuery=true)
	MemberData findByEmail(String email);
	
	@Query(value = "SELECT * FROM member_data WHERE nickname =:nickname", nativeQuery=true)
	MemberData findByNickname(String nickname);
	
	@Query(value = "SELECT * FROM member_data WHERE id = :id AND name = :name AND email = :email", nativeQuery = true)
	MemberData findByIdAndNameAndEmail(String id, String name, String email);

	@Query(value = "SELECT * FROM member_data WHERE name LIKE %:name%", nativeQuery=true)
	List<MemberData> findByNameContaining(String name);
	
	// 총 회원수
	@Query(value="SELECT COUNT(*) FROM member_data", nativeQuery = true)
	int getMemberCount();
	
	
	//마이페이지
	// 개인정보 수정
	@Transactional
	@Modifying
	@Query("UPDATE MemberData md SET md.password=:password, md.email=:email WHERE md.id=:id")
	public void updateMemberData(@Param("id") String id, @Param("password") String password, @Param("email") String email);

	MemberData findByIdAndPassword(String id, String password);


}