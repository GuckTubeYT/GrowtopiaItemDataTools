package me.droreo002.tools.commands.model;

import me.droreo002.tools.ItemDataDecoder;
import me.droreo002.tools.commands.CommandExecutor;
import me.droreo002.tools.log.MainLogger;
import me.droreo002.tools.model.GrowtopiaItem;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class BuildNameCommand implements CommandExecutor {

    private ItemDataDecoder program;

    public BuildNameCommand(ItemDataDecoder program) {
        this.program = program;
    }

    @Override
    public void execute(MainLogger logger, String[] args) {
        try {
            FileWriter writer = new FileWriter(new File("plain-data/Name.txt"));
            for (GrowtopiaItem growtopiaItem : program.getGrowtopiaItems()) {
                writer.write(growtopiaItem.getItemID() + "|" + growtopiaItem.getItemName() + "\n");
            }
            writer.flush();
            writer.close();
            logger.info("Success!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getCommand() {
        return "build-name";
    }
}
