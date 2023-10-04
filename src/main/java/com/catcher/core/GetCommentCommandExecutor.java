package com.catcher.core;

import com.catcher.core.domain.command.Command;
import com.catcher.core.domain.command.GetParentCommentsByPageCommand;
import com.catcher.core.domain.entity.Comment;
import com.catcher.datasource.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class GetCommentCommandExecutor implements CommandExecutor<Page<Comment>> {

    private final CommentRepository commentRepository;

    @Override
    @Transactional
    public Page<Comment> run(final Command command) {
        //1. datasource layer 호출(DB)
        //2. 가공해서 넘겨줘야 한다
        return switch (command.getClass().getSimpleName()) {
            case "GetParentCommentsByPageCommand" -> getParentCommentsByPage(command);
            default -> null;
        };
    }

    private Page<Comment> getParentCommentsByPage(final Command command) {

        final GetParentCommentsByPageCommand getParentCommentsByPageCommand = (GetParentCommentsByPageCommand) command;
        return commentRepository.findByParentIsNull(getParentCommentsByPageCommand.getPageable());
    }
}
