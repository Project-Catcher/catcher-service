package server.catcher.core.command;

import org.springframework.stereotype.Component;

@Component
public class ScheduleCommandExecutor implements CommandExecutor {
    @Override
    public <T> T run(Command<T> command) {
        return command.execute();
    }
}
