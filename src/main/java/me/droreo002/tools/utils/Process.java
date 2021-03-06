package me.droreo002.tools.utils;

import lombok.Getter;

public class Process {

    @Getter
    private long start;
    @Getter
    private long end;

    public Process() {
        this.start = System.nanoTime();
    }

    /**
     * Stop the process, will replace %totalTime in string
     *
     * @param msg The message
     * @return the formatted message
     */
    public String stop(String msg) {
        this.end = System.nanoTime();
        long totalTime = end - start;
        return msg.replace("%totalTime", String.valueOf(totalTime / 1000000L));
    }
}

