package com.catcher.datasource.repository;

import com.catcher.core.domain.entity.User;
import com.catcher.core.domain.entity.UserItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserItemJpaRepository extends JpaRepository<UserItem, Long> {

    @Query("SELECT ui FROM UserItem ui " +
            "LEFT JOIN FETCH ui.location " +
            "WHERE ui.user = :user")
    List<UserItem> findByUser(@Param("user") User user);
}
