package com.catcher.datasource.repository;

import com.catcher.core.domain.entity.Tag;
import com.catcher.core.domain.entity.enums.RecommendedStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TagJpaRepository extends JpaRepository<Tag, Long> {
    List<Tag> findByRecommendedStatus(RecommendedStatus recommendedStatus);

    List<Tag> findByNameIn(List<String> tagNames);
}
