package me.droreo002.tools.model;

import lombok.Getter;

public class ProgramConfig {
    @Getter
    private int lastScrapped;

    public ProgramConfig(int lastScrapped) {
        this.lastScrapped = lastScrapped;
    }
}
