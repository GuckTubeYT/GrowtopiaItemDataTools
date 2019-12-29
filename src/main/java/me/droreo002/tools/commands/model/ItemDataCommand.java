package me.droreo002.tools.commands.model;

import me.droreo002.tools.ItemDataDecoder;
import me.droreo002.tools.commands.CommandExecutor;
import me.droreo002.tools.log.MainLogger;
import me.droreo002.tools.model.ItemData;

import javax.xml.crypto.Data;

public class ItemDataCommand implements CommandExecutor {

    private ItemDataDecoder program;

    public ItemDataCommand(ItemDataDecoder program) {
        this.program = program;
    }

    @Override
    public void execute(MainLogger logger, String[] args) {
        if (args.length == 0) {
            logger.info("Invalid argument!");
            return;
        }
        ItemData data;
        String key = args[0];
        try {
            data = program.getItemData(Integer.parseInt(key));
        } catch (NumberFormatException e) {
            data = program.getItemData(key);
        }
        if (data == null) {
            logger.info("Item cannot be found! [" + key + "]");
        } else {
            logger.info("Item found!: " + data.toString());
        }
    }

    @Override
    public String getCommand() {
        return "item";
    }
}
