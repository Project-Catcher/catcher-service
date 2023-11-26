package com.catcher.core.domain.entity;

import com.catcher.core.domain.entity.enums.ParticipantStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "schedule_participant")
public class ScheduleParticipant extends BaseTimeEntity {
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id", nullable = false)
    private Schedule schedule;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private ParticipantStatus status;
}
