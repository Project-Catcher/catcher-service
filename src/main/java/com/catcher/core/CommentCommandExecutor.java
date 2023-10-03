package com.catcher.core;

import com.catcher.core.domain.command.Command;
import com.catcher.core.domain.command.PostCommentCommand;
import com.catcher.core.domain.entity.Comment;
import com.catcher.datasource.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommentCommandExecutor implements CommandExecutor<Comment> {

    private final CommentRepository commentRepository;

    @Override
    public Comment run(final Command command) {
        //1. datasource layer 호출(DB)
        //2. 가공해서 넘겨줘야 한다
        return switch (command.getClass().getSimpleName()) {
            case "PostCommentCommand" -> postComment(command);
            default -> null;
        };
    }

    private Comment postComment(final Command command) {

        final PostCommentCommand postCommentCommand = (PostCommentCommand)command;
        return commentRepository.save(Comment
                .builder()
                .contents(postCommentCommand.getContents())
                .build());
    }

}
