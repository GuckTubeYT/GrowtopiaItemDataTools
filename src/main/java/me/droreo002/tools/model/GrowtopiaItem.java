package me.droreo002.tools.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;

// Arranged same as item definition
@Getter
@Setter
public class GrowtopiaItem {
    private int itemID = 0;
    private byte editableType = 0;
    private byte itemCategory = 0;
    private byte actionType = 0;
    private byte hitSoundType = 0;
    private String itemName = "null_item";
    private String tilesPageName = "";
    private int tilesPageHash = 0;
    private byte itemKind = 0;
    private int val1 = 0;
    private byte textureX = 0;
    private byte textureY = 0;
    private byte spreadType = 0;
    private boolean stripyWallpaper = false;
    private byte collisionType = 0;
    private byte breakHits = 0;
    private int dropChance = 0;
    private byte clothingType = 0;
    private short rarity = 999;
    private byte maxAmount = (byte) 200;
    private String extraFile = "";
    private int extraFileHash = 0;
    private int audioVolume = 400;
    private String petName = "";
    private String petPrefix = "";
    private String petSuffix = "";
    private String petAbility = "";
    private byte seedBase = 0;
    private byte seedOverlay = 0;
    private byte treeBase = 0;
    private byte treeLeaves = 0;
    private int seedColor = 0;
    private int seedOverlayColor = 0;
    private int deletedIngredientsValue = 0;
    private int growTime = 0;
    private short val2 = 0;
    private boolean rayman = false;
    private String extraOptions = "";
    private String texture2 = "";
    private String extraOptions2 = "";
    private byte[] unknownBytes = null;
    private String punchOptions = "";

    @CustomValue
    private String itemDescription = "";

    public GrowtopiaItem() {}

    private GrowtopiaItem(int itemID) {
        this.itemID = itemID;
    }

    public static GrowtopiaItem empty(int id) {
        return new GrowtopiaItem(id);
    }

    public boolean isSeed() {
        return itemName.contains("Seed");
    }

    @Override
    public String toString() {
        return  "\nitemID=" + itemID +
                "\neditableType=" + editableType +
                "\nitemCategory=" + itemCategory +
                "\nactionType=" + actionType +
                "\nhitSoundType=" + hitSoundType +
                "\nitemName='" + itemName + '\'' +
                "\ntilesPageName='" + tilesPageName + '\'' +
                "\ntilesPageHash=" + tilesPageHash +
                "\nitemKind=" + itemKind +
                "\nval1=" + val1 +
                "\ntextureX=" + textureX +
                "\ntextureY=" + textureY +
                "\nspreadType=" + spreadType +
                "\nisStripyWallpaper=" + stripyWallpaper +
                "\ncollisionType=" + collisionType +
                "\nbreakHits=" + breakHits +
                "\ndropChance=" + dropChance +
                "\nclothingType=" + clothingType +
                "\nrarity=" + rarity +
                "\nmaxAmount=" + maxAmount +
                "\nextraFile='" + extraFile + '\'' +
                "\nextraFileHash=" + extraFileHash +
                "\naudioVolume=" + audioVolume +
                "\npetName='" + petName + '\'' +
                "\npetPrefix='" + petPrefix + '\'' +
                "\npetSuffix='" + petSuffix + '\'' +
                "\npetAbility='" + petAbility + '\'' +
                "\nseedBase=" + seedBase +
                "\nseedOverlay=" + seedOverlay +
                "\ntreeBase=" + treeBase +
                "\ntreeLeaves=" + treeLeaves +
                "\nseedColor=" + seedColor +
                "\nseedOverlayColor=" + seedOverlayColor +
                "\ndeletedIngredientsValue=" + deletedIngredientsValue +
                "\ngrowTime=" + growTime +
                "\nval2=" + val2 +
                "\nisRayman=" + rayman +
                "\nextraOptions='" + extraOptions + '\'' +
                "\ntexture2='" + texture2 + '\'' +
                "\nextraOptions2='" + extraOptions2 + '\'' +
                "\nunknownBytes=" + Arrays.toString(unknownBytes) +
                "\npunchOptions='" + punchOptions + '\'';
    }
}
