package com.tac.guns.client;

import com.tac.guns.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Author: Forked from MrCrayfish, continued by Timeless devs
 */
@Mod.EventBusSubscriber(modid = Reference.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public enum SpecialModels
{
    BULLET_SHELL("bullet_shell"), // Simply for testing fall back, FOR REMOVAL
    BULLET_SHELL_HIGH_CAL("shell_huge"),
    BULLET_SHELL_RIFLE("shell_large"),
    BULLET_SHELL_SHOTGUN("shell_shotgun"),
    BULLET_SHELL_PISTOL("shell_small"),
    BULLET_SHELL_PISTOL_SILVER("shell_silver"),
    BULLET_SHELL_RIFLE_SURPLUS("shell_steel"),


    FLAME("flame"),
    MINI_GUN_BASE("mini_gun_base"),
    MINI_GUN_BARRELS("mini_gun_barrels"),
    GRENADE_LAUNCHER_BASE("grenade_launcher_base"),
    GRENADE_LAUNCHER_CYLINDER("grenade_launcher_cylinder"),
    M1911("m1911"),
    M1911_SLIDE("m1911_slide"),
    M1911_STANDARD_MAG("m1911_standard_mag"),
    M1911_LONG_MAG("m1911_long_mag"),
    M1851("m1851"),
    M1851_CYLINDER("m1851_cylinder"),
    M1851_HAMMER("m1851_hammer"),
    M1928("m1928"),
    M1928_BOLT("m1928_bolt"),
    M1928_STICK_MAG("m1928_stick_mag"),
    M1928_DRUM_MAG("m1928_drum_mag"),
    MOSIN("mosin"),
    MOSIN_BOLT("mosin_bolt"),
    AK47("ak47"),
    AK47_BOLT("ak47_bolt"),
    M60("m60"),
    M60_MAG("m60_mag"),
    M60_STANDARD_MAG("m60_standard_mag"),
    M60_EXTENDED_MAG("m60_extended_mag"),
    M60_CAPS("m60_caps"),
    M60_HANDLE("m60_handle"),
    AK47_OPTIC_MOUNT("ak47_mount"),
    M1917("m1917"),
    M1917_CYLINDER("m1917_cylinder"),
    GLOCK_17("glock_17"),
    GLOCK_17_BULLET("glock_17_bullet"),
    GLOCK_17_SLIDE("glock_17_slide"),
    GLOCK_17_SIGHT("glock_17_sight"),
    GLOCK_17_STANDARD_MAG("glock_17_standard_mag"),
    GLOCK_17_EXTENDED_MAG("glock_17_extended_mag"),
    GLOCK_17_SUPPRESSOR("glock_17_suppressor"),
    DP_28("dp28"),
    DP_28_MAG("dp28_mag"),
    DP_28_BOLT("dp28_bolt"),
    DB_SHORT_FRONT("db_short_front"),
    DB_SHORT_REAR("db_short_rear"),
    DB_SHORT_LEVER("db_short_lever"),
    DB_SHORT_BULLET1("db_short_bullet1"),
    DB_SHORT_BULLET2("db_short_bullet2"),
    M16_A1("m16a1"),
    M16_A1_HANDLE("m16a1_handle"),
    M16_A1_STANDARD_MAG("m16a1_standard_mag"),
    M16_A1_EXTENDED_MAG("m16a1_extended_mag"),
    M16_A1_FRONT_SIGHT("m16a1_front_sight"),
    STI2011("sti2011"),
    STI2011_BULLET1("sti2011_bullet1"),
    STI2011_BULLET2("sti2011_bullet2"),
    STI2011_HAMMER("sti2011_hammer"),
    STI2011_BODY("sti2011_body"),
    STI2011_SIGHT_LIGHT("sti2011_sight_light"),
    STI2011_SLIDE("sti2011_slide"),
    STI2011_STANDARD_MAG("sti2011_standard_mag"),
    STI2011_RAIL("sti2011_rail"),
    AK74("ak74"),
    M92FS("m92fs"),
    M92FS_SLIDE("m92fs_slide"),
    M92FS_SIGHT_LIGHT("m92fs_sight_light"),

    M92FS_BULLET("m92fs_bullet"),
    M92FS_STANDARD_MAG("m92fs_standard_mag"),
    M92FS_EXTENDED_MAG("m92fs_extended_mag"),
    AR15_HELLMOUTH_BODY("ar_15_hellmouth_body"),
    AR15_HELLMOUTH_BUTT_HEAVY("ar_15_hellmouth_butt_heavy"),
    AR15_HELLMOUTH_BUTT_LIGHTWEIGHT("ar_15_hellmouth_butt_lightweight"),
    AR15_HELLMOUTH_SUPPRESSOR("ar_15_hellmouth_suppressor"),
    AR15_HELLMOUTH_MUZZLE("ar_15_hellmouth_muzzle"),
    AR15_HELLMOUTH_BUTT_TACTICAL("ar_15_hellmouth_butt_tactical"),
    AR15_HELLMOUTH_TACTICAL_GRIP("ar_15_hellmouth_tactical_grip"),
    AR15_HELLMOUTH_LIGHTWEIGHT_GRIP("ar_15_hellmouth_lightweight_grip"),
    AR15_P_BODY("ar_15_p_body"),
    AR15_P_IRONS("ar_15_p_irons"),
    AR15_P_MUZZLE("ar_15_p_muzzle"),
    AR15_P_SUPPRESSOR("ar_15_p_suppressor"),
    AR15_P_TACTICAL_GRIP("ar_15_p_tactical_grip"),
    AR15_BOLT("ar_15_bolt"),
    VECTOR45_BODY("vector45_body"),
    VECTOR45_BODY_LIGHT("vector45_body_light"),
    VECTOR45_BOLT("vector45_bolt"),
    VECTOR45_HANDLE("vector45_handle"),
    VECTOR45_EXTENDED_MAG("vector45_extended_mag"),
    VECTOR45_GRIP("vector45_grip"),
    VECTOR45_HEAVY_STOCK("vector45_heavy_stock"),
    VECTOR45_LIGHT_STOCK("vector45_light_stock"),
    VECTOR45_SIGHT("vector45_sight"),
    VECTOR45_SIGHT_LIGHT("vector45_sight_light"),
    VECTOR45_SILENCER("vector45_silencer"),
    VECTOR45_STANDARD_MAG("vector45_standard_mag"),
    VECTOR45_TACTICAL_STOCK("vector45_tactical_stock"),

    VECTOR45_B_LASER_DEVICE("vector45_b_laser_device"),
    VECTOR45_IR_LASER_DEVICE("vector45_ir_laser_device"),

    VECTOR45_B_LASER("vector45_b_laser"),
    VECTOR45_IR_LASER("vector45_ir_laser"),

    MICRO_UZI_BODY("mac10"),
    MICRO_UZI_BULLET("mac10_bullet"),
    MICRO_UZI_BOLT("mac10_bolt"),
    MICRO_UZI_STANDARD_MAG("mac10_standard_mag"),
    MICRO_UZI_EXTENDED_MAG("mac10_extended_mag"),
    MICRO_UZI_STANDARD_STOCK("mac10_standard_stock"),
    MICRO_UZI_UNFOLDED_STOCK("mac10_unfolded_stock"),
    MICRO_UZI_FOLDED_STOCK("mac10_folded_stock"),
    M1911_NETHER("m1911_nether"),
    M1911_SLIDE_NETHER("m1911_slide_nether"),
    M1911_STANDARD_MAG_NETHER("m1911_standard_mag_nether"),
    M1911_LONG_MAG_NETHER("m1911_long_mag_nether"),
    MOSBERG590_BODY("mosberg590_body"),
    MOSBERG590_BULLETS("mosberg590_bullets"),
    MOSBERG590_GRIP("mosberg590_grip"),
    MOSBERG590_SIGHTS("mosberg590_sights"),
    MOSBERG590_SLIDE("mosberg590_slide"),
    WALTHER_PPK_BODY("walther_ppk_body"),
    WALTHER_PPK_EXTENDED_MAG("walther_ppk_extended_mag"),
    WALTHER_PPK_SLIDE("walther_ppk_slide"),
    WALTHER_PPK_STANDARD_MAG("walther_ppk_standard_mag"),
    M4_BODY("m4_body"),
    M4_BOLT("m4_bolt"),
    M4_BOLT_HANDLE("m4_bolt_handle"),
    M4_SIGHT("m4_sight"),
    M4_SIGHT_LIGHT("m4_sight_light"),
    M4_TACTICAL_STOCK("m4_tactical_stock"),
    M24_BODY("m24_body"),
    M24_EXTENDED_MAG("m24_extended_mag"),
    M24_BULLET_SHELL("m24_bullet_shell"),
    M24_BULLET_HEAD("m24_bullet_head"),
    M24_MAG("m24_mag"),
    M24_BOLT("m24_bolt"),
    M24_IRON_SIGHT("m24_iron_sight"),
    M24_SIGHT_LIGHT("m24_sight_light"),
    COYOTE_SIGHT("coyote_sight"),
    MIDRANGE_DOT_SCOPE("midrange_dot_scope"),
    PPSH_41("ppsh_41"),
    PPSH_41_BOLT("ppsh_41_bolt"),
    PPSH_41_STANDARD_MAG("ppsh_41_standard_mag"),
    PPSH_41_EXTENDED_MAG("ppsh_41_extended_mag"),
    QBZ_95_BODY("type_95_longbow"),
    QBZ_95_BODY_LIGHT("type_95_longbow_light"),
    QBZ_95_BOLT("type_95_longbow_bolt"),
    QBZ_95_SUPPRESSOR("type_95_longbow_s_muzzle"),
    QBZ_95_BRAKE("type_95_longbow_b_muzzle"),
    QBZ_95_COMPENSATOR("type_95_longbow_c_muzzle"),
    QBZ_95_DEFAULT_MUZZLE("type_95_longbow_d_muzzle"),
    QBZ_95_DRUM_MAG("type_95_longbow_drum_mag"),
    QBZ_95_MAG("type_95_longbow_standard_mag"),
    SPRINGFIELD_1903("springfield_1903"),
    SPRINGFIELD_1903_BOLT("springfield_1903_bolt"),
    SPRINGFIELD_1903_MOUNT("springfield_1903_mount"),
    DEAGLE_50("deagle_50_body"),
    DEAGLE_50_EXTENDED_MAG("deagle_50_extended_mag"),
    DEAGLE_50_SLIDE("deagle_50_slide"),
    DEAGLE_50_SLIDE_LIGHT("deagle_50_slide_light"),
    DEAGLE_50_STANDARD_MAG("deagle_50_standard_mag"),
    DEAGLE_50_SIGHT_LIGHT("deagle_50_sight_light"),
    AA_12_BODY("aa_12_body"),
    AA_12_BOLT("aa_12_bolt"),
    AA_12_BOLT_HANDLE("aa_12_bolt_handle"),
    AA_12_DRUM_MAG("aa_12_drum_mag"),
    AA_12_GRIP("aa_12_forgrip"),
    AA_12_LIGHT_GRIP("aa_12_light_grip"),
    AA_12_SIGHT("aa_12_sight"),
    AA_12_SIGHT_LIGHT("aa_12_sight_light"),
    AA_12_SILENCER("aa_12_suppressor"),
    AA_12_BRAKE("aa_12_brake"),
    AA_12_COMPENSATOR("aa_12_compensator"),
    AA_12_STANDARD_MAG("aa_12_standard_mag"),
    AA_12_MUZZLE("aa_12_muzzle"),
    AA_12_FRONT_RAIL("aa_12_front_rail"),

    AA_12_B_LASER_DEVICE("aa_12_b_laser_device"),
    AA_12_B_LASER("aa_12_b_laser"),

    X95R("x95r"),
    X95R_BOLT("x95r_bolt"),
    X95R_SIGHT("x95r_sight"),
    X95R_STANDARD_MAG("x95r_standard_mag"),
    FR_F2("fr_f2"),
    FR_F2_BOLT("fr_f2_bolt"),
    SMLE_III("smle_iii"),
    SMLE_III_BOLT("smle_iii_bolt"),
    SMLE_III_BOLT_EXTRA("smle_iii_bolt_extra"),
    SMLE_III_MOUNT("smle_iii_mount"),
    M870_CLASSIC_BODY("m870_body"),
    M870_CLASSIC_PUMP("m870_pump"),
    M870_CLASSIC_BULLET("m870_bullet"),
    M870_CLASSIC_EXTENDED_MAG("m870_extended_mag"),
    M60_UNFOLDED_SIGHT("m60_unfolded_sight"),
    M60_FOLDED_SIGHT("m60_folded_sight"),
    M60_BOLT("m60_bolt"),
    M60_BULLET_CHAIN("m60_bullet_chain"),
    AK47_BUTT_HEAVY("ak47_heavy_stock"),
    AK47_BUTT_LIGHTWEIGHT("ak47_light_stock"),
    AK47_BUTT_TACTICAL("ak47_tactical_stock"),
    AK47_SILENCER("ak47_silencer"),
    AK47_MAGAZINE("ak47_magazine"),
    AK74_BUTT_HEAVY("ak74_heavy_stock"),
    AK74_BUTT_TACTICAL("ak74_tactical_stock"),
    AR_15_CQB_IRONS("ar_15_cqb_irons"),
    AR_15_CQB_IRONS_2("ar_15_cqb_irons_2"),
    AR_15_CQB_DEFAULT_BARREL("ar_15_cqb_default_barrel"),
    AR_15_CQB_BRAKE("ar_15_cqb_brake"),
    AR_15_CQB_COMPENSATOR("ar_15_cqb_compensator"),
    AR_15_CQB_STANDARD_FLASHLIGHT("ar_15_standard_flashlight"),
    AK47_BRAKE("ak47_brake"),
    AK47_COMPENSATOR("ak47_compensator"),
    AR_10_BODY("ar_10_body"),
    AR_10_DEFAULT_BARREL("ar_10_default_muzzle"),
    AR_10_BRAKE("ar_10_brake"),
    AR_10_COMPENSATOR("ar_10_compensator"),
    AR_10_BOLT("ar_10_bolt"),
    AR_10_SUPPRESSOR("ar_10_suppressor"),
    AR_10_EXTENDED_MAG("ar_10_extended_mag"),
    AR_10_HEAVY_STOCK("ar_10_heavy_stock"),
    AR_10_STANDARD_MAG("ar_10_standard_mag"),
    AR_10_TACTICAL_GRIP("ar_10_tactical_grip"),
    M4_COMPENSATOR("m4a1_compensator"),
    M4_DEFAULT_HANDGUARD("m4a1_default_handguard"),
    M4_DEFAULT_BARREL("m4a1_default_muzzle"),
    M4_EXTENDED_HANDGUARD("m4a1_extended_handguard"),
    M4_EXTENDED_MAG("m4a1_extended_mag"),
    M4_SUPPRESSOR("m4a1_silencer"),
    M4_STANDARD_MAG("m4a1_standard_mag"),
    M4_CARRY("m4a1_carry"),
    M4_BRAKE("m4a1_brake"),
    M4_H_STOCK("m4_heavy_stock"),
    M4_L_GRIP("m4_l_grip"),
    M4_T_GRIP("m4_t_grip"),
    M4_LIGHT_STOCK("m4_light_stock"),
    M1A1_SMG_STANDARD_MAG("m1a1_smg_standard_mag"),
    M1A1_SMG_DRUM_MAG("m1a1_smg_drum_mag"),
    M1A1_SMG_BULLET("m1a1_smg_bullet"),
    M1A1_SMG_BOLT("m1a1_smg_bolt"),
    M1A1_SMG_BODY("m1a1_smg_body"),
    SPAS_12_BODY("spas_12"),
    SPAS_12_BOLT("spas_12_bolt"),
    SPAS_12_PUMP("spas_12_pump"),
    SPAS_12_STOCK("spas_12_stock"),
    DEAGLE_50_SILENCER("deagle_50_silencer"),
    DEAGLE_50_BRAKE("deagle_50_brake"),
    DEAGLE_50_COMPENSATOR("deagle_50_compensator"),
    HK_MP5A5_BODY("hk_mp5a5"),
    HK_MP5A5_SIGHT_LIGHT("hk_mp5a5_sight_light"),
    HK_MP5A5_BRAKE("hk_mp5a5_brake"),
    HK_MP5A5_COMPENSATOR("hk_mp5a5_compensator"),
    HK_MP5A5_BOLT("hk_mp5a5_bolt"),
    HK_MP5A5_SUPPRESSOR("hk_mp5a5_silencer"),
    HK_MP5A5_EXTENDED_MAG("hk_mp5a5_extended_mag"),
    HK_MP5A5_HEAVY_STOCK("hk_mp5a5_heavy_stock"),
    HK_MP5A5_STANDARD_MAG("hk_mp5a5_standard_mag"),
    HK_MP5A5_TACTICAL_GRIP("hk_mp5a5_tactical_grip"),
    HK_MP5A5_LIGHT_GRIP("hk_mp5a5_light_grip"),
    HK_MP5A5_LIGHT_STOCK("hk_mp5a5_light_stock"),
    HK_MP5A5_TACTICAL_STOCK("hk_mp5a5_tactical_stock"),
    HK_MP5A5_BOLT_HANDLE("hk_mp5a5_bolt_handle"),
    HK_MP5A5_STANDARD_HANDGUARD("hk_mp5a5_standard_handguard"),
    HK_MP5A5_EXTENDED_HANDGUARD("hk_mp5a5_extended_handguard"),
    HK_MP5A5_DEFAULT_STOCK("hk_mp5a5_default_stock"),
    HK_MP5A5_OPTICS_RAIL("hk_mp5a5_rail"),

    HK_MP5A5_B_LASER_DEVICE("hk_mp5a5_b_laser_device"),
    HK_MP5A5_B_LASER("hk_mp5a5_b_laser"),


    STEN_MK_II_BODY("sten_mk_ii"),
    STEN_MK_II_BOLT("sten_mk_ii_bolt"),
    STEN_MK_II_BOLT_SPRING("sten_mk_ii_bolt_spring"),
    STEN_MK_II_STANDARD_MAG("sten_mk_ii_standard_mag"),
    STEN_MK_II_STANDARD_MAG_EMPTY("sten_mk_ii_standard_mag_empty"),
    STEN_MK_II_EXTENDED_MAG("sten_mk_ii_extended_mag"),
    STEN_MK_II_EXTENDED_MAG_EMPTY("sten_mk_ii_extended_mag_empty"),
    GLOCK_18("glock_18"),
    GLOCK_18_SLIDE("glock_18_slide"),
    GLOCK_18_SIGHT("glock_18_sight"),
    GLOCK_18_STANDARD_MAG("glock_18_standard_mag"),
    GLOCK_18_EXTENDED_MAG("glock_18_extended_mag"),
    GLOCK_18_SUPPRESSOR("glock_18_suppressor"),
    GLOCK_18_BULLET("glock_18_bullet"),
    M1873("m1873"),
    M1873_CYLINDER("m1873_cylinder"),
    CZ75("cz75"),
    CZ75_SLIDE("cz75_slide"),
    CZ75_STANDARD_MAG("cz75_standard_mag"),
    CZ75_EXTENDED_MAG("cz75_extended_mag"),
    CZ75_BRAKE("cz75_brake"),
    CZ75_COMPENSATOR("cz75_compensator"),
    CZ75_SUPPRESSOR("cz75_silencer"),
    CZ75_RAIL("cz75_rail"),
    CZ75_AUTO("cz75_auto"),
    CZ75_AUTO_SLIDE("cz75_auto_slide"),
    M1873_HAMMER("m1873_hammer"),
    VZ61("vz61"),
    VZ61_BOLT("vz61_bolt"),
    VZ61_STANDARD_MAG("vz61_standard_mag"),
    VZ61_EXTENDED_MAG("vz61_extended_mag"),
    QSZ92G1("qsz92g1"),
    QSZ92G1_SLIDE("qsz92g1_slide"),

    HK416_A5_BODY("hk416_a5"),
    HK416_A5_BULLET("hk416_a5_bullet"),
    HK416_A5_BRAKE("hk416_a5_brake"),
    HK416_A5_COMPENSATOR("hk416_a5_compensator"),
    HK416_A5_BOLT("hk416_a5_bolt"),
    HK416_A5_SUPPRESSOR("hk416_a5_silencer"),
    HK416_A5_EXTENDED_MAG("hk416_a5_extended_mag"),
    HK416_A5_HEAVY_STOCK("hk416_a5_heavy_stock"),
    HK416_A5_STANDARD_MAG("hk416_a5_standard_mag"),
    HK416_A5_EXTRA_MAG("hk416_a5_extra_mag"),
    HK416_A5_EXTRA_EXTENDED_MAG("hk416_a5_extra_extended_mag"),
    HK416_A5_TACTICAL_GRIP("hk416_a5_tactical_grip"),
    HK416_A5_LIGHT_GRIP("hk416_a5_light_grip"),
    HK416_A5_LIGHT_STOCK("hk416_a5_light_stock"),
    HK416_A5_TACTICAL_STOCK("hk416_a5_tactical_stock"),
    HK416_A5_FOLDED("hk416_a5_folded_sights"),
    HK416_A5_UNFOLDED("hk416_a5_unfolded_sights"),
    HK416_A5_DEFAULT_MUZZLE("hk416_a5_default_muzzle"),

    HK416_A5_B_LASER_DEVICE("hk416_a5_b_laser_device"),
    HK416_A5_B_LASER("hk416_a5_b_laser"),
    HK416_A5_IR_LASER_DEVICE("hk416_a5_ir_laser_device"),
    HK416_A5_IR_LASER("hk416_a5_ir_laser"),

    STI2011_SUPPRESSOR("sti2011_suppressor"),
    STI2011_EXTENDED_MAG("sti2011_extended_mag"),
    M92FS_SUPPRESSOR("m92fs_suppressor"),
    M1911_SUPPRESSOR("m1911_suppressor"),
    TYPE81_X("type81_x"),
    TYPE81_X_BOLT("type81_x_bolt"),
    TYPE81_X_MOUNT("type81_x_mount"),
    TYPE81_X_MAG("type81_x_mag"),
    TYPE81_X_EXT_MAG("type81_x_extended_mag"),
    M16_A1_BOLT("m16a1_bolt"),
    M16_A1_DEFAULT_MUZZLE("m16a1_default_muzzle"),
    M16_A1_BRAKE("m16a1_brake"),
    M16_A1_COMPENSATOR("m16a1_compensator"),
    M16_A1_SUPPRESSOR("m16a1_silencer"),
    X95R_DEFAULT_MUZZLE("x95r_default_muzzle"),
    X95R_BRAKE("x95r_brake"),
    X95R_COMPENSATOR("x95r_compensator"),
    X95R_SUPPRESSOR("x95r_silencer"),
    X95R_TACTICAL_GRIP("x95r_tactical_grip"),
    X95R_LIGHT_GRIP("x95r_light_grip"),
    X95R_EXTENDED_MAG("x95r_extended_mag"),
    X95R_SIGHT_FOLDED("x95r_sight_fold"),
    PKP_PENCHENNBERG("pkp_penchenberg"),
    PKP_PENCHENNBERG_BOLT("pkp_penchenberg_bolt"),
    PKP_PENCHENNBERG_TACTICAL_GRIP("pkp_penchenberg_tactical_grip"),
    PKP_PENCHENNBERG_LIGHT_GRIP("pkp_penchenberg_light_grip"),
    PKP_PENCHENNBERG_MOUNT("pkp_penchenberg_mount"),
    MP7("mp7"),
    MP7_SIGHT("mp7_sight"),
    MP7_SIGHT_LIGHT("mp7_sight_light"),
    MP7_BRAKE("mp7_brake"),
    MP7_COMPENSATOR("mp7_compensator"),
    MP7_SUPPRESSOR("mp7_silencer"),
    MP7_BOLT("mp7_bolt"),
    MP7_EXTENDED_MAG("mp7_extended_mag"),
    MP7_STANDARD_MAG("mp7_standard_mag"),
    MP7_DEFAULT_MUZZLE("mp7_default_muzzle"),
    M95_BODY("m95_body"),
    M95_BARREL("m95_barrel"),
    M95_BOLT("m95_bolt"),
    M95_BULLET("m95_bullet"),
    M95_EXTENDED_MAG("m95_extended_mag"),
    M95_STANDARD_MAG("m95_standard_mag"),
    M95_SIGHT("m95_sight"),
    M95_SIGHT_FOLDED("m95_sight_folded"),
    M82A2("m82a2"),
    M82A2_BODY("m82a2_body"),
    M82A2_HANDLE("m82a2_handle"),
    M82A2_SIGHT("m82a2_sight"),
    M82A2_SIGHT_LIGHT("m82a2_sight_light"),
    M82A2_SIGHT_FOLDED("m82a2_sight_fold"),
    M82A2_BOLT("m82a2_bolt"),
    M82A2_BULLET("m82a2_bullet"),
    M82A2_BARREL("m82a2_barrel"),
    M82A2_STANDARD_MAG("m82a2_standard_mag"),
    M82A2_EXTENDED_MAG("m82a2_extended_mag"),
    AI_AWP_BRAKE("ai_awp_brake"),
    AI_AWP_COMPENSATOR("ai_awp_compensator"),
    AI_AWP_SUPPRESSOR("ai_awp_silencer"),
    AI_AWP("ai_awp"),
    AI_AWP_SIGHT("ai_awp_sight"),
    AI_AWP_SIGHT_LIGHT("ai_awp_sight_light"),
    AI_AWP_BOLT("ai_awp_bolt"),
    AI_AWP_BOLT_EXTRA("ai_awp_bolt_extra"),
    AI_AWP_BULLET_SHELL("ai_awp_bullet_shell"),
    AI_AWP_MAG("ai_awp_mag"),
    AI_AWP_MAG_EXTENDED("ai_awp_extended_mag"),
    TEC_9("tec_9"),
    TEC_9_BOLT("tec_9_bolt"),
    TEC_9_STANDARD_MAG("tec_9_standard_mag"),
    TEC_9_EXTENDED_MAG("tec_9_extended_mag"),
    RPK("rpk"),
    RPK_BOLT("rpk_bolt"),
    RPK_STANDARD_MAG("rpk_standard_mag"),
    RPK_EXTENDED_MAG("rpk_extended_mag"),
    RPK_BUTT_HEAVY("rpk_heavy_stock"),
    RPK_BUTT_LIGHTWEIGHT("rpk_light_stock"),
    RPK_BUTT_TACTICAL("rpk_tactical_stock"),
    RPK_MOUNT("rpk_mount"),
    RPG7("rpg7"),
    RPG7_ROCKET("rpg7_rocket"),
    FN_FAL("fn_fal"),
    FN_FAL_BOLT("fn_fal_bolt"),
    FN_FAL_SIGHT_LIGHT("fn_fal_sight_light"),
    FN_FAL_STANDARD_MAG("fn_fal_standard_mag"),
    FN_FAL_EXTENDED_MAG("fn_fal_extended_mag"),
    FN_FAL_MOUNT("fn_fal_mount"),
    FN_FAL_STANDARD_HANDGUARD("fn_fal_standard_handguard"),
    FN_FAL_EXTENDED_HANDGUARD("fn_fal_extended_handguard"),
    FN_FAL_STANDARD_BARREL("fn_fal_standard_barrel"),
    FN_FAL_EXTENDED_BARREL("fn_fal_extended_barrel"),
    FN_FAL_TAC_GRIP("fn_fal_tactical_grip"),
    FN_FAL_HANDLE("fn_fal_handle"),
    FN_FAL_LIGHTWEIGHT_GRIP("fn_fal_lightweight_grip"),
    AR15_HELLMOUTH_STANDARD_MAG("ar_15_hellmouth_standard_mag"),
    AR15_HELLMOUTH_EXTENDED_MAG("ar_15_hellmouth_extended_mag"),
    AR15_HELLMOUTH_DD_MAG("ar_15_hellmouth_dd_mag"),
    M4_EXTENDED_HANDGUARD_V2("m4a1_extended_handguard_v2"),
    M4_EXTENDED_HANDGUARD_V2_L_COVER("m4a1_extended_handguard_v2_left_cover"),
    M4_EXTENDED_HANDGUARD_V2_L_RAIL("m4a1_extended_handguard_v2_left_rail"),
    M4_EXTENDED_HANDGUARD_V2_T_COVER("m4a1_extended_handguard_v2_top_cover"),
    M4_EXTENDED_HANDGUARD_V2_T_RAIL("m4a1_extended_handguard_v2_top_rail"),

    M4_B_LASER_DEVICE("m4a1_b_laser_device"),
    M4_IR_LASER_DEVICE("m4a1_ir_laser_device"),

    M4_B_LASER("m4a1_b_laser"),
    M4_IR_LASER("m4a1_ir_laser"),
    VECTOR45_BRAKE("vector45_brake"),
    VECTOR45_COMP("vector45_compensator"),
    VECTOR45_LGRIP("vector45_light_grip"),
    DE_LISLE_STANDARD_MAG("de_lisle_standard_mag"),
    DE_LISLE_EXTENDED_MAG("de_lisle_extended_mag"),
    DE_LISLE("de_lisle"),
    DE_LISLE_BOLT("de_lisle_bolt"),
    DE_LISLE_BOLT_EXTRA("de_lisle_bolt_extra"),
    M1_GARAND("m1_garand"),
    M1_GARAND_BOLT("m1_garand_bolt"),
    M1_GARAND_BOLT_HANDLE("m1_garand_handle"),
    M1_GARAND_CLIP_FULL("m1_garand_clip_full"),
    M1_GARAND_MOUNT("m1_garand_mount"),
    SIG_MCX_SPEAR("sig_mcx_spear"),
    SIG_MCX_SPEAR_BOLT("sig_mcx_spear_bolt"),
    SIG_MCX_SPEAR_BULLET("sig_mcx_spear_bullet"),
    SIG_MCX_SPEAR_SIGHT("sig_mcx_spear_sight"),
    SIG_MCX_SPEAR_SIGHT_LIGHT("sig_mcx_spear_sight_light"),
    SIG_MCX_SPEAR_HANDLE1("sig_mcx_spear_handle1"),
    SIG_MCX_SPEAR_HANDLE2("sig_mcx_spear_handle2"),
    SIG_MCX_SPEAR_MAG("sig_mcx_spear_mag"),
    SIG_MCX_SPEAR_LIGHT_GRIP("sig_mcx_spear_light_grip"),
    SIG_MCX_SPEAR_TACTICAL_GRIP("sig_mcx_spear_tactical_grip"),

    SIG_MCX_SPEAR_B_LASER_DEVICE("sig_mcx_spear_b_laser_device"),
    SIG_MCX_SPEAR_IR_LASER_DEVICE("sig_mcx_spear_ir_laser_device"),

    SIG_MCX_SPEAR_B_LASER("sig_mcx_spear_b_laser"),
    SIG_MCX_SPEAR_IR_LASER("sig_mcx_spear_ir_laser"),

    MP9("mp9"),
    MP9_BOLT("mp9_bolt"),
    MP9_BULLET("mp9_bullet"),
    MP9_HANDLE("mp9_handle"),

    MP9_STOCK_FOLDED("mp9_stock_folded"),
    MP9_STOCK_EXTENDED("mp9_stock_ext"),

    MP9_STANDARD_MAG("mp9_standard_mag"),
    MP9_EXTENDED_MAG("mp9_extended_mag"),
    MP9_B_LASER("mp9_b_laser"),
    MP9_B_LASER_DEVICE("mp9_b_laser_device"),
    SKS("sks"),
    SKS_BOLT("sks_bolt"),
    SKS_STANDARD_MAG("sks_standard_mag"),
    SKS_EXTENDED_MAG("sks_extended_mag"),

    SKS_TACTICAL("sks_tactical"),
    SKS_TACTICAL_BOLT("sks_tactical_bolt"),
    SKS_TACTICAL_HEAVY_STOCK("sks_tactical_heavy_stock"),
    SKS_TACTICAL_EXTENDED_MAG("sks_tactical_extended_mag"),
    SKS_TACTICAL_STANDARD_MAG("sks_tactical_standard_mag"),
    SKS_TACTICAL_LIGHT_GRIP("sks_tactical_light_grip"),
    SKS_TACTICAL_LIGHT_STOCK("sks_tactical_light_stock"),
    SKS_TACTICAL_RSIGHT("sks_tactical_rsight"),
    SKS_TACTICAL_SCOPE_RAIL("sks_tactical_srail"),
    SKS_TACTICAL_TACTICAL_GRIP("sks_tactical_tactical_grip"),
    SKS_TACTICAL_TACTICAL_STOCK("sks_tactical_tactical_stock"),

    SKS_TACTICAL_B_LASER_DEVICE("sks_tactical_b_laser_device"),
    SKS_TACTICAL_B_LASER("sks_tactical_b_laser"),
    SKS_TACTICAL_IR_LASER_DEVICE("sks_tactical_ir_laser_device"),
    SKS_TACTICAL_IR_LASER("sks_tactical_ir_laser"),

    M1014("m1014"),
    M1014_BULLET("m1014_bullet"),
    M1014_BOLT("m1014_bolt"),
    M1014_SHELL("m1014_shell"),
    M1014_SIGHT_LIGHT("m1014_sight_light"),
    M249("m249"),
    M249_SIGHT_LIGHT("m249_sight_light"),
    M249_ROTATE("m249_rotate"),
    M249_IRON("m249_iron"),
    M249_MAG("m249_mag"),
    M249_CAP("m249_cap"),
    M249_BULLET_CHAIN("m249_bullet_chain"),
    M249_BOLT("m249_bolt"),
    M249_TACTICAL_GRIP("m249_tactical_grip"),
    M249_LIGHT_GRIP("m249_light_grip"),

    MGL_40MM("mgl_40mm"),
    MGL_40MM_CYLINDER("mgl_40mm_mag"),
    MGL_40MM_TACTICAL_GRIP("mgl_40mm_tactical_grip"),
    MGL_40MM_LIGHT_GRIP("mgl_40mm_light_grip"),

    MK23("mk23"),
    MK23_SLIDE("mk23_slide"),
    MK23_SIGHT_LIGHT("mk23_sight_light"),
    MK23_BULLET("mk23_bullet"),
    MK23_SUPPRESSOR("mk23_suppressor"),
    MK23_EXTENDED_MAG("mk23_extended_mag"),
    MK23_STANDARD_MAG("mk23_standard_mag"),
    QBZ_191("qbz_191"),
    QBZ_191_BODY("qbz_191_body"),
    QBZ_191_RELEASE("qbz_191_release"),
    QBZ_191_BULLET1("qbz_191_bullet1"),
    QBZ_191_BULLET2("qbz_191_bullet2"),
    QBZ_191_EXTENDED_MAG("qbz_191_extended_mag"),
    QBZ_191_STANDARD_MAG("qbz_191_standard_mag"),
    QBZ_191_BOLT("qbz_191_bolt"),
    QBZ_191_TAC_GRIP("qbz_191_tac_grip"),
    QBZ_191_LIGHT_GRIP("qbz_191_light_grip"),
    QBZ_191_DEFAULT("qbz_191_muz_default"),
    QBZ_191_BRAKE("qbz_191_muz_brake"),
    QBZ_191_COMP("qbz_191_muz_comp"),
    QBZ_191_SUPPRESSOR("qbz_191_muz_suppressor"),
    QBZ_191F("qbz_191_sightsf"),
    QBZ_191NF("qbz_191_sightsnf"),
    QBZ_191NF_L("qbz_191_sightsnf_light"),
    C96("c96"),
    C96_BOLT("c96_bolt"),
    C96_EXTENDED_MAG("c96_extended_mag"),
    C96_STANDARD_MAG("c96_standard_mag"),
    STEN_MK_II_OSS("sten_mk_ii_oss"),

    KAR98_BODY("kar98_body"),
    KAR98_BOLT_FIXED("kar98_bolt_fixed"),
    KAR98_BOLT_ROTATE("kar98_bolt_rotate"),
    KAR98_BULLET2("kar98_bullet2"),
    KAR98_BULLET3("kar98_bullet3"),
    KAR98_BULLETS("kar98_bullets"),
    KAR98_CLIP("kar98_clip"),
    KAR98_FRONT("kar98_front"),
    KAR98_MOUNT("kar98_mount"),
    KAR98_SHELL("kar98_shell"),

    ESPADON("espadon"),
    ESPADON_SIGHT("espadon_sight"),
    ESPADON_RAIL("espadon_rail"),

    AK47_EXTENDED_MAG("ak47_extended_mag"),
    AK47_STANDARD_MAG("ak47_standard_mag"),

    M16A4_BODY("m16a4"),
    M16A4_SIGHT_LIGHT("m16a4_sight_light"),
    M16A4_BOLT("m16a4_bolt"),
    M16A4_PULL_HANDLE("m16a4_pull_handle"),
    M16A4_COMPENSATOR("m16a4_c_muzzle"),
    M16A4_DEFAULT_HANDGUARD("m16a4_default_guard"),
    M16A4_DEFAULT_BARREL("m16a4_d_muzzle"),
    M16A4_EXTENDED_HANDGUARD("m16a4_tac_guard"),
    M16A4_EXTENDED_MAG("m16a4_extended_mag"),
    M16A4_SUPPRESSOR("m16a4_s_muzzle"),
    M16A4_STANDARD_MAG("m16a4_standard_mag"),
    M16A4_CARRY("m16a4_handle"),
    M16A4_BRAKE("m16a4_b_muzzle"),
    M16A4_TAC_GRIP("m16a4_tac_grip"),
    M16A4_LIGHT_GRIP("m16a4_light_grip"),

    M16A4_B_LASER_DEVICE("m16a4_b_laser_device"),
    M16A4_B_LASER("m16a4_b_laser"),
    M16A4_IR_LASER_DEVICE("m16a4_ir_laser_device"),
    M16A4_IR_LASER("m16a4_ir_laser"),


    SCAR_H_BODY("scar_h"),
    SCAR_H_BOLT("scar_h_bolt"),
    SCAR_H_COMPENSATOR("scar_h_c_muzzle"),
    SCAR_H_DEFAULT_BARREL("scar_h_d_muzzle"),
    SCAR_H_EXTENDED_MAG("scar_h_extended_mag"),
    SCAR_H_SUPPRESSOR("scar_h_s_muzzle"),
    SCAR_H_STANDARD_MAG("scar_h_standard_mag"),
    SCAR_H_FS("scar_h_fs"),
    SCAR_H_FS_L("scar_h_fs_light"),
    SCAR_H_FSU("scar_h_fsu"),
    SCAR_H_FSU_L("scar_h_fsu_light"),
    SCAR_H_BRAKE("scar_h_b_muzzle"),
    SCAR_H_TAC_GRIP("scar_h_tac_grip"),
    SCAR_H_LIGHT_GRIP("scar_h_light_grip"),

    SCAR_L_BODY("scar_l"),
    SCAR_L_BOLT("scar_l_bolt"),
    SCAR_L_COMPENSATOR("scar_l_c_muzzle"),
    SCAR_L_DEFAULT_BARREL("scar_l_d_muzzle"),
    SCAR_L_EXTENDED_MAG("scar_l_extended_mag"),
    SCAR_L_SUPPRESSOR("scar_l_s_muzzle"),
    SCAR_L_STANDARD_MAG("scar_l_standard_mag"),
    SCAR_L_FS("scar_l_fs"),
    SCAR_L_FS_L("scar_l_fs_light"),
    SCAR_L_FSU("scar_l_fsu"),
    SCAR_L_FSU_L("scar_l_fsu_light"),
    SCAR_L_BRAKE("scar_l_b_muzzle"),
    SCAR_L_TAC_GRIP("scar_l_tac_grip"),
    SCAR_L_LIGHT_GRIP("scar_l_light_grip"),
    // Used for testing the laser module rendering system
    SCAR_L_MINI_LASER("scar_l_mini_laser"),
    SCAR_L_MINI_LASER_BEAM("scar_l_mini_laser_beam"),
    SCAR_L_IR_LASER("scar_l_ir_laser"),
    SCAR_L_IR_DEVICE("scar_l_ir_device"),
    MK47_BODY("mk47"),
    MK47_BOLT("mk47_bolt"),
    MK47_COMPENSATOR("mk47_c_muzzle"),
    MK47_DEFAULT_BARREL("mk47_d_muzzle"),
    MK47_EXTENDED_MAG("mk47_extended_mag"),
    MK47_SUPPRESSOR("mk47_s_muzzle"),
    MK47_STANDARD_MAG("mk47_standard_mag"),
    MK47_FS("mk47_sightsf"),
    MK47_SIGHTSF_LIGHT("mk47_sightsf_light"),
    MK47_FSU("mk47_sights"),
    MK47_SIGHTS_LIGHT("mk47_sights_light"),
    MK47_BRAKE("mk47_b_muzzle"),
    MK47_TACTICAL_STOCK("mk47_tac_stock"),
    MK47_LIGHT_STOCK("mk47_light_stock"),
    MK47_HEAVY_STOCK("mk47_heavy_stock"),
    MK47_PULL("mk47_pull"),

   AR_15_BODY("ar_15_body"),
   AR_15_BOLT("ar_15h_bolt"),
   AR_15_COMPENSATOR("ar_15_c_muzzle"),
   AR_15_DEFAULT_BARREL("ar_15_d_muzzle"),
   AR_15_EXTENDED_MAG("ar_15_extended_mag"),
   AR_15_SUPPRESSOR("ar_15_s_muzzle"),
   AR_15_STANDARD_MAG("ar_15_standard_mag"),
   AR_15_FS("ar_15_fs"),
   AR_15_FSU("ar_15_fsu"),
   AR_15_BRAKE("ar_15_b_muzzle"),
   AR_15_TACTICAL_STOCK("ar_15_tac_stock"),
   AR_15_LIGHT_STOCK("ar_15_light_stock"),
   AR_15_HEAVY_STOCK("ar_15_heavy_stock"),
   AR_15_GRIP("ar_15_grip"),
   AR_15_LIGHT_GRIP("ar_15_l_grip"),
   AR_15_TAC_GRIP("ar_15_t_grip"),

    SPR_15_BODY("spr15"),
    SPR_15_PULL_HANDLE("spr15_pull_handle"),
    SPR_15_BOLT("spr15_bolt"),
    SPR_15_COMPENSATOR("spr15_c_muzzle"),
    SPR_15_DEFAULT_BARREL("spr15_d_muzzle"),
    SPR_15_EXTENDED_MAG("spr15_extended_mag"),
    SPR_15_SUPPRESSOR("spr15_s_muzzle"),
    SPR_15_STANDARD_MAG("spr15_mag"),
    SPR_15_SIGHT("spr15_sight"),
    SPR_15_SIGHT_LIGHT("spr15_sight_light"),
    SPR_15_SIGHT_FOLDED("spr15_sight_folded"),
    SPR_15_SIGHT_FOLDED_LIGHT("spr15_sight_folded_light"),
    SPR_15_BRAKE("spr15_b_muzzle"),
    SPR_15_TACTICAL_STOCK("spr15_tac_stock"),
    SPR_15_LIGHT_STOCK("spr15_light_stock"),
    SPR_15_HEAVY_STOCK("spr15_heavy_stock"),
    SPR_15_DEFAULT_GRIP("spr15_default_grip"),
    SPR_15_LIGHT_GRIP("spr15_light_grip"),
    SPR_15_TAC_GRIP("spr15_tac_grip"),

    SPR_15_B_LASER_DEVICE("spr15_b_laser_device"),
    SPR_15_B_LASER("spr15_b_laser"),

    TTI_G34("tti_g34"),
    TTI_G34_SLIDE("tti_g34_slide"),
    TTI_G34_SLIDE_LIGHT("tti_g34_slide_light"),
    TTI_G34_STANDARD_MAG("tti_g34_standard_mag"),
    TTI_G34_EXTENDED_MAG("tti_g34_extended_mag"),
    TTI_G34_SUPPRESSOR("tti_g34_suppressor"),

    UDP_9("udp_9"),
    UDP_9_BOLT("udp_9_bolt"),
    UDP_9_BRAKE("udp_9_brake"),
    UDP_9_BULLET("udp_9_bullet"),
    UDP_9_COMPENSATOR("udp_9_compensator"),
    UDP_9_DEFAULT_MUZZLE("udp_9_default_muzzle"),
    UDP_9_EXTENDED_BARREL("udp_9_extended_barrel"),
    UDP_9_EXTENDED_MAG("udp_9_extended_mag"),
    UDP_9_HANDLE("udp_9_handle"),
    UDP_9_HEAVY_STOCK("udp_9_heavy_stock"),
    UDP_9_LIGHT_GRIP("udp_9_light_grip"),
    UDP_9_LIGHT_STOCK("udp_9_light_stock"),
    UDP_9_SIGHT("udp_9_sight"),
    UDP_9_SIGHT_LIGHT("udp_9_sight_light"),
    UDP_9_SIGHT_FOLDED("udp_9_sight_folded"),
    UDP_9_SIGHT_FOLDED_LIGHT("udp_9_sight_folded_light"),
    UDP_9_STANDARD_MAG("udp_9_standard_mag"),
    UDP_9_SUPPRESSOR("udp_9_suppressor"),
    UDP_9_TACTICAL_GRIP("udp_9_tactical_grip"),
    UDP_9_TACTICAL_STOCK("udp_9_tactical_stock"),
    UDP_9_B_LASER_DEVICE("udp_9_b_laser_device"),
    UDP_9_RAIL_COVER("udp_9_rail_cover"),
    UDP_9_B_LASER("udp_9_b_laser"),

    UZI("uzi"),
    UZI_LIGHT("uzi_light"),
    UZI_BOLT("uzi_bolt"),
    UZI_BULLET("uzi_bullet"),
    UZI_EXTENDED_MAG("uzi_extended_mag"),
    UZI_EXTENDED_STOCK("uzi_extended_stock"),
    UZI_FOLDED_STOCK("uzi_folded_stock"),
    UZI_HANDLE("uzi_handle"),
    UZI_STANDARD_MAG("uzi_standard_mag"),
    UZI_SUPPRESSOR("uzi_suppressor"),

    MK18_MOD1_BODY("mk18_mod1"),
    MK18_MOD1_BOLT("mk18_mod1_bolt"),
    MK18_MOD1_COMPENSATOR("mk18_mod1_c_muzzle"),
    MK18_MOD1_DEFAULT_BARREL("mk18_mod1_d_muzzle"),
    MK18_MOD1_EXTENDED_MAG("mk18_mod1_extended_mag"),
    MK18_MOD1_SUPPRESSOR("mk18_mod1_s_muzzle"),
    MK18_MOD1_STANDARD_MAG("mk18_mod1_standard_mag"),
    MK18_MOD1_SIGHT("mk18_mod1_sight"),
    MK18_MOD1_SIGHT_LIGHT("mk18_mod1_sight_light"),
    MK18_MOD1_SIGHT_FOLDED("mk18_mod1_sight_folded"),
    MK18_MOD1_SIGHT_FOLDED_LIGHT("mk18_mod1_sight_folded_light"),
    MK18_MOD1_BRAKE("mk18_mod1_b_muzzle"),
    MK18_MOD1_B5_STOCK("mk18_mod1_b5_stock"),
    //MK18_MOD1_LIGHT_STOCK("spr15_light_stock"),
    //MK18_MOD1_HEAVY_STOCK("spr15_heavy_stock"),
    //MK18_MOD1_DEFAULT_GRIP("mk18_mod1_default_grip"),
    MK18_MOD1_LIGHT_GRIP("mk18_mod1_light_grip"),
    MK18_MOD1_TAC_GRIP("mk18_mod1_tac_grip"),

    MK18_MOD1_IR_DEVICE("mk18_mod1_ir_device"),
    MK18_MOD1_IR_LASER("mk18_mod1_ir_laser"),

    MK18_MOD1_BASIC_LASER_DEVICE("mk18_mod1_b_laser_device"),
    MK18_MOD1_BASIC_LASER("mk18_mod1_b_laser"),

    MP7_IR_DEVICE("mp7_ir_device"),
    MP7_IR_LASER("mp7_ir_laser"),

    MP7_BASIC_LASER_DEVICE("mp7_b_laser_device"),
    MP7_BASIC_LASER("mp7_b_laser"),

    FN_FAL_IR_DEVICE("fn_fal_ir_device"),
    FN_FAL_IR_LASER("fn_fal_ir_laser"),

    FN_FAL_BASIC_LASER_DEVICE("fn_fal_b_laser_device"),
    FN_FAL_BASIC_LASER("fn_fal_b_laser"),
    STI2011_BASIC_LASER_DEVICE("sti2011_b_laser_device"),
    STI2011_BASIC_LASER("sti2011_b_laser"),

    SCAR_MK20_BODY("scar_mk20"),
    SCAR_MK20_BOLT("scar_mk20_bolt"),
    SCAR_MK20_COMPENSATOR("scar_mk20_c_muzzle"),
    SCAR_MK20_DEFAULT_BARREL("scar_mk20_d_muzzle"),
    SCAR_MK20_EXTENDED_MAG("scar_mk20_extended_mag"),
    SCAR_MK20_SUPPRESSOR("scar_mk20_s_muzzle"),
    SCAR_MK20_STANDARD_MAG("scar_mk20_standard_mag"),
    SCAR_MK20_FS("scar_mk20_sight_folded"),
    SCAR_MK20_FS_L("scar_mk20_sight_folded_light"),
    SCAR_MK20_FSU("scar_mk20_sight"),
    SCAR_MK20_FSU_L("scar_mk20_sight_light"),
    SCAR_MK20_BRAKE("scar_mk20_b_muzzle"),
    SCAR_MK20_TAC_GRIP("scar_mk20_t_grip"),
    SCAR_MK20_LIGHT_GRIP("scar_mk20_l_grip"),
    SCAR_MK20_B_LASER("scar_mk20_b_laser_device"),
    SCAR_MK20_B_LASER_BEAM("scar_mk20_b_laser"),
    SCAR_MK20_IR_LASER("scar_mk20_ir_laser"),
    SCAR_MK20_IR_LASER_DEVICE("scar_mk20_ir_laser_device"),
    GLOCK_17_B_LASER_DEVICE("glock_17_b_laser_device"),
    GLOCK_17_B_LASER("glock_17_b_laser"),

    MRAD_BODY("mrad"),
    MRAD_BOLT("mrad_bolt"),
    MRAD_BOLT_EXTRA("mrad_bolt_extra"),
    MRAD_BARREL("mrad_barrel"),
    MRAD_BIPOD("mrad_bipod"),
    MRAD_EXTENDED_MAG("mrad_extended_mag"),
    MRAD_STANDARD_MAG("mrad_standard_mag"),
    MRAD_SF("mrad_sightf"),
    MRAD_SF_L("mrad_sightf_light"),
    MRAD_S("mrad_sight"),
    MRAD_S_L("mrad_sight_light"),
    MRAD_TACTICAL_GRIP("mrad_tactical_grip"),
    MRAD_LIGHT_GRIP("mrad_light_grip"),
    MRAD_BULLET("mrad_bullet"),
    MRAD_SHELL("mrad_shell"),
    MRAD_IR_DEVICE("mrad_ir_laser_device"),
    MRAD_IR_LASER("mrad_ir_laser"),
    MRAD_B_LASER_DEVICE("mrad_b_laser_device"),
    MRAD_B_LASER("mrad_b_laser"),

    HK_G3_BODY("hk_g3"),
    HK_G3_BOLT("hk_g3_bolt"),
    HK_G3_COMPENSATOR("hk_g3_c_muzzle"),
    HK_G3_DEFAULT_MUZZLE("hk_g3_d_muzzle"),
    HK_G3_DEFAULT_STOCK("hk_g3_d_stock"),
    HK_G3_HEAVY_STOCK("hk_g3_h_stock"),
    HK_G3_TACTICAL_STOCK("hk_g3_t_stock"),
    HK_G3_LIGHT_STOCK("hk_g3_l_stock"),
    HK_G3_EXTENDED_MAG("hk_g3_e_mag"),
    HK_G3_SUPPRESSOR("hk_g3_s_muzzle"),
    HK_G3_SIGHT_LIGHT("hk_g3_sight_light"),
    HK_G3_STANDARD_MAG("hk_g3_s_mag"),
    HK_G3_HANDLE("hk_g3_handle"),
    HK_G3_PULL("hk_g3_pull"),
    HK_G3_RAIL("hk_g3_rail"),
    HK_G3_BULLET("hk_g3_bullet"),
    HK_G3_BRAKE("hk_g3_b_muzzle"),
    HK_G3_TAC_GRIP("hk_g3_t_grip"),
    HK_G3_LIGHT_GRIP("hk_g3_l_grip"),
    HK_G3_DEFAULT_HG("hk_g3_d_hg"),
    HK_G3_TACTICAL_HG("hk_g3_t_hg"),
    HK_G3_B_LASER_DEVICE("hk_g3_b_laser_device"),
    HK_G3_B_LASER("hk_g3_b_laser"),

    M92FS_B_LASER_DEVICE("m92fs_b_laser_device"),
    M92FS_B_LASER("m92fs_b_laser"),
    MK23_B_LASER_DEVICE("mk23_b_laser_device"),
    MK23_B_LASER("mk23_b_laser"),

    MK47_IR_DEVICE("mk47_ir_laser_device"),
    MK47_IR_LASER("mk47_ir_laser"),
    MK47_B_LASER_DEVICE("mk47_b_laser_device"),
    MK47_B_LASER("mk47_b_laser"),

    MK14_BODY1 ("mk14"),
    MK14_SIGHT_LIGHT ("mk14_sight_light"),
    BOLT1("mk14_bolt"),
    STANDARD_MAG1 ("mk14_standard_mag"),
    EXTENDED_MAG1("mk14_extended_mag"),
    T_GRIP1("mk14_tac_grip"),
    L_GRIP1 ("mk14_light_grip"),
    SCOPE_MOUNT1 ("mk14_mount"),
    BOLT_HANDLE1 ("mk14_bolt_handle"),

    MK14_B_LASER_DEVICE("mk14_b_laser_device"),
    MK14_B_LASER("mk14_b_laser"),
    MK14_IR_LASER_DEVICE("mk14_ir_laser_device"),
    MK14_IR_LASER("mk14_ir_laser"),

    TIMELESS_50("timeless_50"),
    TIMELESS_50_S_SLIDE("timeless_50_s_slide"),
    TIMELESS_50_S_SLIDE_LIGHT("timeless_50_s_slide_light"),
    TIMELESS_50_S_BARREL("timeless_50_s_barrel"),
    TIMELESS_50_E_SLIDE("timeless_50_e_slide"),
    TIMELESS_50_E_SLIDE_LIGHT("timeless_50_e_slide_light"),
    TIMELESS_50_E_BARREL("timeless_50_e_barrel"),
    TIMELESS_50_E_MAG("timeless_50_e_mag"),
    TIMELESS_50_S_MAG("timeless_50_s_mag"),
    TIMELESS_50_CLUMSYYY("timeless_50_clumsyyy"),
    TIMELESS_50_NEKOOO("timeless_50_nekooo"),
    TIMELESS_50_HAMMER("timeless_50_hammer"),
    TIMELESS_50_BULLET1("timeless_50_bullet1"),
    TIMELESS_50_BULLET2("timeless_50_bullet2"),
    TIMELESS_50_SUPPRESSOR("timeless_50_suppressor"),


    //Everything from this point on is all scope additions

    MINI_DOT_BASE("optics/mini_dot_base"),
    MICRO_HOLO_BASE("optics/micro_holo_base"),
    LPVO_1_6_FRONT("optics/lpvo_1_6_front"),
    LPVO_1_6("optics/lpvo_1_6"),
    Sx8_FRONT("optics/8x_scope_front"),
    Sx8_BODY("optics/8x_scope"),
    //Everything from this point on is all LOD renders

    M1911_LOD("lods/m1911_lod");

    /**
     * The location of an item model in the [MOD_ID]/models/special/[NAME] folder
     */
    private ResourceLocation modelLocation;

    /**
     * Determines if the model should be loaded as a special model.
     */
    private boolean specialModel;

    /**
     * Cached model
     */
    @OnlyIn(Dist.CLIENT)
    private IBakedModel cachedModel;

    /**
     * Sets the model's location
     *
     * @param modelName name of the model file
     */
    SpecialModels(String modelName)
    {
        this(new ResourceLocation(Reference.MOD_ID, "special/" + modelName), true);
    }

    /**
     * Sets the model's location
     *
     * @param modelName name of the model file
     */
    SpecialModels(SpecialModel modelName)
    {
        this(new ResourceLocation(Reference.MOD_ID, "special/" + modelName), true);
    }

    /**
     * Sets the model's location
     *
     * @param resource name of the model file
     * @param specialModel if the model is a special model
     */
    SpecialModels(ResourceLocation resource, boolean specialModel)
    {
        this.modelLocation = resource;
        this.specialModel = specialModel;
    }

    /**
     * Gets the model
     *
     * @return isolated model
     */
    @OnlyIn(Dist.CLIENT)
    public IBakedModel getModel()
    {
        if(this.cachedModel == null)
        {
            IBakedModel model = Minecraft.getInstance().getModelManager().getModel(this.modelLocation);
            if(model == Minecraft.getInstance().getModelManager().getMissingModel())
            {
                return model;
            }
            this.cachedModel = model;
        }
        return this.cachedModel;
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void register(ModelRegistryEvent event)
    {
        for(SpecialModels model : values())
        {
            if(model.specialModel)
            {
                ModelLoader.addSpecialModel(model.modelLocation);
            }
        }
    }
}
