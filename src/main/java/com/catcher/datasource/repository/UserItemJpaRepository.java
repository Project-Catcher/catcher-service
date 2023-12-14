package com.catcher.datasource.repository;

import com.catcher.core.domain.entity.User;
import com.catcher.core.domain.entity.UserItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserItemJpaRepository extends JpaRepository<UserItem, Long> {
    List<UserItem> findByUser(User user);
}
