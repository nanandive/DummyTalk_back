package com.example.DummyTalk.User.DTO;

import com.example.DummyTalk.User.Entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FriendDTO {

    private Long friendId;

    private Long userId; // 친구 신청을 보낸 사람

    private Long friendUserId; // 친구 신청을 받는 사람

    private String accept; // 수락 여부 Y / N

    private User user; // 수락 여부 Y / N

}