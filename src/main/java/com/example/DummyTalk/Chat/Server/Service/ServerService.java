package com.example.DummyTalk.Chat.Server.Service;
import lombok.AllArgsConstructor;
import com.example.DummyTalk.Chat.Channel.Dto.ChannelDto;
import com.example.DummyTalk.Chat.Channel.Entity.ChannelEntity;
import com.example.DummyTalk.Chat.Channel.Repository.ChannelRepository;
import com.example.DummyTalk.Chat.Server.Dto.ServerDto;
import com.example.DummyTalk.Chat.Server.Dto.ServerSettingDto;
import com.example.DummyTalk.Chat.Server.Entity.ServerEntity;
import com.example.DummyTalk.Chat.Server.repository.ServerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
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


    /* 서버리스트 */
    public List<ServerDto> findAllServer() {
        List<ServerEntity> serverEntityList = serverRepository.findAll();
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

    /* 서버 생성 */
    public void createServer(ServerDto serverDto, MultipartFile file) throws Exception{
        if(file != null && !file.isEmpty()){
            String filePath = absolutePath;
            UUID uuid = UUID.randomUUID();
            String fileName = uuid + "_" + file.getOriginalFilename();

            File saveFile = new File(filePath, fileName);
            file.transferTo(saveFile);

            serverDto.setFileName(fileName);
            serverDto.setFilePath(resourcePath + filePath);
        }
            ServerEntity serverEntity = convertToEntity(serverDto);
            serverRepository.save(serverEntity);
            System.out.println("서버 저장 : >>>>>>>>> " + serverEntity);

    }

    public void createServer(ServerDto serverDto) throws Exception {
        ServerEntity serverEntity = convertToEntity(serverDto);
        serverRepository.save(serverEntity);
        System.out.println("서버 저장 : >>>>>>>>> " + serverEntity);
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
