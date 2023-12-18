package com.catcher.core.specification;

import com.catcher.core.domain.entity.*;
import com.catcher.core.domain.entity.enums.ScheduleStatus;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class ScheduleSpecification {

    public static Specification<Schedule> likeTitle(String title) {
        /*
        select s from Schedule s
        where s.title like '%' || :title || '%'
         */
        return (root, query, criteriaBuilder)
                -> criteriaBuilder.like(root.get("title"), "%" + title + "%");
    }

    public static Specification<Schedule> likeDescription (String description) {
        /*
        select s from Schedule s
        where s.description like '%' || :description || '%'
         */
        return (root, query, criteriaBuilder)
                -> criteriaBuilder.like(root.get("description"), "%" + description + "%");
    }

    public static Specification<Schedule> fromBudget(Long budget) {
        /*
        select s from Schedule s
        where s.budget >= :budget
         */
        return (root, query, criteriaBuilder)
                -> criteriaBuilder.greaterThanOrEqualTo(root.get("budget"), budget);
    }

    public static Specification<Schedule> toBudget(Long budget) {
        /*
        select s from Schedule s
        where s.budget <= :budget
         */
        return (root, query, criteriaBuilder)
                -> criteriaBuilder.lessThanOrEqualTo(root.get("budget"), budget);
    }

    public static Specification<Schedule> fromStartAt(LocalDateTime startAt) {
        /*
        select s from Schedule s
        where s.startAt >= :startAt
         */
        return (root, query, criteriaBuilder)
                -> criteriaBuilder.greaterThanOrEqualTo(root.get("startAt"), startAt);
    }

    public static Specification<Schedule> toEndAt(LocalDateTime endAt) {
        /*
        select s from Schedule s
        where s.endAt <= :endAt
         */
        return (root, query, criteriaBuilder)
                -> criteriaBuilder.lessThanOrEqualTo(root.get("endAt"), endAt);
    }

    public static Specification<Schedule> fromParticipant(Long participantFrom) {
        /*
        select s from Schedule s
        where s.participantLimit >= :participantFrom
         */
        return (root, query, criteriaBuilder)
                -> criteriaBuilder.greaterThanOrEqualTo(root.get("participantLimit"), participantFrom);
    }

    public static Specification<Schedule> toParticipant(Long participantTo) {
        /*
        select s from Schedule s
        where s.participantLimit <= :participantTo
         */
        return (root, query, criteriaBuilder)
                -> criteriaBuilder.lessThanOrEqualTo(root.get("participantLimit"), participantTo);
    }

    public static Specification<Schedule> likeByUsername(String username) {
        /*
        select s from Schedule s
        left join User u s.u
        where u.username like '%' || :username || '%'
         */
        return (root, query, criteriaBuilder) -> {
            Join<Schedule, User> join = root.join("user", JoinType.LEFT);

            return criteriaBuilder.like(join.get("username"), "%" + username + "%");
        };
    }

    public static Specification<Schedule> scheduleStatus(ScheduleStatus scheduleStatus) {
        /*
        select s from Schedule s
        where s.scheduleStatus = :scheduleStatus
         */
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("scheduleStatus"), scheduleStatus);
    }

    public static Specification<Schedule> inTagName(String tagName) {
        /*
        select s from Schedule s
        where
        s.id in (select st.schedule.id from ScheduleTag st where st.tag.name = :tagName)
         */
        return (root, query, criteriaBuilder) -> {

            Subquery<Long> subquery = query.subquery(Long.class);
            Root<ScheduleTag> subRoot = subquery.from(ScheduleTag.class);

            subquery.select(subRoot.get("schedule").get("id"))
                    .where(criteriaBuilder.equal(subRoot.get("tag").get("name"), tagName));

            return criteriaBuilder.in(root.get("id")).value(subquery);
        };
    }
}
