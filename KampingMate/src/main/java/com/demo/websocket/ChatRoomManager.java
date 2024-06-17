package com.demo.websocket;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;
import java.util.HashMap;
import java.util.Map;

@Component
public class ChatRoomManager {

    private final Map<String, ChatRoom> rooms = new HashMap<>();

    public ChatRoom getRoom(String roomName) {
        return rooms.get(roomName);
    }

    public void addRoom(String roomName) {
        rooms.put(roomName, new ChatRoom(roomName));
    }

    public void removeRoom(String roomName) {
        rooms.remove(roomName);
    }

    public void addUserToRoom(String roomName, WebSocketSession session) {
        ChatRoom room = getRoom(roomName);
        if (room != null) {
            room.addSession(session);
        }
    }

    public void removeUserFromRoom(String roomName, WebSocketSession session) {
        ChatRoom room = getRoom(roomName);
        if (room != null) {
            room.removeSession(session);
            // 방에 사용자가 없으면 방 삭제하기 (선택사항)
            if (room.getSessions().isEmpty()) {
                removeRoom(roomName);
            }
        }
    }
}
