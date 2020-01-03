package me.droreo002.tools.commands.model;

import me.droreo002.tools.commands.CommandExecutor;
import me.droreo002.tools.log.MainLogger;

public class HelpCommand implements CommandExecutor {

    @Override
    public void execute(MainLogger logger, String[] args) {
        logger.info("Command list: ");
        logger.info("   > /item <id|name> [propertyKey] | Get item information by ID or Name");
        logger.info("   > /build-description [start_id] [stop_id] | Get item description via Growtopia Wiki");
        logger.info("   > /build-name | Build the item name to .txt file");
        logger.info("   > /apply-plain-data | Apply the plain data to .json files");
        logger.info("");
        logger.info("To run, type the command without / (<> Required, [] Optional)");
    }

    @Override
    public String getCommand() {
        return "help";
    }
}
