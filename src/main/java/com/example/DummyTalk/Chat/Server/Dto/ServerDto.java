package com.example.DummyTalk.Chat.Server.Dto;

import com.example.DummyTalk.Chat.Channel.Dto.ChannelDto;
import lombok.*;

import java.util.*;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ServerDto {

    private Long id;
    private String serverName;
    private String invitedCode;
    private String userName;
    private int userCount;

    private List<ChannelDto> channelDtoList;
    private Map<String, String> userList;



}
