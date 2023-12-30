package com.catcher.datasource.user;

import com.catcher.core.domain.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Long countByDeletedAtIsNotNull();

    Page<User> findAll(Specification<User> specification, Pageable pageable);

    @Query("SELECT new map(date(u.createdAt) as date, COUNT(u) as count) " +
            "FROM User u " +
            "WHERE u.createdAt BETWEEN :startDate AND :endDate " +
            "GROUP BY date(u.createdAt)")
    List<Map<String, Object>> countNewUsersPerDay(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT new map(date(u.createdAt) as date, COUNT(u) as count) " +
            "FROM User u " +
            "WHERE u.deletedAt BETWEEN :startDate AND :endDate " +
            "GROUP BY date(u.createdAt)")
    List<Map<String, Object>> countDeletedUsersPerDay(@Param("startDate") ZonedDateTime startDate, @Param("endDate") ZonedDateTime endDate);

    default List<Map<String, Object>> countDeletedUsersPerDay(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate) {
        return countDeletedUsersPerDay(startDate.atZone(ZoneId.systemDefault()), endDate.atZone(ZoneId.systemDefault()));
    }

    @Query("SELECT new map(date(ush.createdAt) as date, COUNT(DISTINCT u) as count) " +
            "FROM UserStatusChangeHistory ush " +
            "JOIN ush.user u " +
            "WHERE ush.createdAt BETWEEN :startDate AND :endDate " +
            "AND ush.action = com.catcher.core.domain.UserStatus.REPORTED " +
            "GROUP BY date(ush.createdAt)")
    List<Map<String, Object>> countReportedUsersPerDay(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

}
