package com.catcher.core.domain.entity;

import com.catcher.core.domain.entity.enums.RecommendedStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.ZonedDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "template")
public class Template extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id", nullable = false)
    private Schedule schedule;

    @Column(nullable = false)
    private Long days; // 일정 소요 기간

    @Column(nullable = false)
    private String theme;

    @Column(nullable = false)
    private RecommendedStatus recommendedStatus;

    private ZonedDateTime deletedAt;
}
