package com.catcher.core.service;

import com.catcher.core.GetCommentCommandExecutor;
import com.catcher.core.domain.command.GetParentCommentsByPageCommand;
import com.catcher.core.domain.entity.Comment;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final GetCommentCommandExecutor getCommentCommandExecutor;

    public Page<Comment> getCommentsWithSize(final Pageable pageable) {
        return getCommentCommandExecutor.run(new GetParentCommentsByPageCommand(pageable));
    }
}
