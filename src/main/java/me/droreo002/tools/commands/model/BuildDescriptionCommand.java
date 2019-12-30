package me.droreo002.tools.commands.model;

import me.droreo002.tools.ItemDataDecoder;
import me.droreo002.tools.commands.CommandExecutor;
import me.droreo002.tools.log.MainLogger;
import me.droreo002.tools.scrapper.ItemScrapper;
import me.droreo002.tools.scrapper.model.ItemDescriptionScrapper;
import org.fusesource.jansi.Ansi;

public class BuildDescriptionCommand implements CommandExecutor {

    private ItemDataDecoder program;

    public BuildDescriptionCommand(ItemDataDecoder program) {
        this.program = program;
    }

    @Override
    public void execute(MainLogger logger, String[] args) {
        if (args.length == 2) {
            int startId = Integer.parseInt(args[0]);
            int stopId = Integer.parseInt(args[1]);
            if (startId == -1 || stopId == -1) {
                logger.info("StartID or StopID cannot be negative!");
                return;
            }
            logger.info(Ansi.ansi().a("ID [" + startId + " -> " + stopId + "] Now opening progress UI").toString());
            start(startId, stopId);
        } else {
            logger.info(Ansi.ansi().a("[Scrapping All] Now opening progress UI").toString());
            start(-1, -1);
        }
    }

    private void start(int startId, int stopId) {
        ItemScrapper.start(new ItemDescriptionScrapper(program, startId, stopId));
    }

    @Override
    public String getCommand() {
        return "build-description";
    }
}
