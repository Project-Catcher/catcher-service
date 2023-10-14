package com.catcher.core.domain.command;

public interface CommandExecutor {
     <T> T run(Command<T> command);
}
