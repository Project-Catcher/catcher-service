package com.catcher.core.domain;

import com.catcher.core.domain.command.Command;
import org.springframework.stereotype.Component;

@Component
public class ScheduleCommandExecutor implements CommandExecutor {
    @Override
    public <T> T run(Command<T> command) {
        return command.execute();
    }
}
