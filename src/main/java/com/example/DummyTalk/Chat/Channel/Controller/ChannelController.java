package com.example.DummyTalk.Chat.Channel.Controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.DummyTalk.Chat.Channel.Dto.ChannelDto;
import com.example.DummyTalk.Chat.Channel.Dto.ChatListDto;
import com.example.DummyTalk.Chat.Channel.Dto.SendChatDto;
import com.example.DummyTalk.Chat.Channel.Entity.ChannelEntity;
import com.example.DummyTalk.Chat.Channel.Service.ChannelService;
import com.example.DummyTalk.Chat.Channel.Service.ChannelServiceImpl;
import com.example.DummyTalk.Common.DTO.ResponseDTO;
import com.example.DummyTalk.Jwt.JwtFilter;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/channel")
public class ChannelController {

    private final ChannelService channelService;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ChannelServiceImpl channelServiceImpl;

    /* 채널 생성 */
    @PostMapping("/writePro")
    public ResponseEntity<?> serverWritePro(@ModelAttribute ChannelDto channelDto) {
        channelServiceImpl.createChannel(channelDto);
        return ResponseEntity.noContent().build();
    }

    /* 채널 삭제 */
    @DeleteMapping("/channel/{id}/delete")
    public ResponseEntity<ChannelEntity> deleteChannel(@PathVariable Long id) {
        System.out.println("채널 삭제 (컨트롤러) >>>>>> :" + id);
        channelServiceImpl.channelDelete(id);
        return ResponseEntity.ok().build();

    }
    /* ------------------------------------------------------------- */

    @MessageMapping("/{channelId}/message") // '/app/message'로 들어오는 메시지를 처리
    @SendTo("/topic/msg/{channelId}")
    public MessageResponse handleMessage(SendChatDto message, @DestinationVariable String channelId) {
        log.info("============message================================={}", message);

        // 채팅 데이터 저장
        int chatId = channelService.saveChatData(message);
        message.setChatId(chatId);
        log.info("============setChatId================================={}", message);

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

    @PostMapping("/chat/trans/{nationLanguage}")
    public MessageResponse translateMessage(@RequestBody SendChatDto message,
     @PathVariable String nationLanguage,
     HttpServletRequest request) {
        
        String token = JwtFilter.resolveToken(request);

        log.debug("{}", message);
        
        
        // return new MessageResponse(chat.getMessage(), "TranslateMessage", chat);
        return channelServiceImpl.translateMessage(message, nationLanguage);
    }
    
}
