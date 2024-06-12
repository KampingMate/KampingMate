package com.demo.persistence;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.demo.domain.GoCamping;

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
    
    @Query(value = "SELECT * FROM go_camping g WHERE (:doNm IS NULL OR g.do_nm = :doNm)", nativeQuery = true)
    List<GoCamping> findByDoNm(@Param("doNm") String doNm);

    @Query(value = "SELECT * FROM go_camping g WHERE (:sigunguNm IS NULL OR g.sigungu_nm = :sigunguNm)", nativeQuery = true)
    List<GoCamping> findBySigunguNm(@Param("sigunguNm") String sigunguNm);

    @Query(value = "SELECT * FROM go_camping g WHERE (:lctCl IS NULL OR g.lct_cl = :lctCl)", nativeQuery = true)
    List<GoCamping> findByLctCl(@Param("lctCl") String lctCl);

    @Query(value = "SELECT * FROM go_camping g WHERE (:faclt IS NULL OR g.faclt_div_nm = :faclt)", nativeQuery = true)
    List<GoCamping> findByFaclt(@Param("faclt") String faclt);

    @Query(value = "SELECT * FROM go_camping g WHERE (:induty IS NULL OR g.induty = :induty)", nativeQuery = true)
    List<GoCamping> findByInduty(@Param("induty") String induty);

    @Query(value = "SELECT * FROM go_camping g WHERE (:sbrsCl IS NULL OR g.sbrs_cl = :sbrsCl)", nativeQuery = true)
    List<GoCamping> findBySbrsCl(@Param("sbrsCl") String sbrsCl);

    @Query(value = "SELECT * FROM go_camping g WHERE (:bottom IS NULL OR :bottom = '' " +
            "OR (:bottom = '잔디' AND g.site_bottom_cl1 > 0) " +
            "OR (:bottom = '파쇄석' AND g.site_bottom_cl2 > 0) " +
            "OR (:bottom = '데크' AND g.site_bottom_cl3 > 0) " +
            "OR (:bottom = '자갈' AND g.site_bottom_cl4 > 0) " +
            "OR (:bottom = '맨흙' AND g.site_bottom_cl5 > 0))",
            nativeQuery = true)
    List<GoCamping> findByBottom(@Param("bottom") String bottom);
}
