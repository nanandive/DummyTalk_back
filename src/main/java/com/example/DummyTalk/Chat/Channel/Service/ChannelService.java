package com.example.DummyTalk.Chat.Channel.Service;

import com.example.DummyTalk.Chat.Channel.Dto.ChannelDto;
import com.example.DummyTalk.Chat.Channel.Dto.ChatListDto;
import com.example.DummyTalk.Chat.Channel.Dto.SendChatDto;
import com.example.DummyTalk.Chat.Channel.Entity.ChannelEntity;
import com.example.DummyTalk.Chat.Channel.Entity.ChatDataEntity;
import com.example.DummyTalk.Chat.Channel.Repository.ChannelRepository;
import com.example.DummyTalk.Chat.Channel.Repository.ChatRepository;
import com.example.DummyTalk.User.DTO.ChatSenderDTO;
import com.example.DummyTalk.User.Entity.User;
import com.example.DummyTalk.User.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChannelService {

    private final ChannelRepository channelRepository;
    private final ChatRepository chatRepository;
    private final UserRepository userResponsitory;
    private final ModelMapper modelMapper;


    /* DB에서 채널 list 가져오기 */
    public List<ChannelDto> findAllChannel(Long id) {
        List<ChannelEntity> channelEntities = channelRepository.findAll();

        return channelEntities.stream()
                .map(this::converToDto)
                .collect((Collectors.toList()));
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
                .userName(user.getUserName())
                .nickname(user.getNickname())
                .userImgPath(user.getUserImgPath())
                .build();
    }


    /* 채팅 데이터 Entity -> Dto 변환 */
    private ChatListDto chatToDto(ChatDataEntity chat) {
        return ChatListDto.builder()
                .message(chat.getMessage())
                .chatId(chat.getChatId())
                .createdAt(chat.getCreatedAt())
                .updatedAt(chat.getUpdatedAt())
                .sender(userToDto(chat.getSender()))
                .build();
    }


    /* 채팅 내용 저장 (DB) */
    @Transactional
    public void saveChatData(SendChatDto message) {
        // sender 조회
        log.info("saveChatData message ============================== " + message);
        User user = userResponsitory.findByUserId(message.getSender());
        log.info("saveChatData user ============================== " + user);
        ChannelEntity channel = channelRepository.findByChannelId((long) message.getChannelId());
        log.info("saveChatData channel ============================== " + channel);

        ChatDataEntity chatEntity = ChatDataEntity.builder()
                .channelId(channel)
                .message(message.getMessage())
                .sender(user)
                .language("en")
                .build();

        log.info("saveChatData chatEntity ============================== " + chatEntity);

        chatRepository.save(chatEntity);
    }


    /* 채널 아이디로 조회한 채널 리스트 */
    public List<ChatListDto> findChatData(int channelId) {
        ChannelEntity channelEntity = channelRepository.findByChannelId((long) channelId);
        log.info("findChatData channelEntity ============================={}", channelEntity);

        List<ChatListDto> chatlist =
                chatRepository.findByChannelId(channelEntity).stream()
                        .map(this::chatToDto)
                        .collect(Collectors.toList());
        log.info("findChatData chatlist ============================={}", chatlist);
        return chatlist;
    }
}