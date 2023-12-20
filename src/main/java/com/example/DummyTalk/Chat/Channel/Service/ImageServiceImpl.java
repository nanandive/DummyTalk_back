package com.example.DummyTalk.Chat.Channel.Service;

import com.example.DummyTalk.Chat.Channel.Dto.ChatDataDto;
import com.example.DummyTalk.Chat.Channel.Dto.ImageDto;
import com.example.DummyTalk.Chat.Channel.Dto.SendChatDto;
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
import com.example.DummyTalk.Util.FileUploadUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    private final FileUploadUtils fileUploadUtils;

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
                .savedFileName(fileName)
                .filePath(path + "/" + fileName)
                .build();
    }

    private ChatDataEntity convertToChatEntity(User user, ChannelEntity channel, ImageDto imageDto) {
        return ChatDataEntity.builder()
                .sender(user)
                .channelId(channel)
                .message(imageDto.getFilePath())
                .type("IMAGE")
                .build();
    }

    private SendChatDto convertToChatDto(ChatDataEntity dto) {
        return SendChatDto.builder()
                .chatId(dto.getChatId())
                .nickname(dto.getSender().getNickname())
                .type(dto.getType())
                .message(dto.getMessage())
                .build();
    }

    private User findUserById(int userId) {
        return Optional.ofNullable(userRepository.findByUserId((long) userId))
                .orElseThrow(() -> new ChatFailException("유저 조회에 실패하였습니다."));
    }

    private ChannelEntity findChannelById(int channelId) {
        return Optional.ofNullable(channelRepository.findByChannelId((long) channelId))
                .orElseThrow(() -> new ChatFailException("채널 조회에 실패하였습니다."));
    }

    /****
     * 이미지 저장 후 채팅 데이터로 저장
     * 로컬 저장 경로 : ${chatAbsolutePath.dir}
     * uuid_기존파일명.확장자
     * @return SendChatDto
     */
    @Override
    public List<SendChatDto> saveImage(ImageDto imageDto) {

        if (imageDto.getFileInfo() == null || imageDto.getFileInfo().length == 0)
            throw new ChatFailException("이미지 파일이 없습니다.");

        List<SendChatDto> imageChatList = new ArrayList<>();

        try {

            for (MultipartFile file : imageDto.getFileInfo()) {
                String saveFilePath = FileUploadUtils.saveFile(absolutePath, file.getOriginalFilename(), file);
                log.info("\nsaveImage saveFile : \n" + saveFilePath);

                ImageEntity imageEntity = convertToImageEntity(imageDto, saveFilePath, file.getOriginalFilename(), absolutePath);
                imageRepository.save(imageEntity);
                imageChatList.add(saveImageToChat(imageDto));
            }
        } catch (Exception e) {
            throw new ChatFailException( "파일 저장에 실패하였습니다.");
        }

        return imageChatList.isEmpty() ? null : imageChatList;
    }   // 이미지 저장 후 채팅 데이터로 저장


    // 이미지 -> 채팅 데이터로 저장 (채팅 데이터에 이미지 정보 저장)
    public SendChatDto saveImageToChat(ImageDto imageDto) {

        User user = findUserById(imageDto.getUserId());
        ChannelEntity channel = findChannelById(imageDto.getChannelId());

        try {
            ChatDataEntity chatEntity = convertToChatEntity(user, channel, imageDto);
            chatRepository.save(chatEntity);
            return convertToChatDto(chatEntity);
        } catch (Exception e) {
            throw new ChatFailException("이미지 저장에 실패하였습니다.");
        }
    }
}
