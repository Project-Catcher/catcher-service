package com.catcher.datasource.repository;

import com.catcher.core.domain.entity.Template;
import com.catcher.core.domain.entity.enums.RecommendedStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TemplateJpaRepository extends JpaRepository<Template, Long> {


    List<Template> findByRecommendedStatus(RecommendedStatus recommendedStatus);
}
