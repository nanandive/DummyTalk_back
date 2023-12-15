package com.example.DummyTalk.Chat.Channel.Controller;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.DummyTalk.Chat.Channel.Dto.ChatListDto;
import com.example.DummyTalk.Chat.Channel.Dto.SendChatDto;
import com.example.DummyTalk.Chat.Channel.Service.ChannelService;
import com.example.DummyTalk.Common.DTO.ResponseDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


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
        log.info("{}", message);

        return new MessageResponse();
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
                .body(new ResponseDTO(HttpStatus.OK, "업무 등록 성공"));
    }

    /* 채널 아이디로 채팅 리스트 조회 */
    @GetMapping("/chat/{channelId}")
    public ResponseEntity<ResponseDTO> getChatData(@PathVariable int channelId) {
        log.info("getChatData ============================={}", channelId);

        List<ChatListDto> list = channelService.findChatData(channelId);
        log.info("getChatData list============================={}", list.size());

        return ResponseEntity
                .ok()
                .body(new ResponseDTO(HttpStatus.OK,
                        "업무 등록 성공", list));
    }




}
