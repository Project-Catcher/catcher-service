package com.catcher.datasource;

import com.catcher.core.database.TagRepository;
import com.catcher.core.domain.entity.Tag;
import com.catcher.core.domain.entity.enums.RecommendedStatus;
import com.catcher.datasource.repository.TagJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class TagRepositoryImpl implements TagRepository {
    private final TagJpaRepository tagJpaRepository;

    @Override
    public List<Tag> findAll() {
        return tagJpaRepository.findAll();
    }

    @Override
    public void save(Tag tag) {
        tagJpaRepository.save(tag);
    }

    @Override
    public List<Tag> findByRecommendedStatus(RecommendedStatus recommendedStatus) {
        return tagJpaRepository.findByRecommendedStatus(recommendedStatus);
    }
}
