package com.example.DummyTalk.Chat.Server.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ServerSettingDto {

    private Long serverId;
    private String serverName;
    private String invitedUser;
    private String resignUser;
}
