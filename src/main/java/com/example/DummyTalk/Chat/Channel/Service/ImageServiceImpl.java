package com.example.DummyTalk.Chat.Channel.Service;

import com.example.DummyTalk.Aws.AwsS3Service;
import com.example.DummyTalk.Chat.Channel.Dto.*;
import com.example.DummyTalk.Chat.Channel.Entity.ChannelEntity;
import com.example.DummyTalk.Chat.Channel.Entity.ChatDataEntity;
import com.example.DummyTalk.Chat.Channel.Entity.ImageEntity;
import com.example.DummyTalk.Chat.Channel.Repository.ChannelRepository;
import com.example.DummyTalk.Chat.Channel.Repository.ChatRepository;
import com.example.DummyTalk.Chat.Channel.Repository.ImageRepository;
import com.example.DummyTalk.Exception.ChatFailException;
import com.example.DummyTalk.User.Entity.User;
import com.example.DummyTalk.User.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageServiceImpl implements ImageService {

    private final UserRepository userRepository;
    private final ImageRepository imageRepository;
    private final ChannelRepository channelRepository;
    private final ChatRepository chatRepository;
    private final AwsS3Service awsS3UploadService;

    private final String BUCKET_DIR = "channel-";


    private ImageEntity convertToImageEntity(Long channelId, ImageDto imageDto) {
        return ImageEntity.builder()
                .channelId(channelId)
                .originalFileName(imageDto.getOriginalFileName())
                .savedFileName(imageDto.getSavedFileName())
                .filePath(imageDto.getFilePath())
                .contentType(imageDto.getContentType())
                .build();
    }

    private ChatDataEntity convertToChatEntity(User user, ChannelEntity channel, String filePath, int imageId) {
        return ChatDataEntity.builder()
                .imageId((long)imageId)
                .sender(user)
                .channelId(channel)
                .message(filePath)
                .type("IMAGE")
                .build();
    }

    private MessageRequest convertToChatDto(ChatDataEntity chat) {
        return MessageRequest.builder()
                .chatId(chat.getChatId())
                .nickname(chat.getSender().getNickname())
                .type(chat.getType())
                .profileImage(chat.getSender().getUserImgPath())
                .timeStamp(chat.getCreatedAt())
                .channelId(chat.getChannelId().getChannelId().intValue())
                .message(chat.getMessage())
                .build();
    }

    private ImageEmbeddingRequestDto convertToImageDto(Long imageId, String filePath, Long channelId) {
        return ImageEmbeddingRequestDto.builder()
                .imageId(imageId.intValue())
                .filePath(filePath)
                .channelId(channelId.intValue())
                .build();
    }

    /****
     * 이미지 저장 후 채팅 데이터로 저장 (processImage -> saveImageToDatabase -> saveImageToChat)
     * @param imageDto : channelId, userId, imageUrl, Multipart[], nickname
     * @return List<ImageEmbeddingRequestDto> : 이미지 아이디, 파일 경로, 채널 아이디
     */
    @Transactional
    @Override
    public List<MessageRequest> saveImage(ImageChatDto imageDto) {

        if (imageDto.getFileInfo() == null || imageDto.getFileInfo().length == 0)
            throw new ChatFailException("이미지 파일이 없습니다.");

        /* 이미지 임베딩 요청을 위한 정보 적재 */
        List<ImageEmbeddingRequestDto> saveImageList = Arrays.stream(imageDto.getFileInfo())
                .map(file -> processImage(file, imageDto.getChannelId()))
                .collect(Collectors.toList());

        log.info("\nImageUploadController saveImage    : {}", saveImageList.get(0).getImageId());

        imageEmbedded(saveImageList);

        return saveImageToChat(saveImageList, imageDto);
    }

    /***
     * 이미지 AWS S3 및 DB 저장
     * @return List<ImageEmbeddingRequestDto> : 이미지 아이디, 파일 경로, 채널 아이디
     */
    private ImageEmbeddingRequestDto processImage(MultipartFile file, int channelId) {
        try {
            ImageDto saveImage = awsS3UploadService.upload(file, BUCKET_DIR+channelId+"/");
            ImageEntity imageEntity = saveImageToDatabase(saveImage, channelId);
            return convertToImageDto(imageEntity.getImageId(), saveImage.getFilePath(), imageEntity.getChannelId());
        } catch (IOException | S3Exception | IllegalStateException e) {
            log.error("이미지 처리 중 에러가 발생했습니다.");
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    // 이미지 -> DB 저장
    private ImageEntity saveImageToDatabase(ImageDto saveImage, int channelId) {
        ChannelEntity channel = channelRepository.findByChannelId((long) channelId);
        if (channel == null) throw new ChatFailException("채널 조회에 실패하였습니다.");

        try {
            return imageRepository.save(convertToImageEntity(channel.getChannelId(), saveImage));
        } catch (Exception e) {
            log.error("이미지를 데이터베이스에 저장하는데 에러가 발생했습니다.");
            log.error(e.getMessage());
            throw new ChatFailException("이미지 저장에 실패하였습니다.");
        }
    }

    /*** 채팅 데이터로 저장
     * @param imageDto : 이미지 아이디, 파일 경로, 채널 아이디
     * @param imageChatDto : 채널 아이디, 유저 아이디, 닉네임
     * @return List<MessageRequest> : 채팅 데이터
     */
    public List<MessageRequest> saveImageToChat(List<ImageEmbeddingRequestDto> imageDto, ImageChatDto imageChatDto) {

        User user = userRepository.findByUserId((long) imageChatDto.getUserId());
        ChannelEntity channel = channelRepository.findByChannelId((long) imageChatDto.getChannelId());

        if (user == null || channel == null)
            throw new ChatFailException("채널 또는 유저 조회에 실패하였습니다.");

        return imageDto.stream()
                .map(chat -> saveToChatDatabase(chat, user, channel))
                .collect(Collectors.toList());
    }


    // 채팅 데이터 -> DB 저장
    private MessageRequest saveToChatDatabase(ImageEmbeddingRequestDto imageDto, User user, ChannelEntity channel) {
        try {
            ChatDataEntity saveChat = chatRepository.save(convertToChatEntity(user, channel, imageDto.getFilePath(), imageDto.getImageId()));
            return convertToChatDto(saveChat);
        } catch (Exception e) {
            log.error("채팅 데이터를 데이터베이스에 저장하는데 에러가 발생했습니다.");
            log.error(e.getMessage());
            throw new ChatFailException("채팅 데이터 저장에 실패하였습니다.");
        }
    }


    /***
     * 이미지 전송
     * @param chat : channelId, userId, filePath, Multipart[]
     * 1. 이미지 저장
     * 2. 이미지 저장 성공 시, 채팅방에 이미지 전송
     */
    public void imageEmbedded(List<ImageEmbeddingRequestDto> chat) {
        try {
            log.info("\n귀신 !!!! imageEmbedded chat   : {}", chat);

            WebClient.create()
                    .post()
                    .uri("http://localhost:8000/api/image/upload")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(chat))
                    .retrieve()
                    .bodyToMono(ResponseEntity.class)
                    .subscribe(res-> log.info("\nImageServiceImpl imageEmbedded    : {}", res));

        } catch (Exception e) {
            log.error("{}", e);
        }
    }



    @Override
    public List<ImageDto> getImageList(Long channelId) {
//            ChannelEntity channel = channelRepository.findByChannelId(channelId);
        List<ImageEntity> imageEntities = imageRepository.findByChannelId(channelId);

        if (imageEntities == null || imageEntities.isEmpty()) return null;
        log.info("\nImageServiceImpl getImageList    : {}", imageEntities);

        return imageEntities.stream()
                .map(image -> ImageDto.builder()
                        .imageId(image.getImageId())
                        .originalFileName(image.getOriginalFileName())
                        .savedFileName(image.getSavedFileName())
                        .contentType(image.getContentType())
                        .fileBlob(awsS3UploadService.getObjectBytes(image.getSavedFileName()))
                        .build())
                .collect(Collectors.toList());
    }

}