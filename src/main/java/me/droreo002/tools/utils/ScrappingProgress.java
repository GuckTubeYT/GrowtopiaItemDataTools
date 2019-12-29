package me.droreo002.tools.utils;

import me.droreo002.tools.ItemDataDecoder;
import me.droreo002.tools.model.GrowtopiaItem;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.beans.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ScrappingProgress extends JPanel implements ActionListener, PropertyChangeListener {

    private static final String WIKI_URL = "https://growtopia.fandom.com/wiki/";

    private JProgressBar progressBar;
    private JButton startButton;
    private JTextArea taskOutput;
    private Task task;
    private ItemDataDecoder program;
    private String currentItemName;
    private Map<String, String> websiteCookies = new HashMap<>();
    private int startId, stopId;
    private Callback callback;

    private class Task implements Runnable {

        private int currentId;
        private int target;

        public Task() {
            if (startId != -1) this.currentId = startId;
            this.currentId = 0;
            this.target = program.getItemCount();
        }

        @Override
        public void run() {
            while (currentId < target) {
                if (currentId > stopId) break;
                GrowtopiaItem item = program.getItemData(currentId);
                if (item == null) continue;
                String itemName = item.getItemName().replace(" ", "_").replace("'", "%27");
                try {
                    // We ignore seed for now
                    if (item.isSeed()) {
                        currentId++;
                        continue;
                    }
                    Document document = Jsoup.connect(WIKI_URL + itemName).cookies(websiteCookies).get();
                    Element infoBox = document.getElementsByClass("iteminfobox").get(0);
                    String desc = infoBox.getElementsByClass("sect_text").get(0).text().replace("\\u0027", "'");
                    item.setItemDescription(desc);

                    taskOutput.append("[" + currentId + "] Scrapping : " + WIKI_URL + itemName + System.lineSeparator());
                    progressBar.setValue(currentId);
                    currentId++;
                } catch (IOException | IndexOutOfBoundsException e) {
                    taskOutput.append("[" + currentId + "] FAILED ON : " + WIKI_URL + itemName + System.lineSeparator());
                    currentId++;
                }
            }
            taskOutput.append("Done!\n");
            try {
                Thread.sleep(1000);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            callback.onDone();
        }
    }

    public interface Callback {
        void onDone();
    }

    public ScrappingProgress(ItemDataDecoder program, int startId, int stopId, Callback callback) {
        super(new BorderLayout());
        this.program = program;
        this.startId = startId;
        this.stopId = stopId;
        this.callback = callback;

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
     * Invoked when the user presses the start button.
     */
    @Override
    public void actionPerformed(ActionEvent evt) {
        startButton.setEnabled(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        Jsoup.connect(WIKI_URL).cookies(this.websiteCookies);

        new Thread(new Task()).start();
    }

    /**
     * Invoked when task's progress property changes.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("progress".equals(evt.getPropertyName())) {
            int progress = (Integer) evt.getNewValue();
            System.out.println("Progress: " + progress);
            progressBar.setValue(progress);
        }
    }

    /**
     * Create the GUI and show it. As with all GUI code, this must run
     * on the event-dispatching thread.
     */
    private static void createAndShowGUI(ItemDataDecoder program, int startId, int stopId, Callback callback) {
        //Create and set up the window.
        JFrame frame = new JFrame("Scrapping Progress");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create and set up the content pane.
        JComponent newContentPane = new ScrappingProgress(program, startId, stopId, callback);
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public static void start(ItemDataDecoder program, int startId, int stopId, Callback callback) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(() -> createAndShowGUI(program, startId, stopId, callback));
    }
}
