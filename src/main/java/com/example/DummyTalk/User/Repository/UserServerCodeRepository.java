package com.example.DummyTalk.User.Repository;

import com.example.DummyTalk.User.Entity.UserServerCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserServerCodeRepository extends JpaRepository<UserServerCode, Long> {
}
