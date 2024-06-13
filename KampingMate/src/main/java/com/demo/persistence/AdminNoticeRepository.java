package com.demo.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.demo.domain.Notice;

public interface AdminNoticeRepository extends JpaRepository<Notice, Integer> {

    @Query(value="SELECT * FROM notice ORDER BY notice_date DESC LIMIT 5", nativeQuery = true)
    List<Notice> getAllListMain();
    
    @Query(value="SELECT * FROM notice WHERE notice_seq = :boardnum2", nativeQuery = true)
    Notice findByNum(int boardnum2);

}
