package com.catcher.core.database;

import com.catcher.core.domain.entity.Template;
import com.catcher.core.domain.entity.enums.RecommendedStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TemplateRepository {
    @Query("SELECT t FROM Template t " +
            "JOIN FETCH t.schedule s " +
            "WHERE t.recommendedStatus = :status")
    List<Template> findByRecommendedStatus(@Param("status") RecommendedStatus recommendedStatus);

    void save(Template template);
}
