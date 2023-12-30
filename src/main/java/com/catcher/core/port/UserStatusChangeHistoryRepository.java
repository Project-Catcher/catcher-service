package com.catcher.core.port;

import com.catcher.core.domain.UserStatus;
import com.catcher.core.domain.entity.User;
import com.catcher.core.domain.entity.UserStatusChangeHistory;

import java.util.List;
import java.util.Optional;

public interface UserStatusChangeHistoryRepository {

    UserStatusChangeHistory save(UserStatusChangeHistory userStatusChangeHistory);

    Optional<UserStatusChangeHistory> findFirstByUserAndActionAndAffectedOrderByIdDesc(User user, UserStatus userStatus, boolean affected);

    List<UserStatusChangeHistory> findAllByUserId(Long id);

}
