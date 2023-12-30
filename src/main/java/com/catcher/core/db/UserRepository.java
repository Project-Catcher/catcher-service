package com.catcher.core.db;

import com.catcher.core.domain.UserSearchFilterType;
import com.catcher.core.domain.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UserRepository {
    Optional<User> findByUsername(String username);

    Optional<User> findById(Long id);

    User save(User user);

    void saveAll(List<User> userList);

    Long countByDeletedAtIsNotNull();

    Long count();

    Map<String, Long> countNewUsersPerDay(LocalDate startDate, LocalDate endDate);

    Map<String, Long> countDeletedUsersPerDay(LocalDate startDate, LocalDate endDate);

    Map<String, Long> countReportedUsersPerDay(LocalDate startDate, LocalDate endDate);

    Page<User> searchUsersWithFilter(UserSearchFilterType filterType, LocalDate startDate, LocalDate endDate, String query, Pageable pageable);

}
