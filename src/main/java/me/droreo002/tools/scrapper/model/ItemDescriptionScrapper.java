package me.droreo002.tools.scrapper.model;

import me.droreo002.tools.ItemDataDecoder;
import me.droreo002.tools.model.GrowtopiaItem;
import me.droreo002.tools.scrapper.ItemScrapper;
import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;
import java.io.IOException;

public class ItemDescriptionScrapper extends ItemScrapper {

    public ItemDescriptionScrapper(ItemDataDecoder program, int startId, int stopId) {
        super(program, startId, stopId);
    }

    @Override
    public void onScrappingDone() {
        try {
            getProgram().saveData(new File(ItemDataDecoder.DATA_FOLDER));
        } catch (IOException e) {
            e.printStackTrace();
        }
        getLogger().info("Data has been saved!");
    }

    @Override
    public boolean onWebsiteEnter(Connection connection, Document document, GrowtopiaItem item, String itemName) {
        int currentId = getCurrentId();
        if (!item.getItemDescription().isEmpty()) {
            increase("[" + currentId + "] Skipping : " + itemName + " [Already has Description]" + System.lineSeparator());
            return true;
        }

        try {
            Element infoBox = document.getElementsByClass("iteminfobox").get(0);
            String desc = infoBox.getElementsByClass("sect_text").get(0).text();
            item.setItemDescription(desc);
            addCache(currentId + "|" + item.getItemName() + "|" + desc);

            if (desc.length() > 50) {
                desc = desc.substring(0, 30) + "...";
            }
            increase("[" + currentId + "] Scrapping : " + WIKI_URL + itemName + " [Success] [" + desc + "]" + System.lineSeparator());
        } catch (Exception e) {
            failed("[" + currentId + "] FAILED ON : " + WIKI_URL + itemName + " [Caused by: " + e.getMessage() + "]" + System.lineSeparator());
        }
        return true;
    }
}
