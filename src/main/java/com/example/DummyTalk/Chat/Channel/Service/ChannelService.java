package com.example.DummyTalk.Chat.Channel.Service;

import com.example.DummyTalk.Chat.Channel.Dto.ChannelDto;
import com.example.DummyTalk.Chat.Channel.Dto.ChatDataDto;
import com.example.DummyTalk.Chat.Channel.Entity.ChannelEntity;
import com.example.DummyTalk.Chat.Channel.Entity.ChatDataEntity;
import com.example.DummyTalk.Chat.Channel.Repository.ChannelRepository;
import com.example.DummyTalk.Chat.Channel.Repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
//        chatRepository.save(ChatDataEntity.toEntity(message));
    }



}
