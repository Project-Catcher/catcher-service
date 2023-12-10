package com.catcher.core.database;

import com.catcher.core.domain.entity.Tag;
import com.catcher.core.domain.entity.enums.RecommendedStatus;

import java.util.List;

public interface TagRepository {
    List<Tag> findAll();

    void save(Tag tag);

    List<Tag> findByRecommendedStatus(RecommendedStatus recommendedStatus);
}
