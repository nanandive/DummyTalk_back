package com.example.DummyTalk.Chat.Channel.Service;

import com.example.DummyTalk.Chat.Channel.Dto.ImageChatDto;
import com.example.DummyTalk.Chat.Channel.Dto.ImageEmbeddingRequestDto;
import com.example.DummyTalk.Chat.Channel.Dto.MessageRequest;

import java.util.List;

public interface ImageService {
    List<ImageEmbeddingRequestDto> saveImage(ImageChatDto imageDto);

    void imageEmbedded(List<ImageEmbeddingRequestDto> saveImageList);

    List<MessageRequest> saveImageToChat(List<ImageEmbeddingRequestDto> imageDto, ImageChatDto imageChatDto);

}
