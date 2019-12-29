package me.droreo002.tools.commands.model;

import me.droreo002.tools.ItemDataDecoder;
import me.droreo002.tools.commands.CommandExecutor;
import me.droreo002.tools.log.MainLogger;
import me.droreo002.tools.model.GrowtopiaItem;
import org.fusesource.jansi.Ansi;

import java.lang.reflect.Field;

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
        GrowtopiaItem data;
        String key = args[0];
        String property = "";
        try {
            data = program.getItemData(Integer.parseInt(key));
        } catch (NumberFormatException e) {
            data = program.getItemData(key);
        }
        if (args.length > 1) property = args[1];

        if (property.isEmpty()) {
            if (data == null) {
                logger.info("Item cannot be found! [" + key + "]");
            } else {
                logger.info("Item found!: " + data.toString());
            }
        } else {
            if (data == null) {
                logger.info("Item cannot be found! [" + key + "]");
            } else {
                try {
                    Field field = data.getClass().getDeclaredField(property.replace(" ", ""));
                    if (!field.isAccessible()) field.setAccessible(true);
                    logger.info("Item property [" + property + "]: " + field.get(data));
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    logger.info(Ansi.ansi().fg(Ansi.Color.RED).a("Failed to find property: " + property).toString());
                }
            }
        }
    }

    @Override
    public String getCommand() {
        return "item";
    }
}
