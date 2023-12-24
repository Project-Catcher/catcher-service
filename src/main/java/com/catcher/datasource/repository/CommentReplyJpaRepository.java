package com.catcher.datasource.repository;

import com.catcher.core.domain.entity.CommentReply;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentReplyJpaRepository extends JpaRepository<CommentReply, Long> {
/*
    @Query("SELECT new com.catcher.core.dto.CommentReplyWithLikesDto(" +
            "cr, " +
            "(SELECT COUNT(*) FROM Like l WHERE l.type = 'COMMENTREPLY' AND l.targetId = cr.id), " +
            "CASE WHEN l.id IS NOT NULL THEN TRUE ELSE FALSE END)" +
            "FROM CommentReply cr " +
            "LEFT JOIN Like l ON l.type = 'COMMENTREPLY' AND l.user.id = cr.user.id AND l.targetId = cr.id  " +
            "WHERE cr.comment.schedule.id = :schedulefId")
    List<CommentReplyWithLikesDto> findAllWithLikeCountAndLikeStatusByScheduleId(
            @Param("scheduleId") Long scheduleId
    );

 */

}
