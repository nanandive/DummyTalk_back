package com.example.DummyTalk.Chat.Channel.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.DummyTalk.Chat.Channel.Controller.MessageResponse;
import com.example.DummyTalk.Chat.Channel.Dto.ChannelDto;
import com.example.DummyTalk.Chat.Channel.Dto.ChatListDto;
import com.example.DummyTalk.Chat.Channel.Dto.SendChatDto;
import com.example.DummyTalk.Chat.Channel.Entity.ChannelEntity;
import com.example.DummyTalk.Chat.Channel.Entity.ChatDataEntity;
import com.example.DummyTalk.Chat.Channel.Repository.ChannelRepository;
import com.example.DummyTalk.Chat.Channel.Repository.ChatRepository;
import com.example.DummyTalk.Chat.Server.repository.ServerRepository;
import com.example.DummyTalk.Exception.ChatFailException;
import com.example.DummyTalk.User.DTO.ChatSenderDTO;
import com.example.DummyTalk.User.Entity.User;
import com.example.DummyTalk.User.Repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChannelServiceImpl implements ChannelService {
    private final ChannelRepository channelRepository;
    private final ServerRepository serverRepository;
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Transactional
    /* 채널 생성 */
    public void createChannel(ChannelDto channelDto) {
        ChannelEntity channelEntity = convertToEntity(channelDto);
        channelRepository.save(channelEntity);
        System.out.println("채널 생성(서버) >>>>>>>>> : " + channelEntity);
    }

    private ChannelEntity convertToEntity(ChannelDto channelDto) {
        return ChannelEntity.builder()
                .serverId(channelDto.getServerId())
                .channelName(channelDto.getChannelName())
                .channelCount(channelDto.getChannelCount())
                .build();
    }

    @Transactional
    /* 채널 리스트 */
    @Override
    public List<ChannelDto> findByChannelList(Long serverId) {
        // 데이터베이스에서 채널 리스트를 조회
        return channelRepository.findByServerId(serverId)
                .stream()
                .map(channelEntity -> ChannelDto.builder()
                        .channelId(channelEntity.getChannelId())
                        .serverId(channelEntity.getServerId())
                        .channelName(channelEntity.getChannelName())
                        .channelCount(channelEntity.getChannelCount())
                        .build())
                .collect(Collectors.toList());
    }

    /* 채널 삭제 */
    public void channelDelete(Long id) {
        channelRepository.deleteById(id);
    }

    /* Entity -> Dto 변환 */
    private ChannelDto converToDto(ChannelEntity channelEntity) {
        return ChannelDto.builder()
                .channelName(channelEntity.getChannelName())
                .channelCount(channelEntity.getChannelCount())
                .build();
    }

    /* 채팅 데이터에 들어가는 유저 정보 Entity -> Dto 변환 */
    private ChatSenderDTO userToDto(User user) {
        return ChatSenderDTO.builder()
                .sender((long) user.getUserId())
                .userName(user.getName())
                .nickname(user.getNickname())
                .userImgPath(user.getUserImgPath())
                .build();
    }

    /* 채팅 데이터 Entity -> Dto 변환 */
    private ChatListDto chatToDto(ChatDataEntity chat) {
        log.info("saveChatData chatToDto ============================== " + chat);
        return ChatListDto.builder()
                .message(chat.getMessage())
                .chatId(chat.getChatId())
                .createdAt(chat.getCreatedAt())
                .updatedAt(chat.getUpdatedAt())
                .sender(userToDto(chat.getSender()))
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

    /* 채널 아이디로 조회한 채널 리스트 */
    public List<ChatListDto> findChatData(int channelId) {
        ChannelEntity channelEntity = Optional.ofNullable(channelRepository.findByChannelId((long) channelId))
                .orElseThrow(() -> new ChatFailException("채널 조회에 실패하였습니다."));
        log.info("findChatData channelEntity ============================={}", channelEntity);
        try {
            List<ChatListDto> chatlist =
                    chatRepository.findAllByChannelId(channelEntity).stream()
                            .map(this::chatToDto)
                            .collect(Collectors.toList());
            log.info("findChatData chatlist ============================={}", chatlist.size());
            return chatlist;
        } catch (DataAccessException e) {
            log.error("Data access error: {}", e.getMessage());
            throw new ChatFailException("채널 조회에 실패하였습니다.", e);
        }
    }

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
}
