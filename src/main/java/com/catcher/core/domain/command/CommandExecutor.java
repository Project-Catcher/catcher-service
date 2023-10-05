package com.catcher.core.domain.command;

import com.catcher.core.domain.command.Command;

public interface CommandExecutor<T> {
    T run(Command command);
}
