package com.example.DummyTalk.Chat.Channel.Service;

import com.example.DummyTalk.Chat.Channel.Dto.ImageChatDto;
import com.example.DummyTalk.Chat.Channel.Dto.ImageDto;
import com.example.DummyTalk.Chat.Channel.Dto.MessageRequest;
import com.example.DummyTalk.Chat.Channel.Entity.ChannelEntity;
import com.example.DummyTalk.Chat.Channel.Entity.ChatDataEntity;
import com.example.DummyTalk.Chat.Channel.Entity.ImageEntity;
import com.example.DummyTalk.Chat.Channel.Repository.ChannelRepository;
import com.example.DummyTalk.Chat.Channel.Repository.ChatRepository;
import com.example.DummyTalk.Chat.Channel.Repository.ImageRepository;
import com.example.DummyTalk.Exception.ChatFailException;
import com.example.DummyTalk.User.Entity.User;
import com.example.DummyTalk.User.Repository.UserRepository;
import com.example.DummyTalk.Util.FileUploadUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

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

//    private UserDTO convertToUserDto(User user) {
//        return UserDTO.builder()
//                .userId(user.getUserId())
//                .nickname(user.getNickname())
//                .build();
//    }

    private ImageEntity convertToImageEntity(int channelId, ImageDto imageDto) {

        return ImageEntity.builder()
                .channelId((long) channelId)
                .originalFileName(imageDto.getOriginalFileName())
                .savedFileName(imageDto.getSavedFileName())
                .filePath(imageDto.getFilePath())
                .build();
    }

    private ChatDataEntity convertToChatEntity(User user, ChannelEntity channel, MessageRequest imageDto) {
        return ChatDataEntity.builder()
                .sender(user)
                .channelId(channel)
                .message(imageDto.getMessage())
                .type("IMAGE")
                .build();
    }

    private MessageRequest convertToChatDto(ChatDataEntity chat, MessageRequest imageDto, int index) {
        log.info("\n!!!!!!!!!!!convertToChatDto : \n" + imageDto);
        return MessageRequest.builder()
                .chatId(chat.getChatId())
                .nickname(chat.getSender().getNickname())
                .type(chat.getType())
                .profileImage(chat.getSender().getUserImgPath())
                .timeStamp(chat.getCreatedAt())
                .channelId(chat.getChannelId().getChannelId().intValue())
                .message(chat.getMessage())
                .filePath(imageDto.getFilePath())
                .file(imageDto.getFileInfo()[index])
                .imageId(imageDto.getImageId())
                .build();
    }

    private MessageRequest convertToImageDto(ImageEntity imageEntity, ImageChatDto imageDto) {
        return MessageRequest.builder()
                .channelId(imageEntity.getChannelId().intValue())
                .nickname(imageDto.getNickname())
                .filePath(imageEntity.getFilePath())
                .message(imageEntity.getSavedFileName())
                .imageId(imageEntity.getImageId())
                .sender(imageDto.getUserId())
                .fileInfo(imageDto.getFileInfo())
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
//    @Override
//    public List<MessageRequest> saveImage(ImageChatDto imageDto) {
//
//        if (imageDto.getFileInfo() == null || imageDto.getFileInfo().length == 0)
//            throw new ChatFailException("이미지 파일이 없습니다.");
//
//        List<MessageRequest> imageChatList;
//
//        try {
//            imageChatList = new ArrayList<>();
//            for (MultipartFile file : imageDto.getFileInfo()) {
//
//                ImageDto saveFile = FileUploadUtils.saveFile(absolutePath, file.getOriginalFilename(), file);
//
//                ImageEntity imageEntity = imageRepository.save(convertToImageEntity(imageDto.getChannelId(), saveFile));
//
//                MessageRequest saveImageDto = convertToImageDto(imageEntity, imageDto);
//                imageChatList.add(saveImageToChat(saveImageDto, index));
//                log.info("\n귀신 saveImageDto : \n" + saveImageDto);
//            }
//                log.info("\n귀신 saveImageDto : \n" + imageChatList);
//        } catch (Exception e) {
//            throw new ChatFailException( "파일 저장에 실패하였습니다.");
//        }
//
//        return imageChatList.isEmpty() ? null : imageChatList;
//    }   // 이미지 저장 후 채팅 데이터로 저장

    @Override
    public List<MessageRequest> saveImage(ImageChatDto imageDto) {

        if (imageDto.getFileInfo() == null || imageDto.getFileInfo().length == 0)
            throw new ChatFailException("이미지 파일이 없습니다.");

        List<MessageRequest> imageChatList;

        try {
            imageChatList = new ArrayList<>();

            IntStream.range(0, imageDto.getFileInfo().length)
                    .forEach(index -> {
                        MultipartFile file = imageDto.getFileInfo()[index];
                        ImageDto saveFile = null;
                        try {
                            saveFile = FileUploadUtils.saveFile(absolutePath, file.getOriginalFilename(), file);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        ImageEntity imageEntity = imageRepository.save(convertToImageEntity(imageDto.getChannelId(), saveFile));
                        MessageRequest saveImageDto = convertToImageDto(imageEntity, imageDto);
                        imageChatList.add(saveImageToChat(saveImageDto, index));
//                        log.info("\n귀신 saveImageDto : \n" + saveImageDto);
                    });

            log.info("\n귀신 saveImageDto : \n" + imageChatList);
        } catch (Exception e) {
            throw new ChatFailException("파일 저장에 실패하였습니다.");
        }

        return imageChatList.isEmpty() ? null : imageChatList;
    }



    // 이미지 -> 채팅 데이터로 저장 (채팅 데이터에 이미지 정보 저장)
    public MessageRequest saveImageToChat(MessageRequest image, int index) {

        User user = findUserById(image.getSender());
        ChannelEntity channel = findChannelById(image.getChannelId());

        try {
            ChatDataEntity chatEntity = convertToChatEntity(user, channel, image);
            chatRepository.save(chatEntity);
//            log.info("\nsaveImage 배열 반환값 : \n" + image);

            return convertToChatDto(chatEntity, image, index);
        } catch (Exception e) {
            throw new ChatFailException("이미지 저장에 실패하였습니다.");
        }
    }
}
