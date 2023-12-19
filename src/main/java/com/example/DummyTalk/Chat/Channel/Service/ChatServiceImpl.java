package com.example.DummyTalk.Chat.Channel.Service;

import com.example.DummyTalk.Chat.Channel.Controller.MessageResponse;
import com.example.DummyTalk.Chat.Channel.Dto.ChannelParticipantDto;
import com.example.DummyTalk.Chat.Channel.Dto.MessageHistoryDto;
import com.example.DummyTalk.Chat.Channel.Dto.SendChatDto;
import com.example.DummyTalk.Chat.Channel.Entity.ChannelEntity;
import com.example.DummyTalk.Chat.Channel.Entity.ChannelParticipantEntity;
import com.example.DummyTalk.Chat.Channel.Entity.ChatDataEntity;
import com.example.DummyTalk.Chat.Channel.Repository.ChannelParticipantRepository;
import com.example.DummyTalk.Chat.Channel.Repository.ChannelRepository;
import com.example.DummyTalk.Chat.Channel.Repository.ChatRepository;
import com.example.DummyTalk.Exception.ChatFailException;
import com.example.DummyTalk.User.DTO.ChatSenderDTO;
import com.example.DummyTalk.User.Entity.User;
import com.example.DummyTalk.User.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatServiceImpl implements ChatService {
    private final ChannelRepository channelRepository;
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;
    private final ChannelParticipantRepository channelParticipantRepository;


    /* 채팅 데이터에 들어가는 유저 정보 Entity -> Dto 변환 */
    private ChatSenderDTO userToDto(User user) {
        return ChatSenderDTO.builder()
                .sender((long) user.getUserId())
                .name(user.getName())
                .nickname(user.getNickname())
                .userImgPath(user.getUserImgPath())
                .build();
    }

    /* 채팅 데이터 Entity -> Dto 변환 */
    private MessageHistoryDto chatToDto(ChatDataEntity chat) {
        log.info("saveChatData chatToDto ============================== " + chat);
        return MessageHistoryDto.builder()
                .message(chat.getMessage())
                .chatId(chat.getChatId())
                .createdAt(chat.getCreatedAt())
                .updatedAt(chat.getUpdatedAt())
                .sender(userToDto(chat.getSender()))
                .build();
    }

    private ChannelParticipantDto channelParticipantToDto(ChannelParticipantEntity channelParticipant) {
        return ChannelParticipantDto.builder()
                .channelId(channelParticipant.getChannelId())
                .userId(channelParticipant.getUserId())
                .lastChatId(channelParticipant.getLastChatId())
                .build();
    }

    /* 채팅 내용 저장 */
    @Transactional
    public int saveChatData(SendChatDto message) {
        // sender 조회
        log.info("saveChatData message ============================== " + message);
        User user = Optional.ofNullable(userRepository.findByUserId((long) message.getSender()))
                .orElseThrow(() -> new ChatFailException("유저 조회에 실패하였습니다. "));
        log.info("saveChatData user ============================== " + user);
        ChannelEntity channel = Optional.ofNullable(channelRepository.findByChannelId((long) message.getChannelId()))
                .orElseThrow(() -> new ChatFailException("채널 조회에 실패하였습니다."));
        log.info("saveChatData channel ============================== " + channel);

        try {
            ChatDataEntity chatEntity = ChatDataEntity.builder()
                    .channelId(channel)
                    .message(message.getMessage())
                    .sender(user)
                    .language("en")
                    .build();
            ChatDataEntity newChat = chatRepository.save(chatEntity);
            log.info("saveChatData newChat ============================== " + newChat);

            // 클라이언트에서 키 정렬을 하기 위한 chatId 반환입니다.
            return Math.toIntExact(newChat.getChatId());
        } catch (Exception e) {
            throw new ChatFailException("채팅 저장에 실패하였습니다.");
        }
    }

    /* 채널아이디로 참여자 정보 조회 */
    public void checkParticipant(int channelId, Long userId) {
        ChannelParticipantEntity channel = channelParticipantRepository.findByChannelIdAndUserId((long) channelId, userId);
//        if (channel == null) {
//            throw new ChatFailException("초대 된 채널이 아닙니다.");
//        }
    }

    @Override
    public Object deleteChat(int chatId) {
        return null;
    }

    /* 채널 아이디로 조회한 채널 리스트 */
    public List<MessageHistoryDto> findChatData(int channelId, String userId) {
        Long user = Long.valueOf(userId);

        ChannelEntity channelEntity = Optional.ofNullable(channelRepository.findByChannelId((long) channelId))
                .orElseThrow(() -> new ChatFailException("채널 조회에 실패하였습니다."));
        log.info("findChatData channelEntity ============================={}", channelEntity);
        try {
            List<MessageHistoryDto> chatlist =
                    chatRepository.findAllByChannelId(channelEntity).stream()
                            .map(this::chatToDto)
                            .collect(Collectors.toList());
            // log.info("findChatData chatlist ============================={}", chatlist.get(0).getCreatedAt());
            return chatlist;
        } catch (DataAccessException e) {
            log.error("Data access error: {}", e.getMessage());
            throw new ChatFailException("채널 조회에 실패하였습니다.", e);
        }
    }

    /* Chat 번역 */
    public MessageResponse translateMessage(SendChatDto chat, String nationLanguage) {

        MessageResponse response = WebClient.create()
                .post()
                .uri("http://localhost:8000/api/v1/trans/" + nationLanguage)
                .header("Content-Type", "application/json")
                .body(BodyInserters.fromValue(chat))
                .retrieve()
                .bodyToMono(MessageResponse.class)
                .block();

        log.info("{}", response);

        return response;
    }

    @Override
    public int saveAudioChatData(SendChatDto sendChatDto) {
        // 오디오 채팅 데이터를 저장하는 로직을 여기에 구현합니다.
        // 예시:
        // 1. sendChatDto에서 필요한 정보를 추출합니다.
        // 2. 채팅 데이터를 데이터베이스에 저장합니다.
        // 3. 필요한 경우 추가적인 처리를 수행합니다.
        return 0;
    }
}
