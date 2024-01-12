package com.example.DummyTalk.Chat.Server.Service;
import com.example.DummyTalk.Chat.Channel.Dto.ChannelDto;
import com.example.DummyTalk.Chat.Channel.Entity.ChannelEntity;
import com.example.DummyTalk.Chat.Channel.Repository.ChannelRepository;
import com.example.DummyTalk.Chat.Server.Dto.ServerDto;
import com.example.DummyTalk.Chat.Server.Dto.ServerSettingDto;
import com.example.DummyTalk.Chat.Server.Entity.ServerEntity;
import com.example.DummyTalk.Chat.Server.repository.ServerRepository;
import com.example.DummyTalk.User.DTO.UserDTO;
import com.example.DummyTalk.User.DTO.UserServerCodeDto;
import com.example.DummyTalk.User.Entity.User;
import com.example.DummyTalk.User.Entity.UserChat;
import com.example.DummyTalk.User.Entity.UserServerCode;
import com.example.DummyTalk.User.Repository.UserChatRepository;
import com.example.DummyTalk.User.Repository.UserRepository;
import com.example.DummyTalk.User.Repository.UserServerCodeRepository;
import jakarta.persistence.Id;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.modelmapper.TypeToken;
import org.apache.catalina.Server;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Type;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
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
    private final UserServerCodeRepository userServerCodeRepository;

    private Map<String, ServerEntity> server = new ConcurrentHashMap<>();

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
                .invitedCode(serverEntity.getInvitedCode())
                .userName(serverEntity.getUserName())
                .maxUsers(serverEntity.getMaxUsers())
                .currentUsers(serverEntity.getCurrentUsers())
                .build();
    }

    @Transactional
    /* 서버 생성 */
    public ServerDto createServer(ServerDto serverDto, MultipartFile file, Long userId) throws Exception{
        System.out.println(">>>>>>>>> userInfoDto (서비스)" );


        // 서버 초대 코드생성
        String invitedCode = UUID.randomUUID().toString();
        serverDto.setInvitedCode(invitedCode);


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


        /* 서버 생성시 채널 생성 */
        List<String> channelNames = Arrays.asList("일반", "1:1 음성 번역");
        for (String channelName : channelNames) {
            ChannelEntity channelEntity = new ChannelEntity();
            channelEntity.setChannelName(channelName);
            channelEntity.setServerId(serverEntity1.getId());
            if(channelName.equals("일반")) {
                channelEntity.setChannelType(ChannelEntity.ChannelType.TEXT);
            }else {
                channelEntity.setChannelType(ChannelEntity.ChannelType.VOICE);
            }
            channelRepository.save(channelEntity);
        }

        // UserChat 테이블에 insert
        UserChat userChat = new UserChat();
        User savedUser = userRepository.findByUserId(userId);
        userChat.setUser(savedUser);
        userChat.setServer(serverEntity1);
        userChatRepository.save(userChat);
        System.out.println("서버 저장 : 이미지 존재 (서비스) : >>>>>>>>> " + serverEntity);
        System.out.println("서버 저장 : 이미지 존재 (서비스)  : >>>>>>>>> " + userChat);


        // 서버 생성시 방장은 초대코드 자동 저장
        UserServerCode userServerCode = new UserServerCode();
        userServerCode.setServerCode(invitedCode);
        userServerCode.setUser(savedUser);
        System.out.println("유저 코드 테이블에 유저정보 저장 (서비스)>>>>>>>>>>>>>>>> : " + userServerCode);
        userServerCodeRepository.save(userServerCode);

        return modelMapper.map(serverEntity, ServerDto.class);

    }

    // 파일이 존재하지 않을시 서버 생성
    public ServerDto createServer(ServerDto serverDto) throws Exception {

        // 서버 초대 코드생성
        String invitedCode = UUID.randomUUID().toString();
        serverDto.setInvitedCode(invitedCode);

        ServerEntity serverEntity = convertToEntity(serverDto);
        User user = userRepository.findByUserId(serverDto.getUserId());
        serverEntity = serverRepository.save(serverEntity);

        List<String> channelNames = Arrays.asList("일반", "1:1 음성 번역");
        for (String channelName : channelNames) {
            ChannelEntity channelEntity = new ChannelEntity();
            channelEntity.setChannelName(channelName);
            channelEntity.setServerId(serverEntity.getId());
            if(channelName.equals("일반")) {
                channelEntity.setChannelType(ChannelEntity.ChannelType.TEXT);
            }else {
                channelEntity.setChannelType(ChannelEntity.ChannelType.VOICE);
            }
            channelRepository.save(channelEntity);
        }


        UserChat userChat = new UserChat();
        userChat.setServer(serverEntity);
        userChat.setUser(user);
        userChatRepository.save(userChat);

        // 서버 생성시 방장은 초대코드 자동 저장
        UserServerCode userServerCode = new UserServerCode();
        userServerCode.setServerCode(invitedCode);
        userServerCode.setUser(user);
        System.out.println("유저 코드 테이블에 유저정보 저장 (서비스)>>>>>>>>>>>>>>>> : " + userServerCode);
        userServerCodeRepository.save(userServerCode);


        System.out.println("서버 저장 : >>>>>>>>> " + serverEntity);

        return modelMapper.map(serverEntity, ServerDto.class);
    }

    private ServerEntity convertToEntity(ServerDto serverDto) {
        return ServerEntity.builder()
                .serverName(serverDto.getServerName())
                .userName(serverDto.getUserName())
                .userId(serverDto.getUserId())
                .invitedCode(serverDto.getInvitedCode())
                .filePath(serverDto.getFilePath())
                .fileName(serverDto.getFileName())
                .maxUsers(serverDto.getMaxUsers())
                .currentUsers(serverDto.getCurrentUsers())
                .build();
    }

    /* 서버 상세보기 */
    public ServerDto findById(Long id) {
        Optional<ServerEntity> optionalServerEntity = serverRepository.findById(id);

        Type channelListType = new TypeToken<List<ChannelDto>>() {}.getType();

        if (optionalServerEntity.isPresent()) {
            ServerEntity serverEntity = optionalServerEntity.get();
            return ServerDto.builder()
                    .id(serverEntity.getId())
                    .userId(serverEntity.getUserId())
                    .serverName(serverEntity.getServerName())
                    .maxUsers(serverEntity.getMaxUsers())
                    .invitedCode(serverEntity.getInvitedCode())
                    .userName(serverEntity.getUserName())
                    .currentUsers(serverEntity.getCurrentUsers())
                    .channelDtoList(modelMapper.map(serverEntity.getChannelEntityList(), channelListType))
                    .build();
        } else {
            return null;
        }
    }

    /* 서버 수정 */
    @Transactional
    public void updateServer(ServerSettingDto serverSettingDto) {
        System.out.println(">>>>>>>>>>>>>>>>, " + serverSettingDto.getServerId());
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
    public void serverDelete(Long id, Long userId) {
        Optional<ServerEntity> serverId = serverRepository.findById(id);
        Long mainUserId = serverId.get().getUserId();
        if (mainUserId.equals(userId)) {
            serverRepository.deleteById(id);
        }
        System.out.println("서버 삭제 권한이 없습니다.");
    }

    /* 서버 초대 */
    public void addUserInvitedCode(UserServerCodeDto userServerCodeDto) {
        // userEmail를 가지고 userId를 조회
        String userEmail = userServerCodeDto.getUserEmail();
        User user = userRepository.findByUserEmail(userEmail);
        User userId = userRepository.findById(user.getUserId())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없음"));

        System.out.println("1-1>>>>>>>>>>>>>>>> : " + userId);
        System.out.println("dto>>>>>>>>>>>>>>>" + userServerCodeDto.getServerId());

        // serverId를 가지고 서버 조회
        ServerEntity serverEntity = serverRepository.findById(userServerCodeDto.getServerId())
                .orElseThrow(() -> new RuntimeException("서버를 찾을 수 없음"));
        String invitedCode = serverEntity.getInvitedCode();

        System.out.println("1-2>>>>>>>>>>>>> : " + invitedCode);

        UserServerCode userServerCode = UserServerCode.builder()
                .user(userId)
                .serverCode(invitedCode)
                .build();
        System.out.println("2.(서비스) >>>>>>>>>>>>> : " + userServerCode.getServerCode());

        // UserChat 테이블에 insert
        UserChat userChat = new UserChat();
        User savedUser = userRepository.findByUserId(user.getUserId());
        userChat.setUser(savedUser);
        userChat.setServer(serverEntity);
        userChatRepository.save(userChat);

        userServerCodeRepository.save(userServerCode);

    }

    /* 서버에 초대된 유저 리스트 */
    public List<UserDTO> getInvitedUser(Long serverId) {
        // 서버Id에 해당되는 서버를 UserChat에서 찾는다.
        List<UserChat> userChatList = userChatRepository.findByServerId(serverId);
        System.out.println("서버에 초대된 유저 리스트(서비스) >>>>>>>>>>>>>>> : " + userChatList);

        List<UserDTO> invitedUsers = userChatList.stream()
                .map(userChat -> UserConvertToDto(userChat.getUser()))
                .collect(Collectors.toList());
        return invitedUsers;
    }

    private UserDTO UserConvertToDto(User user){
        return UserDTO.builder()
                .userEmail(user.getUserEmail())
                .nickname(user.getNickname())
                .build();
    }

    @Transactional
    /* 서버 초대된 유저 강퇴 */
    public void deleteUser(long serverId, String userEmail) {
        User users = userRepository.findByUserEmail(userEmail);
        Long userId = users.getUserId();
        User user = userRepository.findById(userId).orElseThrow();
        ServerEntity server = serverRepository.findById(serverId).orElseThrow();

        System.out.println("(서비스)>>>>>>>>>>>>>>> : userId" + userId);
        userChatRepository.deleteByUserAndServer(user, server);

    }
}
