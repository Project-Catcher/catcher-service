package server.catcher.core.command;

public interface CommandExecutor {
    <T> T run(Command<T> command);
}
