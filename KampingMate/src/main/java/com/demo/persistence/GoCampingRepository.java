package com.demo.persistence;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.demo.domain.GoCamping;
import com.demo.dto.GoCampingSearchList;

@Repository
public interface GoCampingRepository extends JpaRepository<GoCamping, Integer> {

    @Query("SELECT g.doNm FROM GoCamping g")
    List<String> findAllDoNmList();
    
    @Query("SELECT g.facltDivNm FROM GoCamping g")
    List<String> findAllFacltDivList();
    
    @Query("SELECT g.lctCl FROM GoCamping g")
	List<String> findAllLctClList();
    
    @Query("SELECT g.induty FROM GoCamping g")
	List<String> findAllIndutyList();
    
    
    @Query("SELECT g.sbrsCl FROM GoCamping g")
	List<String> findAllSbrsClList();
    
    @Query("SELECT g.doNm, g.sigunguNm, g.facltNm, g.intro, g.addr1, g.tel, g.firstImageUrl, g.sbrsCl FROM GoCamping g")
    List<GoCampingSearchList> getAllSearchList();
    
    @Query("SELECT new com.demo.dto.GoCampingSearchList(g.doNm, g.sigunguNm, g.facltNm, g.intro, g.addr1, g.tel, g.firstImageUrl, g.sbrsCl) " +
            "FROM GoCamping g " +
            "WHERE (:doNm IS NULL OR :doNm = '' OR g.doNm = :doNm) " +
            "AND (:sigunguNm IS NULL OR :sigunguNm = '' OR g.sigunguNm = :sigunguNm) " +
            "AND (:lctCl IS NULL OR :lctCl = '' OR g.lctCl = :lctCl)")
    Page<GoCampingSearchList> getSearchList(@Param("doNm") String doNm, @Param("sigunguNm") String sigunguNm, @Param("lctCl") String lctCl, Pageable pageable);

}
