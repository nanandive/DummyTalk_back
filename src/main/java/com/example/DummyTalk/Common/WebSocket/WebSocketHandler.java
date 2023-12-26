package com.example.DummyTalk.Common.WebSocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class WebSocketHandler extends TextWebSocketHandler {


    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // 클라이언트 연결 시 처리
        log.info("============afterConnectionEstablished================================={}", session);
        // 참여자 목록을 세션의 고유 속성에 저장
        session.getAttributes().put("participants", Arrays.asList("user1", "user2"));
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        // 클라이언트 메시지 수신 시 처리

        // 접근자 확인
        String username = (String) session.getAttributes().get("username");
//        if (!participant.contains(username)) {
        // 참여자가 아님
//    }
    }
}

