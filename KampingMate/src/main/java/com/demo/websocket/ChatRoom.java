package com.demo.websocket;

import org.springframework.web.socket.WebSocketSession;
import java.util.HashSet;
import java.util.Set;

public class ChatRoom {

    private String roomName;
    private Set<WebSocketSession> sessions = new HashSet<>();

    public ChatRoom(String roomName) {
        this.roomName = roomName;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public Set<WebSocketSession> getSessions() {
        return sessions;
    }

    public void setSessions(Set<WebSocketSession> sessions) {
        this.sessions = sessions;
    }

    public void addSession(WebSocketSession session) {
        sessions.add(session);
    }

    public void removeSession(WebSocketSession session) {
        sessions.remove(session);
    }
}
