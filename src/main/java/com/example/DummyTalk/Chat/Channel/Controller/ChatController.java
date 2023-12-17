package com.example.DummyTalk.Chat.Channel.Controller;

import com.example.DummyTalk.Chat.Channel.Dto.MessageHistoryDto;
import com.example.DummyTalk.Chat.Channel.Dto.SendChatDto;
import com.example.DummyTalk.Chat.Channel.Service.ChatService;
import com.example.DummyTalk.Common.DTO.ResponseDTO;
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
@Controller
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatController {

    private final ChatService chatService;

    /*****   웹소켓으로 들어온 메시지 수신 및 발신
     * /topic/msg/{channelId} 경로로 들어온 메시지를 수신하고, 동일한 경로로 MessageResponse를 반환합니다.
     *
     * @param message SendChatDto: 클라이언트에서 전송된 채팅 메시지 데이터
     * @param channelId String: 메시지가 속한 채널의 ID
     *
     * @return MessageResponse: 닉네임, 상태, 메시지 등을 포함한 응답 객체
     *
     * 채팅 데이터를 데이터베이스에 저장한 후, 저장된 데이터의 채팅 ID를 설정하고 응답을 반환합니다.
     */
    @MessageMapping("/{channelId}/message")
    @SendTo("/topic/msg/{channelId}")
    public MessageResponse handleMessage(SendChatDto message, @DestinationVariable String channelId) {
        log.info("============message================================={}", message);

        // 채팅 데이터 저장
        int chatId = chatService.saveChatData(message);
        message.setChatId(chatId);
        log.info("============setChatId================================={}", message);

        return new MessageResponse(message.getNickname(), "채팅 메시지 전송 성공", message);
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
}

