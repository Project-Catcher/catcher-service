package com.catcher.core;

import com.catcher.core.domain.command.Command;
import com.catcher.core.domain.command.PostCommentCommand;
import com.catcher.core.domain.command.PostCommentReplyCommand;
import com.catcher.core.domain.entity.Comment;
import com.catcher.datasource.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class CommentCommandExecutor implements CommandExecutor<Comment> {

    private final CommentRepository commentRepository;

    @Override
    @Transactional
    public Comment run(final Command command) {
        //1. datasource layer 호출(DB)
        //2. 가공해서 넘겨줘야 한다
        return switch (command.getClass().getSimpleName()) {
            case "PostCommentCommand" -> postComment(command);
            case "PostCommentReplyCommand" -> postCommentReply(command);
            default -> null;
        };
    }

    private Comment postComment(final Command command) {

        final PostCommentCommand postCommentCommand = (PostCommentCommand)command;
        return commentRepository.save(Comment
                .builder()
                .userId(postCommentCommand.getUserId())
                .contents(postCommentCommand.getContents())
                .build());
    }

    private Comment postCommentReply(final Command command) {

        final PostCommentReplyCommand postCommentReplyCommand = (PostCommentReplyCommand) command;

        final Comment parentComment = commentRepository
                .findById(postCommentReplyCommand.getParentId())
                .orElseThrow(); //TODO: fill custom exception

        final Comment reply = Comment
                .builder()
                .userId(postCommentReplyCommand.getUserId())
                .parent(parentComment)
                .contents(postCommentReplyCommand.getContents())
                .build();

        parentComment.getReplies().add(reply);

        return reply;
    }

}
