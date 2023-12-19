package com.example.DummyTalk.Chat.Server.Service;
import com.example.DummyTalk.Chat.Channel.Repository.ChannelRepository;
import com.example.DummyTalk.Chat.Server.Dto.ServerDto;
import com.example.DummyTalk.Chat.Server.Dto.ServerSettingDto;
import com.example.DummyTalk.Chat.Server.Entity.ServerEntity;
import com.example.DummyTalk.Chat.Server.repository.ServerRepository;
import com.example.DummyTalk.User.Entity.User;
import com.example.DummyTalk.User.Entity.UserChat;
import com.example.DummyTalk.User.Repository.UserChatRepository;
import com.example.DummyTalk.User.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ServerService {
    @Value("${serverAbsolutePath.dir}")
    private String absolutePath;

    @Value("${serverResourcePath.dir}")
    private String resourcePath;

    private final ServerRepository serverRepository;
    private final ChannelRepository channelRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final UserChatRepository userChatRepository;

    /* 서버리스트 */
    public List<ServerDto> findServerIdByUserId(Long userId) {
        List<ServerEntity> serverEntityList = serverRepository.findServersByUserId(userId);
        System.out.println("서버 리스트 불러오기 성공 (서비스) >>>>>>>>> : " + serverEntityList );
        return serverEntityList.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

    }

    private ServerDto convertToDto(ServerEntity serverEntity) {
        return ServerDto.builder()
                .id(serverEntity.getId())
                .serverName(serverEntity.getServerName())
                .invitedUser(serverEntity.getInvitedUser())
                .userName(serverEntity.getUserName())
                .build();
    }

    @Transactional
    /* 서버 생성 */
    public ServerDto createServer(ServerDto serverDto, MultipartFile file, Long userId) throws Exception{
        System.out.println(">>>>>>>>> userInfoDto (서비스)" );
        if(file != null && !file.isEmpty()){
            String filePath = absolutePath;
            UUID uuid = UUID.randomUUID();
            String fileName = uuid + "_" + file.getOriginalFilename();

            //Files.createDirectories(filePath,);
            File saveFile = new File(filePath, fileName);
            file.transferTo(saveFile);

            serverDto.setFileName(fileName);
            serverDto.setFilePath(resourcePath + filePath);
        }
            ServerEntity serverEntity = convertToEntity(serverDto);
            ServerEntity serverEntity1 = serverRepository.save(serverEntity);

            // UserChat에 insert
            UserChat userChat = new UserChat();
            User savedUser = userRepository.findByUserId(userId);


            System.out.println("savedUser  " + savedUser);
            System.out.println("serverEntity1   " +serverEntity1);
            userChat.setUser(savedUser);
            userChat.setServer(serverEntity1);

            userChatRepository.save(userChat);
            System.out.println("서버 저장 : >>>>>>>>> " + serverEntity);
            System.out.println("서버 저장 : >>>>>>>>> " + userChat);

            return modelMapper.map(serverEntity, ServerDto.class);

    }

    @Transactional
    public ServerDto createServer(ServerDto serverDto) throws Exception {
        ServerEntity serverEntity = convertToEntity(serverDto);
        User user = userRepository.findByUserId(serverDto.getUserId());
        serverEntity = serverRepository.save(serverEntity);

        UserChat userChat = new UserChat();
        userChat.setServer(serverEntity);
        userChat.setUser(user);

        userChatRepository.save(userChat);

        System.out.println("서버 저장 : >>>>>>>>> " + serverEntity);

        return modelMapper.map(serverEntity, ServerDto.class);
    }

    private ServerEntity convertToEntity(ServerDto serverDto) {
        return ServerEntity.builder()
                .serverName(serverDto.getServerName())
                .userName(serverDto.getUserName())
                .invitedUser(serverDto.getInvitedUser())
                .userCount(serverDto.getUserCount())
                .filePath(serverDto.getFilePath())
                .fileName(serverDto.getFileName())
                .build();
    }


    /* 서버 상세보기 */
    public ServerDto findById(Long id) {
        Optional<ServerEntity> optionalServerEntity = serverRepository.findById(id);

        if (optionalServerEntity.isPresent()) {
            ServerEntity serverEntity = optionalServerEntity.get();
            return ServerDto.builder()
                    .id(serverEntity.getId())
                    .serverName(serverEntity.getServerName())
                    .userCount(serverEntity.getUserCount())
                    .invitedUser(serverEntity.getInvitedUser())
                    .userName(serverEntity.getUserName())
                    .build();
        } else {
            return null;
        }
    }

    /* 서버 수정 */
    @Transactional
    public void updateServer(ServerSettingDto serverSettingDto) {

        log.info("serverSettingDto {}", serverSettingDto);
        try {
            ServerEntity serverEntity = serverRepository.findById(serverSettingDto.getServerId()).orElseThrow(() -> new IOException("Error"));
            serverEntity.setServerName(serverSettingDto.getServerName());
            System.out.println("서버 수정 처리 완료 >>>>>>>>>>> : " + serverEntity );
        } catch (IOException e) {
            System.out.println("에러 발생 : 서버 수정 에러");
            e.printStackTrace();
        }
    }

    /* 서버 삭제 */
    public void serverDelete(Long id) {
        serverRepository.deleteById(id);
    }
}
