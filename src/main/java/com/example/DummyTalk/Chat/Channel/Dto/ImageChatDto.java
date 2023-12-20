package com.example.DummyTalk.Chat.Channel.Dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ImageChatDto {
    private Long imageId;
    private int userId;          // 받을 때 string
    private String nickname;        // 받을 때 string
    private int channelId;
    private String originalFileName;
    private String filePath;
    private String savedFileName;
    @JsonIgnore
    private MultipartFile[] fileInfo;
    @JsonIgnore
    private List<EmbeddingImageDto> embeddingImageDtoList ;
}
