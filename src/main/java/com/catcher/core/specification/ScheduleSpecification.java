package com.catcher.core.specification;

import com.catcher.core.domain.entity.*;
import com.catcher.core.domain.entity.enums.ScheduleStatus;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class ScheduleSpecification {

    public static Specification<Schedule> likeTitle(String title) {
        return (root, query, criteriaBuilder)
                -> criteriaBuilder.like(root.get("title"), "%" + title + "%");
    }

    public static Specification<Schedule> likeDescription (String description) {
        return (root, query, criteriaBuilder)
                -> criteriaBuilder.like(root.get("description"), "%" + description + "%");
    }

    public static Specification<Schedule> fromBudget(Long budget) {
        return (root, query, criteriaBuilder)
                -> criteriaBuilder.greaterThanOrEqualTo(root.get("budget"), budget);
    }

    public static Specification<Schedule> toBudget(Long budget) {
        return (root, query, criteriaBuilder)
                -> criteriaBuilder.lessThanOrEqualTo(root.get("budget"), budget);
    }

    public static Specification<Schedule> fromStartAt(LocalDateTime startAt) {
        return (root, query, criteriaBuilder)
                -> criteriaBuilder.greaterThanOrEqualTo(root.get("startAt"), startAt);
    }

    public static Specification<Schedule> toEndAt(LocalDateTime endAt) {
        return (root, query, criteriaBuilder)
                -> criteriaBuilder.lessThanOrEqualTo(root.get("endAt"), endAt);
    }

    public static Specification<Schedule> fromParticipant(Long participantFrom) {
        return (root, query, criteriaBuilder)
                -> criteriaBuilder.greaterThanOrEqualTo(root.get("participantLimit"), participantFrom);
    }

    public static Specification<Schedule> toParticipant(Long participantTo) {
        return (root, query, criteriaBuilder)
                -> criteriaBuilder.lessThanOrEqualTo(root.get("participantLimit"), participantTo);
    }

    public static Specification<Schedule> likeByUsername(String username) {
        return (root, query, criteriaBuilder) -> {
            Join<Schedule, User> join = root.join("user", JoinType.LEFT);

            return criteriaBuilder.like(join.get("username"), "%" + username + "%");
        };
    }

    public static Specification<Schedule> scheduleStatus(ScheduleStatus scheduleStatus) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("scheduleStatus"), scheduleStatus);
    }
}
