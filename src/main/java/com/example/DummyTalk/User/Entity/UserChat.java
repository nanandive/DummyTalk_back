package com.example.DummyTalk.User.Entity;

import com.example.DummyTalk.Chat.Server.Entity.ServerEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString
@Table(name = "user_chat")
public class UserChat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user; // 가정한 UserEntity

    @ManyToOne
    @JoinColumn(name = "server_id")
    private ServerEntity server;

}
