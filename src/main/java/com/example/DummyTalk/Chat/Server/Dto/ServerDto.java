package com.example.DummyTalk.Chat.Server.Dto;

import com.example.DummyTalk.Chat.Channel.Dto.ChannelDto;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ServerDto {

    private Long id;
    private String serverName;
    private String invitedUser;
    private String userName;
    private int maxUsers;
    private int currentUsers;

    private Long userId;

    private String fileName;
    private String filePath;

    private List<ChannelDto> channelDtoList;
    private Map<String, String> userList;




}
