package com.catcher.datasource.user;

import com.catcher.core.domain.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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
    List<Map<String, Object>> countNewUsersPerDay(@Param("startDate") ZonedDateTime startDate, @Param("endDate") ZonedDateTime endDate);

    @Query("SELECT new map(date(u.createdAt) as date, COUNT(u) as count) " +
            "FROM User u " +
            "WHERE u.deletedAt BETWEEN :startDate AND :endDate " +
            "GROUP BY date(u.createdAt)")
    List<Map<String, Object>> countDeletedUsersPerDay(@Param("startDate") ZonedDateTime startDate, @Param("endDate") ZonedDateTime endDate);

    @Query("SELECT new map(date(u.createdAt) as date, COUNT(u) as count) " +
            "FROM User u " +
            "WHERE u.createdAt BETWEEN :startDate AND :endDate " +
            "GROUP BY date(u.createdAt)")
    List<Map<String, Object>> countReportedUsersPerDay(@Param("startDate") ZonedDateTime startDate, @Param("endDate") ZonedDateTime endDate);

}
