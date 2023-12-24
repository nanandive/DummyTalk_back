package com.example.DummyTalk.Chat.Server.repository;

import com.example.DummyTalk.Chat.Server.Entity.ServerRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServerRoleRepository extends JpaRepository<ServerRoleEntity, Long> {
}
