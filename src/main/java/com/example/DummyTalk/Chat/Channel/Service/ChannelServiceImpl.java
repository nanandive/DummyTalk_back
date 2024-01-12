package com.example.DummyTalk.Chat.Channel.Service;

import java.util.List;
import java.util.stream.Collectors;

import com.example.DummyTalk.User.Entity.User;
import com.example.DummyTalk.User.Entity.UserChat;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
                .build();
    }

    @Transactional
    /* 채널 리스트 */
    @Override
    public List<ChannelDto> findByChannelList(Long serverId, Long userId) {

        User user = userRepository.findByUserId(userId);
        
        for (UserChat data : user.getUserChats()){
            if(data.getServer().getId().equals(serverId)){

                // 데이터베이스에서 채널 리스트를 조회
                return channelRepository.findByServerId(serverId)
                        .stream()
                        .map(channelEntity -> ChannelDto.builder()
                                .channelId(channelEntity.getChannelId())
                                .serverId(channelEntity.getServerId())
                                .channelName(channelEntity.getChannelName())
                                .channelType(channelEntity.getChannelType())
                                .build())
                        .collect(Collectors.toList());
            }
        }

        throw new RuntimeException("잘못된 접근입니다.");
    }


    /* 채널 삭제 */
    public void channelDelete(Long id) {
        channelRepository.deleteById(id);
    }

    /* Entity -> Dto 변환 */
    private ChannelDto converToDto(ChannelEntity channelEntity) {
        return ChannelDto.builder()
                .channelName(channelEntity.getChannelName())
                .build();
    }

    /* 채널명 조회 */
    public ChannelDto getChannelName(Long channelId) {
        ChannelEntity channelEntity = channelRepository.findByChannelId(channelId);
        return ChannelDto.builder()
                .channelName(channelEntity.getChannelName())
                .build();
    }




    /*채널 타입 로직추가 */
    public ChannelDto createChannelType(ChannelDto channelDto) {
        ChannelEntity.ChannelType type = channelDto.getChannelType();
        if (type == null) {
            type = ChannelEntity.ChannelType.TEXT; // 기본값 설정
        }

        ChannelEntity channelEntity = ChannelEntity.builder()
                .channelName(channelDto.getChannelName())
                .serverId(channelDto.getServerId())
                .channelType(type)
                .build();

        ChannelEntity responseChannelEntity = channelRepository.save(channelEntity);
        return modelMapper.map(responseChannelEntity, ChannelDto.class);
    }



    /*채널타입 라우터 불러오기*/
    public ChannelDto getChannelType(Long channelId) {
        ChannelEntity channelEntity = channelRepository.findById(channelId).orElse(null);
        if (channelEntity != null) {
            return new ChannelDto(channelEntity.getChannelId(),
                    channelEntity.getServerId(),
                    channelEntity.getChannelName(),
                    channelEntity.getChannelCount(),
                    channelEntity.getChannelType());
        }
        return null;
    }

}





//    public void createChannel(ChannelDto channelDto) {
//        ChannelEntity channelEntity = ChannelEntity.builder()
//                .channelId(channelDto.getChannelId())
//                .serverId(channelDto.getServerId())
//                .channelName(channelDto.getChannelName())
//                .channelCount(channelDto.getChannelCount())
//                .channelType(channelDto.getChannelType()) // channelType 설정
//                .build();
//
//        channelRepository.save(channelEntity);
//    }



