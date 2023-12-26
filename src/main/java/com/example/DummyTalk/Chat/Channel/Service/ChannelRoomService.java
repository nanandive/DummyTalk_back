//package com.example.DummyTalk.Chat.Channel.Service;
//
//import com.example.DummyTalk.Chat.Channel.Dto.ChannelDto;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//@Slf4j
//public class ChannelRoomService {
//
//    private ChannelRepository channelRepository;
//
//    public ChannelType getChannelTypeByChannelId(Long channelId) {
//        return channelRepository.findById(channelId)
//                .orElseThrow(() -> new EntityNotFoundException("Channel not found"))
//                .getChannelType();
//    }
//}