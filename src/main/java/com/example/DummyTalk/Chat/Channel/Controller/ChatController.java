package com.example.DummyTalk.Chat.Channel.Controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.DummyTalk.Chat.Channel.Dto.ChatDataDto.MessageType;
import com.example.DummyTalk.Chat.Channel.Dto.MessageHistoryDto;
import com.example.DummyTalk.Chat.Channel.Dto.MessageRequest;
import com.example.DummyTalk.Chat.Channel.Dto.SummaryDto;
import com.example.DummyTalk.Chat.Channel.Entity.ChatDataEntity;
import com.example.DummyTalk.Chat.Channel.Service.ChatService;
import com.example.DummyTalk.Common.DTO.ResponseDTO;
import com.example.DummyTalk.Exception.ChatFailException;
import com.example.DummyTalk.Jwt.JwtFilter;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatController {

    private final ChatService chatService;


    /*** 웹소켓으로 들어온 메시지 수신 및 발신
     * @param message SendChatDto: 클라이언트에서 전송된 채팅 메시지 데이터
     * @param channelId String: 메시지가 속한 채널의 ID
     * @return MessageResponse: 닉네임, 상태, 메시지 등을 포함한 응답 객체
     * '/topic/msg/{channelId}' 경로로 들어온 메시지를 수신하고, 동일한 경로로 MessageResponse를 반환합니다.
     * type에 따라 오디오, 이미지, 텍스트 메시지를 구분합니다.
     */
    @MessageMapping("/{channelId}/message")
    @SendTo("/topic/msg/{channelId}")
    public MessageResponse handleMessage(MessageRequest message
            , @DestinationVariable String channelId) {
        log.info("\n handleMessage message   : {}", message);

        if (message.getType().equals(MessageType.AUDIO.toString())) {
            // int audioChatId = chatService.saveAudioChatData(message);
            // message.setAudioChatId(audioChatId);
            return new MessageResponse(message.getNickname(), "오디오 채팅 메시지 전송 성공", message);
        }

        if (message.getType() != null && message.getType().equals("IMAGE")) {
            log.info("\n handleMessage IMAGE   : {}", message);
            return new MessageResponse(message.getNickname(), "이미지 전송 성공", message);
        }

        if (message.getMessage() != null && !message.getMessage().isEmpty()) {
            ChatDataEntity chat = chatService.saveChatData(message);
            message.setChatId(chat.getChatId());
            message.setType("TEXT");
            message.setCreatedAt(chat.getCreatedAt());
            log.info("\n handleMessage TEXT   : {}", message);
            return new MessageResponse(message.getNickname(), "일반 텍스트 채팅 메시지 전송 성공", message);
        } else {
            throw new ChatFailException("메시지를 입력해주세요. ");
        }
    }

    /* 채널 아이디로 채팅 데이터 리스트 조회 */
    @GetMapping("/{channelId}/{userId}")
    public ResponseEntity<ResponseDTO> getChatData(@PathVariable int channelId, @PathVariable String userId) {
        log.info("\n getChatData channelId=============================\n{}", channelId);
        try {
            List<MessageHistoryDto> list = chatService.findChatData(channelId);
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
    public MessageResponse translateMessage(@RequestBody MessageRequest message,
                                            @PathVariable String nationLanguage,
                                            HttpServletRequest request) {

        String token = JwtFilter.resolveToken(request);

        log.debug("{}", message);

        // return new MessageResponse(chat.getMessage(), "TranslateMessage", chat);
        return chatService.translateMessage(message, nationLanguage);
    }


    /* 채팅 삭제 */
    @PostMapping("/del/{channelId}/{chatId}")
    public ResponseEntity<ResponseDTO> deleteChat(
            @PathVariable(value="channelId") int channelId,
            @PathVariable(value="chatId") int chatId) {
        log.info("\n deleteChat channelId=============================\n{}", channelId);
        try {
            return ResponseEntity
                    .ok().body(new ResponseDTO(HttpStatus.OK
                            ,"채팅 삭제 성공", chatService.deleteChat(channelId, chatId)));
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
        }
    }

    /* 채팅 요약 */
    @PostMapping("/summary")
    public ResponseEntity<SummaryDto> summaryChat(){
        return null;
    }

}





