package com.example.DummyTalk.Chat.Channel.Service;

import com.example.DummyTalk.Aws.AwsS3Service;
import com.example.DummyTalk.Chat.Channel.Controller.MessageResponse;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.IOException;
import java.lang.reflect.Array;
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

    private final String BUCKET_DIR = "channel-1/";


    private ImageEntity convertToImageEntity(int channelId, ImageDto imageDto) {
        return ImageEntity.builder()
                .channelId((long) channelId)
                .originalFileName(imageDto.getOriginalFileName())
                .savedFileName(imageDto.getSavedFileName())
                .filePath(imageDto.getFilePath())
                .build();
    }

    private ChatDataEntity convertToChatEntity(User user, ChannelEntity channel, String filePath){
        return ChatDataEntity.builder()
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
     * 이미지 저장 후 채팅 데이터로 저장
     * 로컬 저장 경로 : ${chatAbsolutePath.dir}
     * uuid_기존파일명.확장자
     * @return SendChatDto
     */
    @Transactional
    @Override
    public List<ImageEmbeddingRequestDto> saveImage(ImageChatDto imageDto) {

        if (imageDto.getFileInfo() == null || imageDto.getFileInfo().length == 0)
            throw new ChatFailException("이미지 파일이 없습니다.");

        return Arrays.stream(imageDto.getFileInfo())
                .map(file -> processImage(file, imageDto.getChannelId()))
                .collect(Collectors.toList());
    }

    private ImageEmbeddingRequestDto processImage(MultipartFile file, int channelId) {
        try {
            ImageDto saveImage = awsS3UploadService.upload(file, BUCKET_DIR);
            ImageEntity imageEntity = saveImageToDatabase(saveImage, channelId);
            return convertToImageDto(imageEntity.getImageId(), saveImage.getFilePath(), imageEntity.getChannelId());
        } catch (IOException | S3Exception | IllegalStateException e) {
            log.error("이미지 처리 중 에러가 발생했습니다.");
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }


    private ImageEntity saveImageToDatabase(ImageDto saveImage, int channelId) {
        try {
            return imageRepository.save(convertToImageEntity(channelId, saveImage));
        } catch (Exception e) {
            log.error("이미지를 데이터베이스에 저장하는데 에러가 발생했습니다.");
            log.error(e.getMessage());
            throw new ChatFailException("이미지 저장에 실패하였습니다.");
        }
    }

//        return Arrays.stream(imageDto.getFileInfo())
//                .map(file -> {
//                    try {
//                        // 이미지 S3에 저장
//                        ImageDto saveImage = awsS3UploadService.upload(file, BUCKET_DIR);
//
//                        // 이미지 DB에 저장
//                        ImageEntity imageEntity = imageRepository.save(convertToImageEntity(imageDto.getChannelId(), saveImage));
//
//                        // 이미지 임베딩 요청을 위한 정보 list에 담기
//                        return convertToImageDto(imageEntity.getImageId(), imageDto.getFilePath(), imageEntity.getChannelId());
//                    } catch (IOException e) {
//                        log.error("파일을 읽어들이는데 에러가 발생했습니다.");
//                        log.error(e.getMessage());
//                        throw new RuntimeException(e.getMessage());
//                    } catch (S3Exception e) {
//                        log.error(e.getMessage());
//                        throw new AwsFailException("AWS와 통신에 문제가 발생했습니다.");
//                    } catch (IllegalStateException e){
//                        log.error(e.getMessage());
//                        throw new ChatFailException("이미지 저장에 실패하였습니다.");
//                    }
//                })
//                .collect(Collectors.toList());
//    }

    /***
     * 이미지 전송
     * @param chat : channelId, userId, filePath, Multipart[]
     * 1. 이미지 저장
     * 2. 이미지 저장 성공 시, 채팅방에 이미지 전송
     */
    public String imageEmbedded(List<ImageEmbeddingRequestDto> chat) {


//        try {
//        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//        Map<String, String> map = objectMapper.convertValue(chat, new TypeReference<Map<String, String>>() {}); // (3)
//        params.setAll(map); // (4)
//        log.info("\n귀신 !!!! imageEmbedded    : {}", params);
//
////            log.info("\n귀신 !!!! imageEmbedded    : {}", body);
//
//            ResponseEntity response = WebClient.create()
//                    .post()
//                    .uri("http://localhost:8000/uploadImage")
//                    .contentType(MediaType.MULTIPART_FORM_DATA)
//                    .body(BodyInserters.fromMultipartData(params))
//                    .retrieve()
//                    .bodyToMono(String.class)
//                    .subscribe(response -> System.out.println("Response: " + response));
//            if (response.equals("200")) {
//                return new MessageResponse(saveImageToChat.get(0).getNickname(), "이미지 전송 성공", saveImageToChat);
//            }
//
//        } catch (Exception e) {
//            log.error("{}", e);
//        }

        return "response";
    }
//    public static String imageEmbedded(List<MessageRequest> chat) {
//        log.info("\n귀신 !!!! imageEmbedded    : {}", chat);
//
//        MultipartBodyBuilder builder = new MultipartBodyBuilder();
//
//
//        for (MessageRequest messageRequest : chat) {
////            builder.part("channelId", chat.get(0).getChannelId());
////            builder.part("imageId", messageRequest.getImageId());
////            builder.part("file", messageRequest.getFile());
////            builder.part("filePath", messageRequest.getFilePath());
//
//        }
//
//        MultiValueMap<String, HttpEntity<?>> multipartBody = builder.build();
//
//        log.info("\n귀신 !!!! imageEmbedded    : {}", multipartBody);
//
//        WebClient.create()
//                .post()
//                .uri("http://localhost:8000/uploadImage")
//                .accept(MediaType.APPLICATION_JSON)
//                .contentType(MediaType.MULTIPART_FORM_DATA)
//                .bodyValue(multipartBody)
//                .retrieve()
//                .bodyToMono(String.class)
//                .block();
//
////        log.info("{}", response);
//
//        return "200";
//    }


    // 이미지 -> 채팅 데이터로 저장 (채팅 데이터에 이미지 정보 저장)
    public List<MessageRequest> saveImageToChat(List<ImageEmbeddingRequestDto> imageDto, ImageChatDto imageChatDto) {

        /* chat DB에 담기
         * 1. list size 반복
         * 2. chat에 필요한 정보 담기 : message, channelId, sender, type
         * 3. dto에 builder
         * */

        User user = Optional.ofNullable(userRepository.findByUserId((long) imageChatDto.getUserId()))
                .orElseThrow(() -> new ChatFailException("유저 조회에 실패하였습니다."));

        ChannelEntity channel = Optional.ofNullable(channelRepository.findByChannelId((long) imageChatDto.getChannelId()))
                .orElseThrow(() -> new ChatFailException("채널 조회에 실패하였습니다."));

        return imageDto.stream()
                .map(chat -> saveToChatDatabase(chat, user, channel))
                .collect(Collectors.toList());
    }



    private MessageRequest saveToChatDatabase(ImageEmbeddingRequestDto imageDto, User user, ChannelEntity channel){
        try {
            ChatDataEntity saveChat = chatRepository.save(convertToChatEntity(user, channel, imageDto.getFilePath()));
            return convertToChatDto(saveChat);
        } catch (Exception e) {
            log.error("채팅 데이터를 데이터베이스에 저장하는데 에러가 발생했습니다.");
            log.error(e.getMessage());
            throw new ChatFailException("채팅 데이터 저장에 실패하였습니다.");
        }

    }
}