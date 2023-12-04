package com.catcher.core.domain.entity;

import com.catcher.core.domain.entity.enums.ContentType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "upload_file")
public class UploadFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long contentId;

    @Enumerated(value = EnumType.STRING)
    private ContentType contentType;

    @Column(nullable = false)
    private String fileUrl;

    @Column(name = "deleted_at")
    private ZonedDateTime deletedAt;
}