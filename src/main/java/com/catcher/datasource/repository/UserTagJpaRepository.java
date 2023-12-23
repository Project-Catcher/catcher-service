package com.catcher.datasource.repository;

import com.catcher.core.domain.entity.UserTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserTagJpaRepository extends JpaRepository<UserTag, Long> {
    void deleteByUserId(Long userId);

    @Query("select ut from UserTag  ut " +
            "join fetch ut.tag " +
            "where ut.user.id = :userId")
    List<UserTag> findByUserIdFJTag(@Param("userId") Long userId);
}
