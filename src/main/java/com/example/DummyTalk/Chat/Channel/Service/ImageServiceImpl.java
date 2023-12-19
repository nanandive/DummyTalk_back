package com.example.DummyTalk.Chat.Channel.Service;

import com.example.DummyTalk.Chat.Channel.Dto.ImageDto;
import com.example.DummyTalk.Chat.Channel.Entity.ChannelEntity;
import com.example.DummyTalk.Chat.Channel.Entity.ChatDataEntity;
import com.example.DummyTalk.Chat.Channel.Entity.ImageEntity;
import com.example.DummyTalk.Chat.Channel.Repository.ChannelRepository;
import com.example.DummyTalk.Chat.Channel.Repository.ChatRepository;
import com.example.DummyTalk.Chat.Channel.Repository.ImageRepository;
import com.example.DummyTalk.Exception.ChatFailException;
import com.example.DummyTalk.User.DTO.UserDTO;
import com.example.DummyTalk.User.Entity.User;
import com.example.DummyTalk.User.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageServiceImpl implements ImageService {
    @Value("${chatAbsolutePath.dir}")
    private String absolutePath;

    @Value("${chatResourcePath.dir}")
    private String resourcePath;

    private final UserRepository userRepository;
    private final ImageRepository imageRepository;
    private final ChannelRepository channelRepository;
    private final ChatRepository chatRepository;

    private UserDTO convertToUserDto(User user) {
        return UserDTO.builder()
                .userId(user.getUserId())
                .nickname(user.getNickname())
                .build();
    }

    private ImageEntity convertToImageEntity(ImageDto img, String fileName, String originFileName, String path) {
        return ImageEntity.builder()
                .channelId((long) img.getChannelId())
                .originalFileName(originFileName)
//                .originalFileName(originFileName.split("\\.")[0])
                .filePath(path + fileName)  // 로컬 저장 경로 (추후 상대 경로로 수정)
                .build();
    }

    private ChatDataEntity convertToChatEntity(User user, ChannelEntity channel, String filePath) {
        return ChatDataEntity.builder()
                .sender(user)
                .channelId(channel)
                .message(filePath)
                .type("IMAGE")
                .build();
    }

    /****
     * 로컬 저장 경로
     * uuid_기존파일명.확장자
     */
    @Override
    public void saveImage(ImageDto imageDto) {

        if (imageDto.getFileInfo() == null || imageDto.getFileInfo().length == 0)
            throw new ChatFailException("이미지 파일이 없습니다.");

        for (MultipartFile file : imageDto.getFileInfo()) {

            String fileName = UUID.randomUUID().toString();
            fileName = fileName + "_" + Objects.requireNonNull(file.getOriginalFilename()).replace("_", "");
            log.info("\nsaveImage fileName : \n" + fileName);

            File saveFile = new File(absolutePath, fileName);       // 저장할 파일 경로
            log.info("\nsaveImage saveFile : \n" + saveFile);

            try {
                file.transferTo(saveFile);
                ImageEntity imageEntity = convertToImageEntity(imageDto, fileName, file.getOriginalFilename(), absolutePath);
                imageRepository.save(imageEntity);
                saveImageToChat(imageDto, imageEntity.getFilePath());
            } catch (Exception e) {
                throw new ChatFailException("파일 저장에 실패하였습니다.");
            }
        }

    }

    // 이미지 -> 채팅 데이터로 저장 (채팅 데이터에 이미지 정보 저장)
    public void saveImageToChat(ImageDto imageDto, String filePath) {
        User user = Optional.ofNullable(userRepository.findByUserId((long) imageDto.getUserId()))
                .orElseThrow(() -> new ChatFailException("유저 조회에 실패하였습니다."));
        log.info("\nimageToChatData user  \n" + user);

        ChannelEntity channel = Optional.ofNullable(channelRepository.findByChannelId((long) imageDto.getChannelId()))
                .orElseThrow(() -> new ChatFailException("채널 조회에 실패하였습니다."));
        log.info("\nimageToChatData channel \n" + user);
        try {
            ChatDataEntity chatEntity = convertToChatEntity(user, channel, filePath);
            chatRepository.save(chatEntity);
        } catch (Exception e) {
            throw new ChatFailException("이미지 저장에 실패하였습니다.");
        }
    }
}
