package com.catcher.core.domain;

import com.catcher.core.domain.command.Command;

public interface CommandExecutor {
    <T> T run(Command<T> command);
}