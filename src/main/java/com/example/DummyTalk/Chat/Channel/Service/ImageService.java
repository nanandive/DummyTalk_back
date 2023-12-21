package com.example.DummyTalk.Chat.Channel.Service;

import com.example.DummyTalk.Chat.Channel.Dto.ImageChatDto;
import com.example.DummyTalk.Chat.Channel.Dto.MessageRequest;

import java.util.List;

public interface ImageService {
    List<MessageRequest> saveImage(ImageChatDto imageDto);
}
