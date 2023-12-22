package com.example.DummyTalk.Chat.Channel.Controller;

import com.example.DummyTalk.Chat.Channel.Dto.ImageChatDto;
import com.example.DummyTalk.Chat.Channel.Dto.MessageRequest;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import org.apache.tomcat.util.http.fileupload.FileUploadBase;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.http.client.reactive.ClientHttpRequest;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import com.example.DummyTalk.Chat.Channel.Service.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.File;
import java.util.List;
import java.util.Map;

@RequestMapping("/img")
@RestController
@Slf4j
@RequiredArgsConstructor
public class ImageUploadController {

    private final ImageService imageService;

    /***
     * 이미지 저장
     * @param imageDto : channelId, userId, imageUrl, Multipart[], nickname
     * 1. 로컬에 저장
     * 2. DB에 값 저장
     * 3. 추후 성공적으로 저장되면 로컬은 삭제
     */
    @PostMapping("/save")
    public MessageResponse saveImage(@ModelAttribute ImageChatDto imageDto) {

        List<MessageRequest> saveImageToChat = imageService.saveImage(imageDto);

        log.info("\nimageEmbedded saveImageToChat    : {}", saveImageToChat);

        try {
            String response = imageEmbedded(saveImageToChat);
            if (response.equals("200")) {
                return new MessageResponse(saveImageToChat.get(0).getNickname(), "이미지 전송 성공", saveImageToChat);
            }

        } catch (Exception e) {
            log.error("{}", e);
        }


        return new MessageResponse("이미지 임베딩 실패");
    }


    /***
     * 이미지 전송
     * @param chat : channelId, userId, filePath, Multipart[]
     * 1. 이미지 저장
     * 2. 이미지 저장 성공 시, 채팅방에 이미지 전송
     */
    public String imageEmbedded(List<MessageRequest> chat) {


        try {
//        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//        Map<String, String> map = objectMapper.convertValue(chat, new TypeReference<Map<String, String>>() {}); // (3)
//        params.setAll(map); // (4)
//        log.info("\n귀신 !!!! imageEmbedded    : {}", params);
//
////            log.info("\n귀신 !!!! imageEmbedded    : {}", body);
//
//            WebClient.create()
//                    .post()
//                    .uri("http://localhost:8000/uploadImage")
//                    .contentType(MediaType.MULTIPART_FORM_DATA)
//                    .body(BodyInserters.fromMultipartData(params))
//                    .retrieve()
//                    .bodyToMono(String.class)
//                    .subscribe(response -> System.out.println("Response: " + response));

        } catch (Exception e) {
            log.error("{}", e);
        }

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
}


