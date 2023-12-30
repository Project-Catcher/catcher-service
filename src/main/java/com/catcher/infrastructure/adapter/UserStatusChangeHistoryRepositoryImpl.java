package com.catcher.infrastructure.adapter;

import com.catcher.core.domain.UserStatus;
import com.catcher.core.domain.entity.User;
import com.catcher.core.domain.entity.UserStatusChangeHistory;
import com.catcher.core.port.UserStatusChangeHistoryRepository;
import com.catcher.datasource.repository.UserStatusChangeHistoryJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserStatusChangeHistoryRepositoryImpl implements UserStatusChangeHistoryRepository {

    private final UserStatusChangeHistoryJpaRepository userStatusChangeHistoryJpaRepository;

    @Override
    public UserStatusChangeHistory save(final UserStatusChangeHistory userStatusChangeHistory) {
        return userStatusChangeHistoryJpaRepository.save(userStatusChangeHistory);
    }

    @Override
    public Optional<UserStatusChangeHistory> findFirstByUserAndActionAndAffectedOrderByIdDesc(final User user, final UserStatus userStatus, final boolean affected) {
        return userStatusChangeHistoryJpaRepository.findFirstByUserAndActionAndAffectedOrderByIdDesc(user, userStatus, affected);
    }

    @Override
    public List<UserStatusChangeHistory> findAllByUserId(final Long userId) {

        return userStatusChangeHistoryJpaRepository.findByUserId(userId);
    }

}
