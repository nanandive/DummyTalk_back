package com.example.DummyTalk.Chat.Server.Service;

import com.example.DummyTalk.Chat.Channel.Entity.ChannelEntity;
import com.example.DummyTalk.Chat.Channel.Repository.ChannelRepository;
import com.example.DummyTalk.Chat.Server.Dto.ServerDto;
import com.example.DummyTalk.Chat.Server.Entity.ServerEntity;
import com.example.DummyTalk.Chat.Server.repository.ServerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ServerService {
    private final ServerRepository serverRepository;
    private final ChannelRepository channelRepository;




}
