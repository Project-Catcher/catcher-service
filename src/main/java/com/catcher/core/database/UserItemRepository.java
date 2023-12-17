package com.catcher.core.database;

import com.catcher.core.domain.entity.User;
import com.catcher.core.domain.entity.UserItem;

import java.util.List;
import java.util.Optional;

public interface UserItemRepository {

    void save(UserItem userItem);

    Optional<UserItem> findById(Long itemId);

    List<UserItem> findByUser(User user);
}
