package com.tac.guns.gunskins;

public enum ModelComponent {
    BODY("main"),
    //sundry
    MOUNT("mount"),
    BOLT("bolt"),
    BOLT_EXTRA("bolt_extra"),
    CARRY("carry"),
    SLIDE("slide"),
    SIGHT("sight"),
    SIGHT_FOLDED("sight_folded"),
    BULLET("bullet"),
    BULLET_SHELL("bullet_shell"),
    HANDLE("handle"),
    //stock
    STOCK_DEFAULT("default_stock"),
    STOCK_ANY("default_any"),
    STOCK_LIGHT("light_stock"),
    STOCK_TACTICAL("tactical_stock"),
    STOCK_HEAVY("heavy_stock"),
    //muzzle
    MUZZLE_DEFAULT("default_muzzle"),
    MUZZLE_BRAKE("brake"),
    MUZZLE_COMPENSATOR("compensator"),
    MUZZLE_SILENCER("silencer"),
    //mag
    MAG_STANDARD("standard_mag"),
    MAG_EXTENDED("extended_mag"),
    MAG_DRUM("drum_mag"),
    //side rail
    LASER_BASIC("basic_laser"),
    LASER_BASIC_DEVICE("basic_laser_device"),
    LASER_IR("ir_laser"),
    LASER_IR_DEVICE("ir_laser_device"),
    //grip
    GRIP_LIGHT("light_grip"),
    GRIP_TACTICAL("tactical_grip")
    ;
    public final String key;
    ModelComponent(String key){
        this.key = key;
    }
}
