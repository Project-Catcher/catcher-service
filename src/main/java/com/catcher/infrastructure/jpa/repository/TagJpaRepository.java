package com.catcher.infrastructure.jpa.repository;

import com.catcher.core.domain.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TagJpaRepository extends JpaRepository<Tag, Long> {
    List<Tag> findByIsRecommendedTrue();
}
