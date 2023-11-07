package com.tac.guns.common.container.slot;

public enum SlotType {
    SCOPE("scope", "Scope"),            // 0
    BARREL("barrel", "Barrel"),            // 1
    STOCK("stock", "Stock"),            // 2
    UNDER_BARREL("under_barrel", "Under_Barrel"),            // 3
    SIDE_RAIL("side_rail", "Side_Rail"),            // 4
    EXTENDED_MAG("extended_mag", "Extended_Mag"), // 5
    GUN_SKIN("gun_skin", "Gun_Skin");           // 6


    private String translationKey;
    private String tagKey;

    SlotType(String translationKey, String tagKey) {
        this.translationKey = translationKey;
        this.tagKey = tagKey;
    }

    public String getTranslationKey() {
        return this.translationKey;
    }

    public String getTagKey() {
        return this.tagKey;
    }

}
