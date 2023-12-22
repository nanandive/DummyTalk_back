package com.example.DummyTalk.Chat.Channel.Controller;

import com.example.DummyTalk.Chat.Channel.Dto.ImageChatDto;
import com.example.DummyTalk.Chat.Channel.Dto.MessageRequest;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import com.example.DummyTalk.Chat.Channel.Service.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

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
    public static String imageEmbedded(List<MessageRequest> chat) {
        log.info("\n귀신 !!!! imageEmbedded    : {}", chat);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        for(MessageRequest messageRequest : chat) {
            body.add("imageId", messageRequest.getImageId());
            body.add("channelId", messageRequest.getChannelId());
            body.add("file", messageRequest.getFile());
            body.add("filePath", messageRequest.getFilePath());
        }

        String response = WebClient.create()
                .post()
                .uri("http://localhost:8000/uploadImage")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(body))
                .retrieve()
                .bodyToMono(String.class)
                .block();

        log.info("{}", response);

        return response;
    }
}

