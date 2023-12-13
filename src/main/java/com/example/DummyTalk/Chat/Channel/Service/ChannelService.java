package com.example.DummyTalk.Chat.Channel.Service;

import com.example.DummyTalk.Chat.Channel.Dto.ChannelDto;
import com.example.DummyTalk.Chat.Channel.Dto.ChatDataDto;
import com.example.DummyTalk.Chat.Channel.Entity.ChannelEntity;
import com.example.DummyTalk.Chat.Channel.Entity.ChatDataEntity;
import com.example.DummyTalk.Chat.Channel.Repository.ChannelRepository;
import com.example.DummyTalk.Chat.Channel.Repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChannelService {

    private final ChannelRepository channelRepository;
    private final ChatRepository chatRepository;
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










    /* 채팅 내용 저장 (DB) */
    @Transactional
    public void saveChatData(ChatDataDto message){
        log.info("SendChatDto : " + message);
        // sender, message, language, channelId
        chatRepository.save(convert(message));
    }


    private ChatDataEntity convert(ChatDataDto chatDataDto) {

        return ChatDataEntity.builder()
                .message(chatDataDto.getMessage())
                .sender(chatDataDto.getSender())
                .language(chatDataDto.getLanguage())
                .build();


    }

    public List<ChatDataDto> findChatData(Long channelId) {
        Optional<ChannelEntity> channelEntity =  channelRepository.findById(channelId);


        List<ChatDataDto> chatlist =  chatRepository.findByChannelId(channelEntity).stream()
                .map(chatDataEntity -> ChatDataDto.builder()
                        .message(chatDataEntity.getMessage())
                        .sender(chatDataEntity.getSender())
                        .language(chatDataEntity.getLanguage())
                        .build())
                .collect(Collectors.toList());
        log.info("findChatData ============================={}", chatlist);
        return chatlist;
    }
}
