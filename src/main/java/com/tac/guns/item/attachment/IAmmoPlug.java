package com.tac.guns.item.attachment;

import com.tac.guns.item.attachment.impl.AmmoPlug;

public interface IAmmoPlug extends IAttachment<AmmoPlug> {
    /**
     * @return The type of this attachment
     */
    @Override
    default Type getType()
    {
        return Type.AMMO;
    }
}