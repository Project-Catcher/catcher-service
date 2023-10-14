package com.catcher.core.domain.command;

import com.catcher.core.domain.entity.Comment;
import com.catcher.core.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class GetCommentCommand implements Command<Page<Comment>> {

    private final CommentService commentService;

    private final Pageable pageable;
    @Override
    public Page<Comment> execute() {
        return commentService.findByParentIsNull(pageable);
    }
}
