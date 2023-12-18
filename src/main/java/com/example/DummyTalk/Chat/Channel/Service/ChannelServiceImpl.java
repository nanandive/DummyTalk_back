package com.example.DummyTalk.Chat.Channel.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.example.DummyTalk.Chat.Channel.Dto.MessageHistoryDto;
import com.example.DummyTalk.Chat.Channel.Dto.SendChatDto;
import com.example.DummyTalk.Chat.Channel.Entity.ChatDataEntity;
import com.example.DummyTalk.Exception.ChatFailException;
import com.example.DummyTalk.User.DTO.ChatSenderDTO;
import com.example.DummyTalk.User.Entity.User;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.DummyTalk.Chat.Channel.Controller.MessageResponse;
import com.example.DummyTalk.Chat.Channel.Dto.ChannelDto;
import com.example.DummyTalk.Chat.Channel.Entity.ChannelEntity;
import com.example.DummyTalk.Chat.Channel.Repository.ChannelRepository;
import com.example.DummyTalk.Chat.Channel.Repository.ChatRepository;
import com.example.DummyTalk.Chat.Server.repository.ServerRepository;
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


}
