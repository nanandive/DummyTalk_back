package com.example.DummyTalk.Common.WebSocket;


import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class WebSocketController {
    private final SimpMessagingTemplate simpMessagingTemplate;
    @MessageMapping("message")  // '/app/message'로 들어오는 메시지를 처리
    public void handleMessage(String message) {
        // 웹소켓 클라이언트로 메시지 전송
        System.out.println(message);
        simpMessagingTemplate.convertAndSend("/topic/msg", "hi");
    }



}