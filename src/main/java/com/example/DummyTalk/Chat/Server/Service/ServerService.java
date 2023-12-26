package com.example.DummyTalk.Chat.Server.Service;
import com.example.DummyTalk.Chat.Channel.Entity.ChannelEntity;
import com.example.DummyTalk.Chat.Channel.Repository.ChannelRepository;
import com.example.DummyTalk.Chat.Server.Dto.ServerDto;
import com.example.DummyTalk.Chat.Server.Dto.ServerSettingDto;
import com.example.DummyTalk.Chat.Server.Entity.ServerEntity;
import com.example.DummyTalk.Chat.Server.repository.ServerRepository;
import com.example.DummyTalk.User.Entity.User;
import com.example.DummyTalk.User.Entity.UserChat;
import com.example.DummyTalk.User.Repository.UserChatRepository;
import com.example.DummyTalk.User.Repository.UserRepository;
import jakarta.persistence.Id;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.catalina.Server;
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
                .maxUsers(serverEntity.getMaxUsers())
                .currentUsers(serverEntity.getCurrentUsers())
                .build();
    }

    @Transactional
    /* 서버 생성 */
    public ServerDto createServer(ServerDto serverDto, MultipartFile file, Long userId) throws Exception{
        System.out.println(">>>>>>>>> userInfoDto (서비스)" );

        // 파일존재시
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

        // 파일 존재 후 그 이후 생성
        ServerEntity serverEntity = convertToEntity(serverDto);
        ServerEntity serverEntity1 = serverRepository.save(serverEntity);

//
//        /* 서버 생성시 채널 생성 */
//        List<String> channelNames = Arrays.asList("일반", "1:1 음성 번역");
//        for (String channelName : channelNames) {
//            ChannelEntity channelEntity = new ChannelEntity();
//            channelEntity.setChannelName(channelName);
//            channelEntity.setServerId(serverEntity1.getId());
//            channelRepository.save(channelEntity);
//        }

        // UserChat 테이블에 insert
        UserChat userChat = new UserChat();
        User savedUser = userRepository.findByUserId(userId);


        System.out.println("savedUser  " + savedUser);
        System.out.println("serverEntity1   " +serverEntity1);
        userChat.setUser(savedUser);
        userChat.setServer(serverEntity1);

        userChatRepository.save(userChat);
        System.out.println("서버 저장 : 이미지 존재 (서비스) : >>>>>>>>> " + serverEntity);
        System.out.println("서버 저장 : 이미지 존재 (서비스)  : >>>>>>>>> " + userChat);

        return modelMapper.map(serverEntity, ServerDto.class);

    }

    // 파일이 존재하지 않을시 서버 생성
    @Transactional
    public ServerDto createServer(ServerDto serverDto) throws Exception {
        ServerEntity serverEntity = convertToEntity(serverDto);
        User user = userRepository.findByUserId(serverDto.getUserId());
        serverEntity = serverRepository.save(serverEntity);

        List<String> channelNames = Arrays.asList("일반", "1:1 음성 번역");
        for (String channelName : channelNames) {
            ChannelEntity channelEntity = new ChannelEntity();
            channelEntity.setChannelName(channelName);
            channelEntity.setServerId(serverEntity.getId());
            channelRepository.save(channelEntity);
        }


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
                .filePath(serverDto.getFilePath())
                .fileName(serverDto.getFileName())
                .maxUsers(serverDto.getMaxUsers())
                .currentUsers(serverDto.getCurrentUsers())
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
                    .maxUsers(serverEntity.getMaxUsers())
                    .invitedUser(serverEntity.getInvitedUser())
                    .userName(serverEntity.getUserName())
                    .currentUsers(serverEntity.getCurrentUsers())
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

    @Transactional
    /* 서버 삭제 */
    public void serverDelete(Long id) {
        serverRepository.deleteById(id);
    }

//    /* 서버에 접속중인 유저 수 제한 */
//    public void updateMaxUser(Long id) {
//        ServerEntity serverEntity = serverRepository.findById(id).orElseThrow(()->
//                new RuntimeException(">>>>>>>>>>>>>>>>>>>>>>>(서비스)서버를 찾을수 없다"));
//        int currentUsers = serverEntity.getcurrentUsers();
//        int maxUser = serverEntity.getMaxUsers();
//
//        if(currentUsers < maxUser) {
//            serverEntity.setcurrentUsers(currentUsers + 1);
//            serverRepository.save(serverEntity);
//        }else {
//            throw new RuntimeException(">>>>>>>>>>>>>>>>(서비스) 서버 최대 사용자 수 초과");
//        }
//    }

}
