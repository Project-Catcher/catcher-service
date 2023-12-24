package com.catcher.core.service;

import com.catcher.common.exception.BaseException;
import com.catcher.common.exception.BaseResponseStatus;
import com.catcher.core.database.CommentReplyRepository;
import com.catcher.core.database.CommentRepository;
import com.catcher.core.database.ScheduleRepository;
import com.catcher.core.domain.entity.Comment;
import com.catcher.core.domain.entity.CommentReply;
import com.catcher.core.domain.entity.Schedule;
import com.catcher.core.domain.entity.User;
import com.catcher.core.domain.entity.enums.CommentStatus;
import com.catcher.core.dto.CommentReplyWithLikesDto;
import com.catcher.core.dto.CommentWithLikesDto;
import com.catcher.core.dto.request.SaveCommentRequest;
import com.catcher.core.dto.response.CommentListResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class CommentService {
    private final CommentRepository commentRepository;
    private final CommentReplyRepository commentReplyRepository;
    private final ScheduleRepository scheduleRepository;

    @Transactional
    public CommentListResponse saveCommentOrCommentReply(User user, SaveCommentRequest saveCommentRequest) {
        if (saveCommentRequest.getCommentId() != null) {
            saveCommentReply(user, saveCommentRequest.getCommentId(), saveCommentRequest.getContent());
        } else
            saveComment(user, saveCommentRequest.getScheduleId(), saveCommentRequest.getContent());

        return getCommentListResponseByScheduleIdAndUserId(saveCommentRequest.getScheduleId(), user.getId());
    }

    private CommentListResponse getCommentListResponseByScheduleIdAndUserId(Long scheduleId, Long userId) {
        List<CommentWithLikesDto> commentWithLikesDtoList = commentRepository.findAllWithLikeCountAndLikeStatusByScheduleIdAndUserID(scheduleId, userId);
        List<CommentReplyWithLikesDto> commentReplyWithLikesDtoList = commentReplyRepository.findAllWithLikeCountAndLikeStatusByScheduleIdAndUserID(scheduleId, userId);

        List<CommentListResponse.CommentDto> commentDtoList = commentWithLikesDtoList.stream()
                .map((commentWithLikesDto) -> {
                    List<CommentListResponse.CommentReplyDto> commentReplyDtoList = commentReplyWithLikesDtoList.stream()
                            .filter(commentReplyWithLikesDto -> commentReplyWithLikesDto.getCommentReply().getComment().getId().equals(commentWithLikesDto.getComment().getId()))
                            .map(commentReplyWithLikesDto
                                    -> new CommentListResponse.CommentReplyDto(commentReplyWithLikesDto.getCommentReply(),
                                    commentReplyWithLikesDto.getLikeCount(),
                                    commentReplyWithLikesDto.getLikeStatus()))
                            .toList();
                    return new CommentListResponse.CommentDto(commentWithLikesDto.getComment(),
                            commentReplyDtoList,
                            commentWithLikesDto.getLikeCount(),
                            commentWithLikesDto.getLikeStatus());
                }).toList();

        return new CommentListResponse(commentDtoList);
    }

    private void saveComment(User user, Long scheduleId, String content) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.DATA_NOT_FOUND));
        Comment comment = createComment(user, schedule, content);
        commentRepository.save(comment);
    }

    private void saveCommentReply(User user, Long commentId, String content) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.DATA_NOT_FOUND));
        CommentReply commentReply = createCommentReply(user, comment, content);
        commentReplyRepository.save(commentReply);
    }

    private Comment createComment(User user, Schedule schedule, String content) {
        return Comment.builder()
                .user(user)
                .schedule(schedule)
                .content(content)
                .status(CommentStatus.NORMAL)
                .build();
    }

    private CommentReply createCommentReply(User user, Comment comment, String content) {
        return CommentReply.builder()
                .user(user)
                .comment(comment)
                .content(content)
                .status(CommentStatus.NORMAL)
                .build();
    }
}
