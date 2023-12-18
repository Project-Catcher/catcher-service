package com.catcher.core.domain.entity;

import com.catcher.core.domain.entity.enums.ItemType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Table(name = "schedule_detail")
@Where(clause = "deleted_at is null")
public class ScheduleDetail extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id", nullable = false)
    private Schedule schedule;

    @Column(nullable = false)
    private Long itemId;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private ItemType itemType;

    private String description;

    private String color;

    @Column(name = "start_at", nullable = false)
    private LocalDateTime startAt;

    @Column(name = "end_at", nullable = false)
    private LocalDateTime endAt;

    private LocalDateTime deletedAt;

    public void updateScheduleDetail(String color, String description, LocalDateTime startAt, LocalDateTime endAt) {
        this.color = color;
        this.description = description;
        this.startAt = startAt;
        this.endAt = endAt;
    }
}
