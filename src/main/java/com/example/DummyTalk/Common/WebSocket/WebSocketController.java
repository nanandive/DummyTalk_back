package com.example.DummyTalk.Common.WebSocket;

import com.example.DummyTalk.Chat.Channel.Dto.ChatDataDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class WebSocketController {
    private final SimpMessagingTemplate simpMessagingTemplate;
    @MessageMapping("message")  // 클라이언트에서 메시지를 보내면 /app/message로 메시지를 전달
    public void handleMessage(String message) {
        // 웹소켓 클라이언트로 메시지 전송
        System.out.println(message);
        simpMessagingTemplate.convertAndSend("/topic/msg", "hi");
    }


}