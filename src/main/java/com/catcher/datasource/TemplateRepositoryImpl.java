package com.catcher.datasource;

import com.catcher.core.database.TemplateRepository;
import com.catcher.core.domain.entity.Template;
import com.catcher.core.domain.entity.enums.RecommendedStatus;
import com.catcher.datasource.repository.TemplateJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class TemplateRepositoryImpl implements TemplateRepository {
    private final TemplateJpaRepository templateJpaRepository;

    @Override
    public List<Template> findByRecommendedStatus(RecommendedStatus recommendedStatus) {
        return templateJpaRepository.findByRecommendedStatus(recommendedStatus);
    }

    @Override
    public void save(Template template) {
        templateJpaRepository.save(template);
    }
}
