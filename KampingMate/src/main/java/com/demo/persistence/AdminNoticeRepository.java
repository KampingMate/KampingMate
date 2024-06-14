package com.demo.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.demo.domain.Notice;

public interface AdminNoticeRepository extends JpaRepository<Notice, Integer> {

    @Query(value="SELECT * FROM notice ORDER BY notice_date DESC LIMIT 5", nativeQuery = true)
    List<Notice> getAllListMain();
    
    @Query(value="SELECT * FROM notice WHERE notice_seq = :boardnum2", nativeQuery = true)
    Notice findByNum(int boardnum2);
    
  //전체 공지사항 리스트 출력
//  	@Query(value="SELECT * FROM Notice ORDER BY notice_seq DESC ", nativeQuery=true)
//  	public List<Notice> getAllList();
  	
    @Query(value="SELECT * FROM Notice WHERE notice_cate = 'notice' ORDER BY notice_seq DESC", nativeQuery=true)
    public List<Notice> getAllNotices();

    @Query(value="SELECT * FROM Notice WHERE notice_cate = 'event' ORDER BY notice_seq DESC", nativeQuery=true)
    public List<Notice> getAllEvents();

    @Query(value = "SELECT * FROM notice WHERE notice_seq = :boardnum2", nativeQuery = true)
    Notice findBySeq(int boardnum2);


}
