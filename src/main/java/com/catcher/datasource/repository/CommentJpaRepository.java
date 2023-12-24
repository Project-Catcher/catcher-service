package com.catcher.datasource.repository;

import com.catcher.core.domain.entity.Comment;
import com.catcher.core.dto.CommentWithLikesDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentJpaRepository extends JpaRepository<Comment, Long> {
    @Query("SELECT new com.catcher.core.dto.CommentWithLikesDto(" +
            "c, " +
            "(SELECT COUNT(*) FROM Like l WHERE l.type = 'COMMENT' AND l.targetId = c.id), " +
            "CASE WHEN l.id IS NOT NULL THEN TRUE ELSE FALSE END)" +
            "FROM Comment c " +
            "LEFT JOIN Like l ON l.type = 'COMMENT' AND l.user.id = c.user.id AND l.targetId = c.id " +
            "WHERE c.schedule.id = :scheduleId")
    List<CommentWithLikesDto> findAllWithLikeCountAndLikeStatusByScheduleId(
            @Param("scheduleId") Long scheduleId
    );
}
