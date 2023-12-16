package com.example.DummyTalk.Chat.Channel.Controller;

import com.example.DummyTalk.Chat.Channel.Dto.ChatListDto;
import com.example.DummyTalk.Chat.Channel.Dto.SendChatDto;
import com.example.DummyTalk.Chat.Channel.Service.ChannelService;
import com.example.DummyTalk.Common.DTO.ResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.socket.WebSocketSession;

import java.util.List;


@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/channel")
public class ChannelController {

    private final ChannelService channelService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/{channelId}/message")  // '/app/message'로 들어오는 메시지를 처리
    @SendTo("/topic/msg/{channelId}")
    public MessageResponse handleMessage(SendChatDto message, @DestinationVariable String channelId) {
        log.info("============message================================={}", message);

        // 채팅 데이터 저장
        int chatId = channelService.saveChatData(message);
        message.setChatId(chatId);
        log.info("============setChatId================================={}", message);
        //채팅방에 메시지 전송
        return new MessageResponse(message.getNickname(), "채팅 메시지 전송 성공", message);
    }

    @GetMapping("/chat")
    public String main() {
        return "chat/writeForm";
        // private static Set<Long>userList = new HashSet<>();
    }


    /* 채팅 데이터 삽입 */
    @PostMapping("/chat")
    public ResponseEntity<ResponseDTO> saveChatData(@RequestBody SendChatDto message) {
        log.info("saveChatData ============================={}.", message);
        channelService.saveChatData(message);
        return ResponseEntity
                .ok()
                .body(new ResponseDTO(HttpStatus.OK, "채팅 저장 성공"));
    }

    /* 채널 아이디로 채팅 리스트조회 */
    @GetMapping("/chat/{channelId}")
    public ResponseEntity<ResponseDTO> getChatData(@PathVariable int channelId) {
        log.info("getChatData ============================={}", channelId);
        try {
            List<ChatListDto> list = channelService.findChatData(channelId);
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
