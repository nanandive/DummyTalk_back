package com.example.DummyTalk.User.Repository;

import com.example.DummyTalk.User.Entity.UserChat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface UserChatRepository extends JpaRepository<UserChat, Long> {
}
