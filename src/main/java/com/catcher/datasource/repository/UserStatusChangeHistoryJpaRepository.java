package com.catcher.datasource.repository;

import com.catcher.core.domain.UserStatus;
import com.catcher.core.domain.entity.User;
import com.catcher.core.domain.entity.UserStatusChangeHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserStatusChangeHistoryJpaRepository extends JpaRepository<UserStatusChangeHistory, Long> {

    Optional<UserStatusChangeHistory> findFirstByUserAndActionAndAffectedOrderByIdDesc(User user, UserStatus userStatus, boolean affected);

    List<UserStatusChangeHistory> findByUserId(Long userId);

}
