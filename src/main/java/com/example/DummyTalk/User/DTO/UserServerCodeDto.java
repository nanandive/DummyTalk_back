package com.example.DummyTalk.User.DTO;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserServerCodeDto {
    private Long id;
    private String userEmail;
    private Long userId;
    private Long serverId;
}
