package me.droreo002.tools.model;

import lombok.Getter;

public class GrowtopiaItem {
    @Getter
    private ItemData itemData;
    @Getter
    private String description;

    public GrowtopiaItem(ItemData itemData, String description) {
        this.itemData = itemData;
        this.description = description;
    }
}
