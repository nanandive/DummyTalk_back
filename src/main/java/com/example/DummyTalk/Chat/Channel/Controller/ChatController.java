package com.example.DummyTalk.Chat.Channel.Controller;

import com.example.DummyTalk.Chat.Channel.Dto.ChannelParticipantDto;
import com.example.DummyTalk.Chat.Channel.Dto.MessageHistoryDto;
import com.example.DummyTalk.Chat.Channel.Dto.SendChatDto;
import com.example.DummyTalk.Chat.Channel.Entity.ChannelParticipantEntity;
import com.example.DummyTalk.Chat.Channel.Service.ChannelService;
import com.example.DummyTalk.Chat.Channel.Service.ChatService;
import com.example.DummyTalk.Common.DTO.ResponseDTO;
import com.example.DummyTalk.Jwt.JwtFilter;
import com.example.DummyTalk.User.Entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    public MessageResponse handleMessage(SendChatDto message
            , @DestinationVariable String channelId
            ) {
        log.info("============message================================={}", message);
        // 채팅 데이터 저장
        if (message.getAudioUrl() != null && !message.getAudioUrl().isEmpty()) {
            // 오디오 채팅 데이터 저장
            int audioChatId = chatService.saveAudioChatData(message);
            message.setAudioChatId(audioChatId);
            log.info("============setAudioChatId================================={}", message);
            return new MessageResponse(message.getNickname(), "오디오 채팅 메시지 전송 성공", message);
        } else {
            // 일반 텍스트 채팅 데이터 저장
            int chatId = chatService.saveChatData(message);
            message.setChatId(chatId);
            log.info("============setChatId================================={}", message);
            return new MessageResponse(message.getNickname(), "일반 텍스트 채팅 메시지 전송 성공", message);
        }
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
    @GetMapping("/{channelId}/{userId}")
    public ResponseEntity<ResponseDTO> getChatData(@PathVariable int channelId, @PathVariable String userId) throws UnsupportedEncodingException {
        log.info("\n getChatData channelId=============================\n{}", channelId);
        Long user = Long.parseLong(userId);
        chatService.checkParticipant(channelId, user);
        log.info("\n getChatData userId ==============check 완료 userId===============\n{}", userId);
        try {
            List<MessageHistoryDto> list = chatService.findChatData(channelId, userId);
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


    @PostMapping("del/{chatId}")
    public ResponseEntity<ResponseDTO> deleteChat(@PathVariable int chatId){
        try {
            return ResponseEntity
                    .ok()
                    .body(new ResponseDTO(HttpStatus.OK,
                            "이전 채팅 불러오기 성공", chatService.deleteChat(chatId)));
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
        }
    }

}


//public class ChatController extends TextWebSocketHandler {
//    private final List<String> participants = new ArrayList<>();
//
//    @Override
//    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
//        log.info("============session================================={}", session);
//        // 접근자의 ID를 가져옵니다.
//        String id = Objects.requireNonNull(session.getRemoteAddress()).getHostName();
//
//        // 접근자를 참여자로 추가합니다.
//        participants.add(id);
//        session.getAttributes().put("participants", participants);
//    }
//
//    @Override
//    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // 메시지의 내용을 가져옵니다.
//        String content = message.getPayload();
//
//        // 메시지의 내용을 파싱합니다.
//        SendChatDto dto = new SendChatDto();
//        dto.setNickname(content.split(" ")[0]);
//        dto.setContent(content.split(" ")[1]);
//        dto.setChannelId(content.split(" ")[2]);
//
//        // 채팅 데이터 저장
//        int chatId = chatService.saveChatData(dto);
//        dto.setChatId(chatId);
//
//        // 참여자에게 메시지 전송
//        for (String participant : participants) {
//            if (!participant.equals(session.getRemoteAddress().getHostName())) {
//                session.sendMessage(new TextMessage(toJson(dto)));
//            }
//        }
//    private String toJson(Object obj) {
//        return new ObjectMapper().writeValueAsString(obj);
//    }
//    }


//    @PostMapping("/writePro")
//    public ResponseEntity<?> serverWritePro(@ModelAttribute ChannelDto channelDto) {
//        channelService.createChannel(channelDto);
//        return ResponseEntity.noContent().build();
//    }






