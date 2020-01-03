package me.droreo002.tools.scrapper;

import lombok.Getter;
import me.droreo002.tools.ItemDataDecoder;
import me.droreo002.tools.log.MainLogger;
import me.droreo002.tools.model.GrowtopiaItem;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class ItemScrapper extends JPanel implements ActionListener {

    public static final String WIKI_URL = "https://growtopia.fandom.com/wiki/";

    @Getter
    private final Map<String, String> websiteCookies = new HashMap<>();
    @Getter
    private final List<Integer> failedItem = new ArrayList<>();
    @Getter
    private final List<String> scrappingCache = new ArrayList<>();
    @Getter
    private JProgressBar progressBar;
    @Getter
    private JButton startButton;
    @Getter
    private JTextArea taskOutput;
    @Getter
    private ItemDataDecoder program;
    @Getter
    private int currentId, targetId, currentCacheId;
    @Getter
    private FileWriter cacheWriter;
    @Getter
    private MainLogger logger;

    public ItemScrapper(ItemDataDecoder program, int startId, int stopId) {
        super(new BorderLayout());
        this.program = program;
        this.logger = program.getLogger();
        try {
            File file = new File("scrapping-cache.txt");
            if (!file.exists()) file.createNewFile();
             this.cacheWriter = new FileWriter(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (startId != -1) {
            this.currentId = startId;
        } else {
            this.currentId = 0;
        }
        if (stopId != -1) {
            this.targetId = stopId;
        } else {
            this.targetId = program.getItemCount();
        }

        //Create the demo's UI.
        startButton = new JButton("Start");
        startButton.setActionCommand("start");
        startButton.addActionListener(this);

        progressBar = new JProgressBar(0, 100);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        if (stopId == -1) {
            progressBar.setMaximum(program.getItemCount());
        } else {
            progressBar.setMaximum(stopId - startId);
        }

        taskOutput = new JTextArea(5, 20);
        taskOutput.setMargin(new Insets(5,5,5,5));
        taskOutput.setEditable(false);

        JPanel panel = new JPanel();
        panel.add(startButton);
        panel.add(progressBar);

        add(panel, BorderLayout.PAGE_START);
        add(new JScrollPane(taskOutput), BorderLayout.CENTER);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    }

    /**
     * Add to cache file
     *
     * @param cache Cache string to add
     */
    public void addCache(String cache) {
        scrappingCache.add(cache);
        try {
            cacheWriter.write(cache + System.lineSeparator());
            // Every 500 cache added, we save the cache
            if (currentCacheId >= 100) {
                cacheWriter.flush();
                currentCacheId = 0;
                taskOutput.append("\n");
                taskOutput.append("------------------------- Saving to cache -------------------------\n");
                taskOutput.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        currentCacheId++;
    }
    /**
     * Invoked when the user presses the start button.
     */
    @Override
    public void actionPerformed(ActionEvent evt) {
        startButton.setEnabled(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        Jsoup.connect(WIKI_URL).cookies(this.websiteCookies);

        new Thread(new ScrapperTask()).start();
    }


    /**
     * Increase the current ID and append to
     * task output
     *
     * @param outputText The output text
     */
    public void increase(String outputText) {
        this.taskOutput.append(outputText);
        this.progressBar.setValue(currentId);
        currentId++;
    }

    /**
     * Mark the currentID as failed to scrap,
     * but continue increasing
     *
     * @param outputText The output text
     */
    public void failed(String outputText) {
        this.failedItem.add(currentId);
        increase(outputText);
    }

    /**
     * Called when the scrapping is done
     */
    public abstract void onScrappingDone();

    /**
     * Called when scrapper successfully entered the website
     *
     * @param connection Website's current connection
     * @param document Current document
     * @param currentItem Current item
     * @param itemName Fixed item name
     *
     * @return true if continue, false if cancel the scrapping
     */
    public abstract boolean onWebsiteEnter(Connection connection, Document document, GrowtopiaItem currentItem, String itemName);

    /**
     * Start the scrapper
     *
     * @param itemScrapper The ItemScrapper instance
     */
    public static void start(ItemScrapper itemScrapper) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            //Create and set up the window.
            JFrame frame = new JFrame("Scrapping...");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            //Create and set up the content pane.
            itemScrapper.setOpaque(true); //content panes must be opaque
            frame.setContentPane(itemScrapper);

            //Display the window.
            frame.pack();
            frame.setVisible(true);
        });
    }

    private class ScrapperTask implements Runnable {

        @Override
        public void run() {
            while (currentId < targetId) {
                GrowtopiaItem item = program.getItemData(currentId);
                if (item == null) {
                    increase("[" + currentId + "] Skipping because item is NULL!" + System.lineSeparator());
                    continue;
                }
                if (item.isSeed()) {
                    increase("[" + currentId + "] Skipping because seed!" + System.lineSeparator());
                    continue;
                }

                String itemName = item.getItemName().replace(" ", "_").replace("'", "%27");
                if (itemName.contains("null_item")) {
                    increase("[" + currentId + "] Skipping because invalid item! " + itemName + System.lineSeparator());
                    continue;
                }

                Connection connection = Jsoup.connect(WIKI_URL + itemName).cookies(websiteCookies);
                try {
                    Document document = connection.get();
                    if (!onWebsiteEnter(connection, document, item, itemName)) break;
                } catch (IOException e) {
                    failed("[" + currentId + "] Skipping because error when getting website data!" + System.lineSeparator());
                }
            }
            if (failedItem.isEmpty()) {
                taskOutput.append("Done!\n");
            } else {
                taskOutput.append("Scrapping is now finished!. But we failed on these items! " + failedItem.toString());
            }
            try {
                Thread.sleep(1000);
                cacheWriter.flush();
                cacheWriter.close();
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
            onScrappingDone();
        }
    }
}
