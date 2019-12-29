package me.droreo002.tools;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import jline.console.ConsoleReader;
import lombok.Getter;
import me.droreo002.tools.commands.CommandHandler;
import me.droreo002.tools.log.MainLogger;
import me.droreo002.tools.model.ItemData;
import me.droreo002.tools.model.ProgramConfig;
import me.droreo002.tools.utils.Process;
import me.droreo002.tools.utils.Utilities;
import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static me.droreo002.tools.utils.Utilities.*;
import static org.fusesource.jansi.Ansi.Color.*;
import static org.fusesource.jansi.Ansi.*;

public class ItemDataDecoder {

    private static final boolean ENABLE_DEBUG = false;
    private static final String APP_DATA = System.getenv("APPDATA").replace("\\Roaming", "\\Local");
    private static final String DATA_FOLDER = "loaded";

    @Getter
    private static ItemDataDecoder instance;

    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            System.out.println("Please specify argument! (Reload item data?) [true|false]");
            new Scanner(System.in).nextLine();
            System.exit(0);
            return;
        }
        instance = new ItemDataDecoder();
        instance.run(args);
    }

    @Getter
    private final List<ItemData> loadedData = new ArrayList<>();
    @Getter
    private int itemCount = 0;
    @Getter
    private int itemDataVersion = 0;
    @Getter
    private int itemDataHash;
    @Getter
    private int loadingCount;
    @Getter
    private ConsoleReader consoleReader;
    @Getter
    private MainLogger logger;
    @Getter
    private CommandHandler commandHandler;
    @Getter
    private ProgramConfig config;
    @Getter
    private Gson gson;

    private void run(String[] args) throws IOException {
        gson = new GsonBuilder().setPrettyPrinting().create();
        File configFile = new File("config.json");
        if (configFile.exists()) {
            config = gson.fromJson(new JsonReader(new FileReader(configFile)), ProgramConfig.class);
        } else {
            config = new ProgramConfig(0); // Setup default
            FileWriter writer = new FileWriter(configFile);
            gson.toJson(config, writer);
            writer.flush();
            writer.close();
        }

        boolean reloadData = Boolean.parseBoolean(args[0]);

        System.setProperty("library.jansi.version", "MainLogger");
        AnsiConsole.systemInstall();

        consoleReader = new ConsoleReader();
        consoleReader.setExpandEvents( false );

        logger = new MainLogger( "MainLogger", "program.log", consoleReader );
        commandHandler = new CommandHandler(this);

        System.out.println(Ansi.ansi().fg(Ansi.Color.GREEN).a("                    Running on Version v1.0").reset().toString());
        System.out.println(" ");
        log(ansi().a("Config has been loaded!").fg(GREEN).a(" (config.json)"));
        log("Getting items.dat on " + APP_DATA);
        File itemData = new File(APP_DATA + "\\Growtopia\\cache\\items.dat");
        File loadedItems = new File(DATA_FOLDER);

        byte[] all = Files.readAllBytes(Paths.get(itemData.toURI()));

        ByteBuffer buffer = ByteBuffer.wrap(all);
        buffer.order(ByteOrder.LITTLE_ENDIAN);

        this.itemDataHash = getHash(itemData);
        this.itemDataVersion = buffer.getShort();
        this.itemCount = buffer.getInt();

        Process process = new Process();

        log(" ");
        log(ansi().fg(WHITE).a("Version: ").fg(RED).a(itemDataVersion));
        log(ansi().fg(WHITE).a("Hash: ").fg(RED).a(itemDataHash));
        log(ansi().fg(WHITE).a("Item Count: ").fg(RED).a(itemCount));

        if (reloadData) {
            log("Starting item.dat reading..");
            outer: for (int i = 0; i < itemCount; i++) {
                loadingCount = i;
                ItemData actualData = new ItemData();
                Field[] fields = actualData.getClass().getDeclaredFields();
                for (Field dataField : fields) {
                    if (dataField.getName().equals("itemID")) debug("-------------------- Checking on count: " + i);
                    if (!dataField.isAccessible()) dataField.setAccessible(true);
                    Class<?> type = dataField.getType();
                    try {
                        if (int.class.isAssignableFrom(type)) {
                            int integerData = buffer.getInt();
                            dataField.set(actualData, integerData);
                            debug(dataField.getName() + " [" + type.getName() + "]: " + integerData);
                        }

                        if (byte.class.isAssignableFrom(type)) {
                            byte byteData = (byte) (buffer.get() & 0xff);
                            dataField.set(actualData, byteData);
                            debug(dataField.getName() + " [" + type.getName() + "] : " + byteData + " [Signed]");
                        }

                        if (String.class.isAssignableFrom(type)) {
                            int strLength = buffer.get() & 0xff;
                            buffer.get(); // Skip 1 byte
                            if (type.getName().equals("punchOptions") && !(itemDataVersion >= 11)) continue;
                            if (strLength == 0) {
                                debug(dataField.getName() + " [" + type.getName() + "] : EMPTY [" + strLength + "]");
                                continue;
                            }
                            byte[] byteData = new byte[strLength];
                            buffer.get(byteData);
                            String actualString;

                            if (dataField.getName().equals("itemName")) {
                                actualString = Utilities.decodeItemName(byteData, actualData.getItemID());
                            } else {
                                actualString = new String(byteData);
                            }

                            dataField.set(actualData, actualString);
                            debug(dataField.getName() + " [" + type.getName() + "] : " + actualString + " [" + strLength + "]");
                        }

                        if (byte[].class.isAssignableFrom(type)) {
                            if (dataField.getName().equals("unknownBytes")) {
                                debug(dataField.getName() + " [Byte Array] : Skipping unknown bytes..");
                                try {
                                    buffer.position(buffer.position() + 80);
                                } catch (IllegalArgumentException e) {
                                    debug("-------------------- Success! [" + i + "]");
                                    break outer;
                                }
                            }
                        }

                        if (short.class.isAssignableFrom(type)) {
                            short readData = buffer.getShort();
                            dataField.set(actualData, readData);
                            debug(dataField.getName() + " [" + type.getName() + "] : " + readData);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        break outer;
                    }
                }
                loadedData.add(actualData);
            }

            log("Saving loaded items.. This might take a while!");
            for (ItemData data : loadedData) {
                FileWriter writer = new FileWriter(new File(loadedItems, data.getItemID() + ".json"));
                gson.toJson(data, writer);
                writer.flush();
                writer.close();
            }
        } else {
            log(ansi().a("Loading items data from folder ").fg(GREEN).a("\"loaded\""));
            File[] listFile = loadedItems.listFiles();
            if (listFile == null || listFile.length < itemCount) {
                log(ansi().fg(RED).a("Cache is invalid!. Please reload data!"));
                pause();
                return;
            }
        }

        log(ansi().a("Successfully loaded " + loadedData.size() + " items! Took (").fg(GREEN).a(process.stop("%totalTimems")).reset().a(")! For help type \"help\""));

        // Input
        String line;
        while ((line = consoleReader.readLine( ">" )) != null) {
            if (line.equals("stop")) {
                break;
            }
            if (!commandHandler.execute(line)) {
                log(ansi().fg(RED).a("Command cannot be found!"));
            }
        }

        pause();
    }

    private void pause() {
        log("Huh?, program ended. Press enter to continue");
        new Scanner(System.in).nextLine();
        System.exit(0);
    }

    /**
     * Get ItemData by ID
     *
     * @param id The item id
     * @return the item data if there's any
     */
    @Nullable
    public ItemData getItemData(int id) {
        return loadedData.stream().filter(item -> item.getItemID() == id).findAny().orElse(null);
    }

    /**
     * Get item data by Name
     *
     * @param name The item name
     * @return the item data if there's any
     */
    @Nullable
    public ItemData getItemData(String name) {
        return loadedData.stream().filter(item -> item.getItemName().toLowerCase().equals(name.toLowerCase())).findAny().orElse(null);
    }

    /*
    Logging method
     */

    private void log(Ansi ansi) {
        log(ansi.reset().toString());
    }

    private void log(String s) {
        logger.info(s);
    }

    private void debug(String s) {
        if (!ENABLE_DEBUG) return;
        debug(s, false);
    }

    private void debug(String s, boolean ignoreCheck) {
        if (!ignoreCheck) {
            if (!(loadingCount < 3)) return;
        }
        System.out.println(s);
    }
}
