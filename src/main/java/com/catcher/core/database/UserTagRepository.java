package com.catcher.core.database;

import com.catcher.core.domain.entity.UserTag;

import java.util.List;

public interface UserTagRepository {
    void deleteByUserId(Long userId);

    void saveAll(List<UserTag> userTags);

    List<UserTag> findByUserId(Long userId);
}
