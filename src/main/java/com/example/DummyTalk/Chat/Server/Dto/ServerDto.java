package com.example.DummyTalk.Chat.Server.Dto;

import com.example.DummyTalk.Chat.Channel.Dto.ChannelDto;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.*;

@Data
@ToString
@Builder
public class ServerDto {

    private String serverId;
    private String serverName;
    private String invitedCode;
    private String userName;
    private String createdAt;
    private String updatedAt;
    private List<ChannelDto> channelDtoList;
    private Map<String, String> userList;



}
