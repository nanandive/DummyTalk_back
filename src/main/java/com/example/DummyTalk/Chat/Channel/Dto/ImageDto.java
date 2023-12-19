package com.example.DummyTalk.Chat.Channel.Dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ImageDto {
    private Long imageId;
    private String userId;          // 받을 때 string
    private String nickname;        // 받을 때 string
    private String originalFileName;
    private String filePath;
    private String savedFileName;
    @JsonIgnore
    private MultipartFile[] fileInfo;
    @JsonIgnore
    private List<EmbeddingImageDto> embeddingImageDtoList ;
}
