package com.example.DummyTalk.User.DTO;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserChatDto {
    private Long id;
    private Long userId;
    private Long serverId;
}
