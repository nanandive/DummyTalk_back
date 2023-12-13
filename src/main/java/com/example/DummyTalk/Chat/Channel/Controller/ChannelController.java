package com.example.DummyTalk.Chat.Channel.Controller;
import com.example.DummyTalk.Chat.Channel.Dto.ChatDataDto;
import com.example.DummyTalk.Chat.Channel.Service.ChannelService;
import com.example.DummyTalk.Common.DTO.ResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.juli.logging.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/channel")
public class ChannelController {
    private final ChannelService channelService;


    private final SimpMessagingTemplate simpMessagingTemplate;
    @MessageMapping("message")  // '/app/message'로 들어오는 메시지를 처리
    public void handleMessage(String message) {
        System.out.println(message);
        simpMessagingTemplate.convertAndSend("/topic/msg", "hi");
    }

    @GetMapping("/chat")
    public String main() {
        return "chat/writeForm";
        // private static Set<Long>userList = new HashSet<>();
    }

//    /* 채널 리스트 */
//    @GetMapping("/list")
//    public List<ChannelDto> channelDtoList () {
//        return channelService.findAllChannel();
//    }











    /* 채팅 데이터 삽입 */
    @PostMapping("/chat")
    public ResponseEntity<ResponseDTO> saveChatData(@ModelAttribute ChatDataDto message) {
        log.info("ChatDataDto ============================={}.", message);
        channelService.saveChatData(message);

        return ResponseEntity
                .ok()
                .body(new ResponseDTO(HttpStatus.OK, "업무 등록 성공"));
    }

    @GetMapping("/chat/{channelId}")
    public ResponseEntity<ResponseDTO> getChatData(@PathVariable Long channelId) {
        log.info("getChatData ============================={}", channelId);

        List<ChatDataDto> list = channelService.findChatData(channelId);
        log.info("getChatData list============================={}", list.size());

        return ResponseEntity
                .ok()
                .body(new ResponseDTO(HttpStatus.OK,
                        "업무 등록 성공", list));
    }

}
