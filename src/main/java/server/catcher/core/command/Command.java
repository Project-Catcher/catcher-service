package server.catcher.core.command;

public interface Command<T> {
    T execute();
}
