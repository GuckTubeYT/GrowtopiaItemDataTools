package me.droreo002.tools.model;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

// Arranged same as item definition
@Getter
@Setter
public class ItemData {
    private int itemID;
    private byte editableType;
    private byte itemCategory;
    private byte actionType;
    private byte hitSoundType;
    private String itemName;
    private String tilesPageName;
    private int tilesPageHash;
    private byte itemKind;
    private int val1;
    private byte textureX;
    private byte textureY;
    private byte spreadType;
    private byte isStripyWallpaper;
    private byte collisionType;
    private byte breakHits;
    private int dropChance;
    private byte clothingType;
    private short rarity;
    private byte maxAmount;
    private String extraFile;
    private int extraFileHash;
    private int audioVolume;
    private String petName;
    private String petPrefix;
    private String petSuffix;
    private String petAbility;
    private byte seedBase;
    private byte seedOverlay;
    private byte treeBase;
    private byte treeLeaves;
    private int seedColor;
    private int seedOverlayColor;
    private int deletedIngredientsValue;
    private int growTime;
    private short val2;
    private short isRayman;
    private String extraOptions;
    private String texture2;
    private String extraOptions2;
    private byte[] unknownBytes;
    private String punchOptions;

    public ItemData() { }

    @Override
    public String toString() {
        return "ItemData{" +
                "itemID=" + itemID +
                ", editableType=" + editableType +
                ", itemCategory=" + itemCategory +
                ", actionType=" + actionType +
                ", hitSoundType=" + hitSoundType +
                ", itemName='" + itemName + '\'' +
                ", tilesPageName='" + tilesPageName + '\'' +
                ", tilesPageHash=" + tilesPageHash +
                ", itemKind=" + itemKind +
                ", val1=" + val1 +
                ", textureX=" + textureX +
                ", textureY=" + textureY +
                ", spreadType=" + spreadType +
                ", isStripyWallpaper=" + isStripyWallpaper +
                ", collisionType=" + collisionType +
                ", breakHits=" + breakHits +
                ", dropChance=" + dropChance +
                ", clothingType=" + clothingType +
                ", rarity=" + rarity +
                ", maxAmount=" + maxAmount +
                ", extraFile='" + extraFile + '\'' +
                ", extraFileHash=" + extraFileHash +
                ", audioVolume=" + audioVolume +
                ", petName='" + petName + '\'' +
                ", petPrefix='" + petPrefix + '\'' +
                ", petSuffix='" + petSuffix + '\'' +
                ", petAbility='" + petAbility + '\'' +
                ", seedBase=" + seedBase +
                ", seedOverlay=" + seedOverlay +
                ", treeBase=" + treeBase +
                ", treeLeaves=" + treeLeaves +
                ", seedColor=" + seedColor +
                ", seedOverlayColor=" + seedOverlayColor +
                ", deletedIngredientsValue=" + deletedIngredientsValue +
                ", growTime=" + growTime +
                ", val2=" + val2 +
                ", isRayman=" + isRayman +
                ", extraOptions='" + extraOptions + '\'' +
                ", texture2='" + texture2 + '\'' +
                ", extraOptions2='" + extraOptions2 + '\'' +
                ", unknownBytes=" + Arrays.toString(unknownBytes) +
                ", punchOptions='" + punchOptions + '\'' +
                '}';
    }
}
