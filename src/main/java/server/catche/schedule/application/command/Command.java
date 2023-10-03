package server.catche.schedule.application.command;

public interface Command<T> {
    T execute();
}
