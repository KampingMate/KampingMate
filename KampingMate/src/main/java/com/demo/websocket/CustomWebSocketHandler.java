package com.demo.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import java.io.IOException;

@Component
public class CustomWebSocketHandler implements WebSocketHandler {

    private final ChatRoomManager chatRoomManager;

    @Autowired
    public CustomWebSocketHandler(ChatRoomManager chatRoomManager) {
        this.chatRoomManager = chatRoomManager;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // 클라이언트가 연결되면 기본 채팅방에 추가
        chatRoomManager.addUserToRoom("defaultRoom", session);
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        // 클라이언트로부터 메시지를 받았을 때 처리하는 로직
        // 여기에서 메시지를 채팅방에 속한 모든 사용자에게 전송하는 로직 추가 가능
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        // 에러 발생 시 처리 로직
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        // 클라이언트 연결이 종료되면 채팅방에서 제거
        chatRoomManager.removeUserFromRoom("defaultRoom", session);
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
