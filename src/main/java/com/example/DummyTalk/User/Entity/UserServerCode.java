package com.example.DummyTalk.User.Entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.web.bind.annotation.GetMapping;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "user_server_code")
public class UserServerCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String serverCode;


    /* 유저와의 연관관계 */
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
