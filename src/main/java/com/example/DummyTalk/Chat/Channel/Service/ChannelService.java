package com.example.DummyTalk.Chat.Channel.Service;

import com.example.DummyTalk.Chat.Channel.Dto.ChannelDto;
import com.example.DummyTalk.Chat.Channel.Dto.ChatDataDto;
import com.example.DummyTalk.Chat.Channel.Dto.SendChatDto;
import com.example.DummyTalk.Chat.Channel.Entity.ChannelEntity;
import com.example.DummyTalk.Chat.Channel.Entity.ChatDataEntity;
import com.example.DummyTalk.Chat.Channel.Entity.SendChatEntity;
import com.example.DummyTalk.Chat.Channel.Repository.ChannelRepository;
import com.example.DummyTalk.Chat.Channel.Repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.config.Task;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChannelService {

    private ChannelRepository channelRepository;
    private ChatRepository chatRepository;
    private ChatDataEntity chatDataEntity;
    private SendChatEntity sendChatEntity;
    private ModelMapper modelMapper;

    public ChannelService(ModelMapper modelMapper
            , ChatDataEntity chatDataEntity
            , ChannelRepository channelRepository
            , ChatRepository chatRepository
            ){
        this.modelMapper = modelMapper;
        this.chatDataEntity = chatDataEntity;
        this.channelRepository = channelRepository;
        this.chatRepository = chatRepository;
    }




    /* DB에서 채널 list 가져오기 */
    public List<ChannelDto> findAllChannel() {
        List<ChannelEntity> channelEntities = channelRepository.findAll();

        return channelEntities.stream()
                .map(this::converToDto)
                .collect((Collectors.toList()));
    }

    /* Entity -> Dto 변환 */
    private ChannelDto converToDto(ChannelEntity channelEntity) {
        return ChannelDto.builder()
                .channelName(channelEntity.getChannelName())
                .build();
    }










//
//    /* 업무 등록 */
//    @Transactional
//    public void taskRegist(AuthEmpDto emp, TaskDto task) {
//
//        task.setAuthor(modelMapper.map(emp, ProjEmpDto.class));
//        task.setModifyTime(new Date());
//
//        taskRepository.save(modelMapper.map(task, Task.class));
//
//    }
//



//    /* 채팅 내용 저장 (DB) */
//    @Transactional
//    public void saveChatData(ChatDataDto chatDataDto) {
//
////        ChatDataDto chat = ChatDataDto.builder()
////                .channelDataId(1L)
////                .sender(sender)
////                .message(message)
////                .build();
//        log.info("chatDataDto : " + chatDataDto);
//
//
//        chatRepository.save(modelMapper.map(chatDataDto, ChatDataEntity.class));
//    }

    /* 채팅 내용 저장 (DB) */
    @Transactional
    public void saveChatData(ChatDataDto message){

        log.info("chatDataDto : " + message);

        chatRepository.save(modelMapper.map(message, ChatDataEntity.class));
    }

    // 채팅 내용 불러오기


}
