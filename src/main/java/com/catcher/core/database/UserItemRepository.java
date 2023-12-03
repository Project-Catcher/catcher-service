package com.catcher.core.database;

import com.catcher.core.domain.entity.UserItem;

import java.util.Optional;

public interface UserItemRepository {

    void save(UserItem userItem);

    Optional<UserItem> findById(Long itemId);
}
