package com.example.DummyTalk.Config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan(basePackages = "com.example.DummyTalk") // 인지할 Entity범위
@EnableJpaRepositories(basePackages = "com.example.DummyTalk") // 인지할 repository 범위
public class JpaConfig {
}

