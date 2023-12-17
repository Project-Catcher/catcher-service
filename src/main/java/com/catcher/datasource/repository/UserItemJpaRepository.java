package com.catcher.datasource.repository;

import com.catcher.core.domain.entity.UserItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserItemJpaRepository extends JpaRepository<UserItem, Long> {
}
