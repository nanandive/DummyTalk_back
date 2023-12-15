package com.example.DummyTalk.Common.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Data
public class BaseTimeEntity {
  @CreationTimestamp
  @Column(updatable = false)
    private LocalDateTime createdAt;

  @LastModifiedDate
  @Column(updatable = true)
    private LocalDateTime updatedAt;


}
