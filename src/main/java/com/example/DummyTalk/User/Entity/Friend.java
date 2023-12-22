package com.example.DummyTalk.User.Entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "Friend")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Friend {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "friend_id")
    private Long friendId;

    @Column(name = "user_id")
    private Long userId; // 친구 신청을 보낸 사람

    @Column(name = "friend_user_id")
    private Long friendUserId; // 친구 신청을 받는 사람

    @Column(name = "accept")
    private String accept; // 수락 여부 Y / N

    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

}