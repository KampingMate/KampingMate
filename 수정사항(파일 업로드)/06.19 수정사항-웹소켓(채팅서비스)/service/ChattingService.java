package com.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.demo.domain.ChatRoom;
import com.demo.domain.Chatting;
import com.demo.domain.MemberData;
import com.demo.persistence.ChattingRepository;
import java.util.List;

@Service
public class ChattingService {

    @Autowired
    private ChattingRepository chattingRepository;

    public void saveMessage(ChatRoom chatRoom, MemberData member, String nickname, String content) {
        Chatting chatMessage = new Chatting();
        chatMessage.setChatRoom(chatRoom);
        chatMessage.setMember(member);
        chatMessage.setNickname(nickname);
        chatMessage.setContent(content);
        chattingRepository.save(chatMessage);
    }

    public List<Chatting> getMessagesByRoom(ChatRoom chatRoom) {
        return chattingRepository.findByChatRoom(chatRoom);
    }

    public List<ChatRoom> getJoinedRooms(Long noData) {
        return chattingRepository.findJoinedChatRoomsByMemberNoData(noData);
    }

    public String getNicknameByRoomSeqAndNoData(int roomSeq, Long noData) {
        return chattingRepository.findNicknameByRoomSeqAndNoData(roomSeq, noData);
    }
}
