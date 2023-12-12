package com.catcher.core.domain.entity;

import com.catcher.core.domain.entity.enums.PublicStatus;
import com.catcher.core.domain.entity.enums.ScheduleStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Table(name = "schedule")
public class Schedule extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private Location location;

    private Long participantLimit;

    @Column(nullable = false)
    private String title;

    private String description;

    @Column(nullable = false)
    private String thumbnailUrl;

    @Column(nullable = false)
    private Long viewCount;

    private Long budget;

    @Enumerated(value = EnumType.STRING)
    @ColumnDefault("'NORMAL'")
    private ScheduleStatus scheduleStatus;

    @Enumerated(value = EnumType.STRING)
    private PublicStatus publicStatus;

    @Column(name = "start_at", nullable = false)
    private ZonedDateTime startAt; // 일정 시작

    @Column(name = "end_at", nullable = false)
    private ZonedDateTime endAt; // 일정 종료

    @Column(name = "participate_start_at")
    private LocalDateTime participateStartAt; // 모집 시작

    @Column(name = "participate_end_at")
    private LocalDateTime participateEndAt; // 모집 종료

    public void draftSchedule(
            String title, String thumbnailUrl, ZonedDateTime startAt, ZonedDateTime endAt, Location location,
            Long participantLimit, Long budget, PublicStatus publicStatus,
            LocalDateTime participateStartAt, LocalDateTime participateEndAt
    ) {
        this.title = title;
        this.thumbnailUrl = thumbnailUrl;
        this.startAt = startAt;
        this.endAt = endAt;
        this.location = location;
        this.participantLimit = participantLimit;
        this.budget = budget;
        this.publicStatus = publicStatus;
        this.participateStartAt = participateStartAt;
        this.participateEndAt = participateEndAt;
    }
}
