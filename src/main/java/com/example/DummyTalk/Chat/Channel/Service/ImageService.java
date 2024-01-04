package com.example.DummyTalk.Chat.Channel.Service;

import com.example.DummyTalk.Chat.Channel.Dto.ImageChatDto;
import com.example.DummyTalk.Chat.Channel.Dto.ImageDto;
import com.example.DummyTalk.Chat.Channel.Dto.ImageEmbeddingRequestDto;
import com.example.DummyTalk.Chat.Channel.Dto.MessageRequest;
import org.springframework.http.RequestEntity;

import java.util.List;

public interface ImageService {
    List<MessageRequest> saveImage(ImageChatDto imageDto);

    void imageEmbedded(List<ImageEmbeddingRequestDto> saveImageList);

    List<MessageRequest> saveImageToChat(List<ImageEmbeddingRequestDto> imageDto, ImageChatDto imageChatDto);

    List<ImageDto> getImageList(Long channelId);
}
