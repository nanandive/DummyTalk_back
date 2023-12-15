package com.example.DummyTalk.Chat.Channel.Service;

import com.example.DummyTalk.Chat.Channel.Dto.ChannelDto;
import com.example.DummyTalk.Chat.Channel.Entity.ChannelEntity;
import com.example.DummyTalk.Chat.Channel.Repository.ChannelRepository;
import com.example.DummyTalk.Chat.Server.repository.ServerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChannelServiceImpl implements ChannelService{
    private final ChannelRepository channelRepository;
    private final ServerRepository serverRepository;

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
                        .ServerId(channelEntity.getServerId())
                        .channelName(channelEntity.getChannelName())
                        .channelCount(channelEntity.getChannelCount())
                        .build())
                .collect(Collectors.toList());
    }


    /* 채널 삭제 */
    public void channelDelete(Long id) {
        channelRepository.deleteById(id);
    }





}
