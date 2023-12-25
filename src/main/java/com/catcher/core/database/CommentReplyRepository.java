package com.catcher.core.database;

import com.catcher.core.domain.entity.CommentReply;
import com.catcher.core.dto.CommentReplyWithLikesDto;

import java.util.List;
import java.util.Optional;

public interface CommentReplyRepository {
    Optional<CommentReply> findById(Long commentReplyId);

    CommentReply save(CommentReply commentReply);

    List<CommentReplyWithLikesDto> findAllWithLikeCountAndLikeStatusByScheduleIdAndUserID(Long scheduleId, Long userId);

    List<CommentReply> saveAll(List<CommentReply> commentReplyList);
}
