package com.catcher.core.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@Getter
public class BaseTimeEntity {
    protected static ZoneId ZONE = ZoneId.of("Asia/Seoul");

    @Column(name = "created_at")
    private ZonedDateTime createdAt;

    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;

    @PrePersist
    private void prePersist() {
        this.createdAt = ZonedDateTime.now(ZONE);
        this.updatedAt = ZonedDateTime.now(ZONE);
    }

    @PreUpdate
    private void preUpdate() {
        this.updatedAt = ZonedDateTime.now(ZONE);
    }

    // 작성 중인 일정 TEST 위해 생성 - CW
    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
