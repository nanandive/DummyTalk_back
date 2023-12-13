package com.example.DummyTalk.Chat.Server.Service;

import com.example.DummyTalk.Chat.Channel.Dto.ChannelDto;
import com.example.DummyTalk.Chat.Channel.Entity.ChannelEntity;
import com.example.DummyTalk.Chat.Channel.Repository.ChannelRepository;
import com.example.DummyTalk.Chat.Server.Dto.ServerDto;
import com.example.DummyTalk.Chat.Server.Entity.ServerEntity;
import com.example.DummyTalk.Chat.Server.repository.ServerRepository;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.Server;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.io.Console;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ServerService {
    private final ServerRepository serverRepository;
    private final ChannelRepository channelRepository;


    /* 서버리스트 */
    public List<ServerDto> findAllServer() {
        List<ServerEntity> serverEntityList = serverRepository.findAll();
        return serverEntityList.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private ServerDto convertToDto(ServerEntity serverEntity) {
        return ServerDto.builder()
                .serverName(serverEntity.getServerName())
                .invitedCode(serverEntity.getInvitedCode())
                .userName(serverEntity.getUserName())
                .build();
    }

    /* 서버 생성 */
    public void createServer(ServerDto serverDto) {
        ServerEntity serverEntity = convertToEntity(serverDto);
        serverRepository.save(serverEntity);
    }

    private ServerEntity convertToEntity(ServerDto serverDto) {

        return ServerEntity.builder()
                .serverName(serverDto.getServerName())
                .userName(serverDto.getUserName())
                .invitedCode(serverDto.getInvitedCode())
                .userCount(serverDto.getUserCount())
                .build();
    }

    /* 서버 상세보기 */
    public ServerDto findbyId(Long id) {
        Optional<ServerEntity> optionalServerEntity = serverRepository.findById(id);

        /* 채널 List 불러오기 */
        List<ChannelEntity> channelEntities = channelRepository.findByServerEntity_Id(id);
        List<ChannelDto> channelDtoList = channelEntities.stream()
                .map(channelEntity -> ChannelDto.builder()
                        .channelName(channelEntity.getChannelName())
                        .channelCount(channelEntity.getChannelCount())
                        .build())
                .collect(Collectors.toList());

        if (optionalServerEntity.isPresent()) {
            ServerEntity serverEntity = optionalServerEntity.get();
            return ServerDto.builder()
                    .serverName(serverEntity.getServerName())
                    .userCount(serverEntity.getUserCount())
                    .invitedCode(serverEntity.getInvitedCode())
                    .userName(serverEntity.getUserName())
                    .channelDtoList(channelDtoList)
                    .build();
        } else {
            return null;
        }
    }
}
