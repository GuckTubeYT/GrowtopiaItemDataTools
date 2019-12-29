package me.droreo002.tools.commands.model;

import me.droreo002.tools.ItemDataDecoder;
import me.droreo002.tools.commands.CommandExecutor;
import me.droreo002.tools.log.MainLogger;
import me.droreo002.tools.utils.ScrappingProgress;
import org.fusesource.jansi.Ansi;

import java.io.File;
import java.io.IOException;

public class BuildDescriptionCommand implements CommandExecutor {

    private ItemDataDecoder program;

    public BuildDescriptionCommand(ItemDataDecoder program) {
        this.program = program;
    }

    @Override
    public void execute(MainLogger logger, String[] args) {
        if (args.length < 2) {
            logger.info("Invalid argument!");
            return;
        }
        int startId = Integer.parseInt(args[0]);
        int stopId = Integer.parseInt(args[1]);

        if (startId == -1 && stopId != -1) {
            logger.info("Both must be negative to start by default!");
            return;
        }
        if (stopId == -1 && startId != -1) {
            logger.info("Both must be negative to start by default!");
            return;
        }
        if (startId > stopId) {
            logger.info("StartID cannot be greater than stopID!");
            return;
        }

        logger.info(Ansi.ansi().a("ID [" + startId + " <-> " + stopId + "] Now opening progress UI ").fg(Ansi.Color.GREEN).a("[Program will close after completion or when cancelled]").reset().toString());
        ScrappingProgress.start(program, startId, stopId, () -> {
            try {
                program.saveData(new File(ItemDataDecoder.DATA_FOLDER));
            } catch (IOException e) {
                e.printStackTrace();
            }
            logger.info("Data has been saved!");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.exit(0);
        });
    }

    @Override
    public String getCommand() {
        return "build-description";
    }
}
