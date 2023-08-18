package com.tac.guns.client.settings;

import net.minecraft.client.settings.KeyBinding;

import java.util.ArrayList;

/**
 * @author Arcomit
 * @updateDate 2023/7/27
 */
public class TacKeyBingding extends KeyBinding {
    public static final ArrayList<TacKeyBingding> TAC_KEYBINDS = new ArrayList<>();
    public boolean isTriggerOtherKey;
    public TacKeyBingding(String description, int keyCode, String category, boolean isTriggerOtherKey) {
        super(description, keyCode, category);
        TAC_KEYBINDS.add(this);
        this.isTriggerOtherKey = isTriggerOtherKey;
    }
}
