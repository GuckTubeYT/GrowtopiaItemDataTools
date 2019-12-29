package me.droreo002.tools.commands;

import me.droreo002.tools.log.MainLogger;

public interface CommandExecutor {
    void execute(MainLogger logger, String[] args);
    String getCommand();
}
