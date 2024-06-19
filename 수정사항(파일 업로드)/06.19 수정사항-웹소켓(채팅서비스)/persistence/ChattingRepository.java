package com.demo.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.demo.domain.Chatting;
import com.demo.domain.ChatRoom;

import java.util.List;

public interface ChattingRepository extends JpaRepository<Chatting, Long> {

    List<Chatting> findByChatRoom(ChatRoom chatRoom);

    @Query("SELECT c FROM Chatting c WHERE c.member.no_data = :no_data")
    List<Chatting> findByMemberNoData(@Param("no_data") Long noData);

    @Query("SELECT DISTINCT c.chatRoom FROM Chatting c WHERE c.member.no_data = :no_data")
    List<ChatRoom> findJoinedChatRoomsByMemberNoData(@Param("no_data") Long noData);

    @Query(value = "SELECT c.nickname FROM Chatting c WHERE c.room_seq = :room_seq AND c.no_data = :no_data AND ROWNUM = 1", nativeQuery = true)
    String findNicknameByRoomSeqAndNoData(@Param("room_seq") int roomSeq, @Param("no_data") Long noData);
}
