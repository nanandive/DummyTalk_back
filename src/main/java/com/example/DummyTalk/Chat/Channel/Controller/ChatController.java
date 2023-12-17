package com.example.DummyTalk.Chat.Channel.Controller;

import com.example.DummyTalk.Chat.Channel.Dto.MessageHistoryDto;
import com.example.DummyTalk.Chat.Channel.Dto.SendChatDto;
import com.example.DummyTalk.Chat.Channel.Service.ChatService;
import com.example.DummyTalk.Common.DTO.ResponseDTO;
import com.example.DummyTalk.Jwt.JwtFilter;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatController {

    private final ChatService chatService;

    @MessageMapping("/{channelId}/message") // '/app/message'로 들어오는 메시지를 처리
    @SendTo("/topic/msg/{channelId}")
    public MessageResponse handleMessage(SendChatDto message, @DestinationVariable String channelId) {
        log.info("============message================================={}", message);

        // 채팅 데이터 저장
        int chatId = chatService.saveChatData(message);
        message.setChatId(chatId);
        log.info("============setChatId================================={}", message);

        return new MessageResponse(message.getNickname(), "채팅 메시지 전송 성공", message);
    }

    /* 전송된 메시지 데이터 저장 */
    public ResponseEntity<ResponseDTO> saveChatData(@RequestBody SendChatDto message) {
        log.info("saveChatData ============================={}.", message);
        chatService.saveChatData(message);
        return ResponseEntity
                .ok()
                .body(new ResponseDTO(HttpStatus.OK, "채팅 저장 성공"));
    }

    /* 채널 아이디로 채팅 데이터 리스트 조회 */
    @GetMapping("/{channelId}")
    public ResponseEntity<ResponseDTO> getChatData(@PathVariable int channelId) {
        log.info("getChatData ============================={}", channelId);
        try {
            List<MessageHistoryDto> list = chatService.findChatData(channelId);
            log.info("getChatData list============================={}", list.size());
            return ResponseEntity
                    .ok()
                    .body(new ResponseDTO(HttpStatus.OK,
                            "이전 채팅 불러오기 성공", list));
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
        }
    }

    @PostMapping("trans/{nationLanguage}")
    public MessageResponse translateMessage(@RequestBody SendChatDto message,
            @PathVariable String nationLanguage,
            HttpServletRequest request) {

        String token = JwtFilter.resolveToken(request);

        log.debug("{}", message);

        // return new MessageResponse(chat.getMessage(), "TranslateMessage", chat);
        return chatService.translateMessage(message, nationLanguage);
    }

}
