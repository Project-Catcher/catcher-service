package com.catcher.core.database;

import com.catcher.core.domain.entity.Tag;

import java.util.List;

public interface TagRepository {
    List<Tag> findAll();

    void save(Tag tag);

    List<Tag> findByIsRecommendedTrue();
}
