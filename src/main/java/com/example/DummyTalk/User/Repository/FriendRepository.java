package com.example.DummyTalk.User.Repository;

import com.example.DummyTalk.User.Entity.Friend;
import com.example.DummyTalk.User.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

public interface FriendRepository extends JpaRepository<Friend, Long> {


    @Query("SELECT f FROM Friend f " +
            "WHERE f.friendUserId = :userId " +
            "AND f.userId = :friendId")
    Friend findByFriend(int userId, String friendId);

}
