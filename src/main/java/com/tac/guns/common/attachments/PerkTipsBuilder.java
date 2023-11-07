package com.tac.guns.common.attachments;

import com.tac.guns.common.attachments.perk.BooleanPerk;
import com.tac.guns.common.attachments.perk.DoublePerk;
import com.tac.guns.common.attachments.perk.FloatPerk;
import net.minecraft.util.text.ITextComponent;

import java.util.ArrayList;
import java.util.List;

public class PerkTipsBuilder {
    List<ITextComponent> positivePerks = new ArrayList<>();
    List<ITextComponent> negativePerks = new ArrayList<>();
    CustomModifierData data;

    public PerkTipsBuilder(CustomModifierData data) {
        this.data = data;
    }

    public PerkTipsBuilder add(FloatPerk perk) {
        float value = perk.getValue(data);
        if (value > 0.0F) {
            positivePerks.add(perk.getPositive(data));
        } else if (value < 0.0F) {
            negativePerks.add(perk.getNegative(data));
        }
        return this;
    }

    public PerkTipsBuilder addPercentage(DoublePerk perk) {
        double value = perk.getValue(data);
        if (value < 1.0) {
            positivePerks.add(perk.getPositive(data));
        } else if (value > 1.0) {
            negativePerks.add(perk.getNegative(data));
        }
        return this;
    }

    public PerkTipsBuilder add(BooleanPerk perk) {
        if(perk.getValue(data)){
            positivePerks.add(perk.getText());
        }
        return this;
    }

    public List<ITextComponent> build() {
        positivePerks.addAll(negativePerks);
        return positivePerks;
    }
}
