package com.catcher.core.service;

import com.catcher.core.domain.entity.Comment;
import com.catcher.datasource.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    @Transactional
    public Page<Comment> findByParentIsNull(final Pageable pageable) {
        return commentRepository.findByParentIsNull(pageable);
    }

    @Transactional
    public Comment saveSingleComment(final Long userId, final String contents) {
        return commentRepository.save(Comment
                .builder()
                .userId(userId)
                .contents(contents)
                .build());
    }

    @Transactional
    public Comment saveSingleReply(Long parentId, Long userId, String contents) {
        final Comment parentComment = commentRepository
                .findById(parentId)
                .orElseThrow(); //TODO: fill custom exception

        final Comment reply = Comment
                .builder()
                .userId(userId)
                .parent(parentComment)
                .contents(contents)
                .build();

        parentComment.getReplies().add(reply);

        return reply;
    }
}
