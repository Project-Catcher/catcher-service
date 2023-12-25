package com.catcher.core.domain.entity;

import com.catcher.core.domain.entity.enums.LikeType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Table(name = "likes")
public class Like extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "target_id", nullable = false)
    private Long targetId;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private LikeType type;
}
