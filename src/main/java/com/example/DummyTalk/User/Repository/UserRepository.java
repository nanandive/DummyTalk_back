package com.example.DummyTalk.User.Repository;

import com.example.DummyTalk.User.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
