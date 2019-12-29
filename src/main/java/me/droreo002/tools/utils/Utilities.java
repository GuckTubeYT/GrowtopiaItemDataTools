package me.droreo002.tools.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public final class Utilities {

    /**
     * Get the items.dat hash
     *
     * @param itemsDat The items.dat file
     * @return The file hash as int
     */
    public static int getHash(File itemsDat) {
        int h = 0x55555555;
        long len = itemsDat.length();
        byte[] c = readFileToByteArray(itemsDat, true);

        for (int i = 0; i < len; i++) {
            h = (h >>> 27) + (h << 5) + ((int) c[i] & 0xff);
        }

        return h;
    }

    public static int cyclicHash(String s) {
        int h=0;
        for (int i=0; i < s.length(); i++) {
            // 5-bit cyclic shift of the running sum:
            h = (h << 5) | (h >>> 27);
            h += (int) s.charAt(i);   // add in next character
        }
        return Math.abs(h);
    }

    /**
     * This method uses java.io.FileInputStream to read
     * file content into a byte array
     *
     * @param file The file to read
     * @return the bytes
     */
    public static byte[] readFileToByteArray(File file, boolean itemsDat) {
        FileInputStream fis;

        if (itemsDat) {
            byte[] bArray = new byte[(int) file.length() + 1];
            try {
                fis = new FileInputStream(file);
                bArray[(int) file.length()] = 0;

                fis.read(bArray);
                fis.close();
            } catch (IOException ioExp) {
                ioExp.printStackTrace();
            }

            return bArray;
        } else {
            byte[] bArray = new byte[(int) file.length()];
            try {
                fis = new FileInputStream(file);

                fis.read(bArray);
                fis.close();
            } catch (IOException ioExp) {
                ioExp.printStackTrace();
            }

            return bArray;
        }
    }

    /**
     * Convert string to number
     *
     * @param str String to convert
     * @return The number
     */
    public static int toNumber(String str) {
        return ord(str);
    }

    /**
     * Convert a string into a number
     *
     * @param s The string to convert
     * @return The number
     */
    public static int ord(String s) {
        return s.length() > 0 ? (s.getBytes(StandardCharsets.UTF_8)[0] & 0xff) : 0;
    }

    /**
     * Convert the byte to a readable byte as string
     *
     * @param packetData The packet data
     * @param readTo How much to read
     * @return String of bytes (not converted)
     */
    public static String readableByte(byte[] packetData, int readTo) {
        byte[] dat = new byte[(readTo == -1) ? packetData.length : readTo];
        if (readTo != -1) {
            System.arraycopy(packetData, 0, dat, 0, readTo);
        } else {
            dat = packetData;
        }
        StringBuilder builder = new StringBuilder();
        for (byte b : dat) {
            builder.append((b & 0xFF));
        }
        return builder.toString();
    }

    /**
     * Decode item data name
     *
     * @param encrypted The encrypted byte
     * @param itemID The item id
     * @return the decoded item name
     */
    public static String decodeItemName(byte[] encrypted, int itemID) {
        String key = "PBG892FXX982ABC*"; // Oh wow?
        byte[] bytes = new byte[encrypted.length];
        for (int i = 0; i < encrypted.length; i++) {
            try {
                bytes[i] = (byte) (encrypted[i] ^ (key.charAt((i + itemID) % key.length())));
            } catch (StringIndexOutOfBoundsException ignored) {
            }
        }
        return new String(bytes);
    }

    /**
     * Convert bytes to a hex
     *
     * @param bytes The bytes to convert
     * @return Actual hex of the bytes
     */
    public static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte aByte : bytes) {
            sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }

    /**
     * Convert byte to hex
     * we use cheaty way here, also not recommend
     * to directly write to file after converting
     *
     * @param b The byte
     * @return Integer byte
     */
    public static int toHex(byte b) {
        String st = String.format("%02X", b);
        return Integer.parseInt(st);
    }
}
