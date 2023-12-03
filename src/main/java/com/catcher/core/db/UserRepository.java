package com.catcher.core.db;

import com.catcher.core.domain.entity.User;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findByUsername(String username);

    Optional<User> findById(Long id);
}
