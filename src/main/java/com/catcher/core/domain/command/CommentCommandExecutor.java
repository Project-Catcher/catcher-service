package com.catcher.core.domain.command;

import org.springframework.stereotype.Component;

@Component
public class CommentCommandExecutor implements CommandExecutor {

    @Override
    public <T> T run(Command<T> command) {
        return command.execute();
    }

}
