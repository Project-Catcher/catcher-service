package com.catcher.core.database;

import com.catcher.core.domain.entity.Comment;
import com.catcher.core.dto.CommentWithLikesDto;

import java.util.List;
import java.util.Optional;

public interface CommentRepository {
    Optional<Comment> findById(Long commentId);

    Comment save(Comment comment);

    List<Comment> saveAll(List<Comment> commentList);

    List<CommentWithLikesDto> findAllWithLikeCountAndLikeStatusByScheduleIdAndUserID(Long scheduleId, Long userId);
}
