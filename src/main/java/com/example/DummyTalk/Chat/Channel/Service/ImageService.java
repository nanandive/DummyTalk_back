package com.example.DummyTalk.Chat.Channel.Service;

import com.example.DummyTalk.Chat.Channel.Dto.ImageChatDto;
import com.example.DummyTalk.Chat.Channel.Dto.ImageDto;
import com.example.DummyTalk.Chat.Channel.Dto.SendChatDto;

import java.util.List;

public interface ImageService {
    List<SendChatDto> saveImage(ImageChatDto imageDto);
}
