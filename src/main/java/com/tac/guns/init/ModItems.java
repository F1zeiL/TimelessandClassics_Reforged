package com.tac.guns.init;

import com.tac.guns.GunMod;
import com.tac.guns.Reference;
import com.tac.guns.common.GunModifiers;
import com.tac.guns.item.*;
import com.tac.guns.item.transition.*;
import com.tac.guns.item.transition.grenades.BaseballGrenadeItem;
import com.tac.guns.item.transition.grenades.LightGrenadeItem;
import com.tac.guns.item.transition.grenades.utility.FlashGrenadeItem;
import com.tac.guns.item.transition.grenades.utility.ImpactFlashGrenadeItem;
import com.tac.guns.item.attachment.impl.*;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModItems {
    public static final DeferredRegister<Item> REGISTER = DeferredRegister.create(ForgeRegistries.ITEMS, Reference.MOD_ID);

    /*** Guns ***/
    /* Guns */
    public static final RegistryObject<GunItem> M1911 = REGISTER.register("m1911",
            () -> new TimelessPistolGunItem(properties -> properties.group(GunMod.PISTOL), GunModifiers.M1911_MOD));
    public static final RegistryObject<GunItem> AK47 = REGISTER.register("ak47",
            () -> new TimelessGunItem(properties -> properties.group(GunMod.RIFLE), GunModifiers.AK47_MOD));
    public static final RegistryObject<GunItem> M60 = REGISTER.register("m60",
            () -> new TimelessGunItem(properties -> properties.group(GunMod.HEAVY_MATERIAL), GunModifiers.M60_MOD));
    public static final RegistryObject<Item> GLOCK_17 = REGISTER.register("glock_17",
            () -> new TimelessPistolGunItem(properties -> properties.group(GunMod.PISTOL), GunModifiers.GLOCK17_MOD));
    public static final RegistryObject<Item> DP28 = REGISTER.register("dp28",
            () -> new TimelessGunItem(properties -> properties.group(GunMod.HEAVY_MATERIAL), GunModifiers.DP28_MOD));
    public static final RegistryObject<Item> STI2011 = REGISTER.register("sti2011",
            () -> new TimelessPistolGunItem(properties -> properties.group(GunMod.PISTOL), GunModifiers.STI2011_MOD));
    public static final RegistryObject<Item> M92FS = REGISTER.register("m92fs",
            () -> new TimelessPistolGunItem(properties -> properties.group(GunMod.PISTOL), GunModifiers.PISTOL_MOD));
    public static final RegistryObject<GunItem> VECTOR45 = REGISTER.register("vector45",
            () -> new TimelessGunItem(properties -> properties.group(GunMod.SMG), GunModifiers.VECTOR_MOD));
    public static final RegistryObject<Item> MICRO_UZI = REGISTER.register("micro_uzi",
            () -> new TimelessGunItem(properties -> properties.group(GunMod.SMG), GunModifiers.MICROUZI_MOD));
    public static final RegistryObject<Item> UZI = REGISTER.register("uzi",
            () -> new TimelessPistolGunItem(properties -> properties.group(GunMod.SMG), GunModifiers.UZI_MOD));
    public static final RegistryObject<Item> M4 = REGISTER.register("m4",
            () -> new TimelessGunItem(properties -> properties.group(GunMod.RIFLE), GunModifiers.M4A1_MOD));
    //public static final RegistryObject<GunItem> MOSBERG590 = REGISTER.register("mosberg590", () -> new TimelessGunItem(properties -> properties.group(GunMod.SHOTGUN)));
    public static final RegistryObject<Item> DB_SHORT = REGISTER.register("db_short",
            () -> new TimelessGunItem(properties -> properties.group(GunMod.SHOTGUN)));
    //public static final RegistryObject<Item> DB_LONG = REGISTER.register("db_long", () -> new TimelessGunItem(properties -> properties.group(GunMod.SHOTGUN)));
    //public static final RegistryObject<Item> WALTHER_PPK = REGISTER.register("walther_ppk", () -> new TimelessPistolGunItem(properties -> properties.group(GunMod.PISTOL)));
    public static final RegistryObject<GunItem> M24 = REGISTER.register("m24",
            () -> new TimelessGunItem(properties -> properties.group(GunMod.SNIPER), GunModifiers.M24_MOD));
    //public static final RegistryObject<Item> PPSH_41 = REGISTER.register("ppsh_41", () -> new TimelessGunItem(properties -> properties.group(GunMod.SMG)));
    public static final RegistryObject<Item> QBZ_95 = REGISTER.register("qbz_95",
            () -> new TimelessGunItem(properties -> properties.group(GunMod.RIFLE), GunModifiers.AR_MOD));
    //public static final RegistryObject<Item> SPRINGFIELD_1903 = REGISTER.register("springfield_1903", () -> new TimelessOldRifleGunItem(properties -> properties.group(GunMod.SNIPER)));
    public static final RegistryObject<Item> AA_12 = REGISTER.register("aa_12",
            () -> new TimelessGunItem(properties -> properties.group(GunMod.SHOTGUN), GunModifiers.AA_12_MOD));
    //public static final RegistryObject<Item> X95R = REGISTER.register("x95r", () -> new TimelessGunItem(properties -> properties.group(GunMod.RIFLE)));
    //public static final RegistryObject<Item> SMLE_III = REGISTER.register("smle_iii", () -> new TimelessOldRifleGunItem(properties -> properties.group(GunMod.SNIPER)));
    public static final RegistryObject<Item> M870_CLASSIC = REGISTER.register("m870_classic",
            () -> new TimelessGunItem(properties -> properties.group(GunMod.SHOTGUN), GunModifiers.M870_MOD));//properties.group(GunMod.SHOTGUN)));
    //public static final RegistryObject<Item> MG3 = REGISTER.register("mg3", () -> new TimelessGunItem(properties -> properties.group(GunMod.HEAVY_MATERIAL)));
    //public static final RegistryObject<Item> MG42 = REGISTER.register("mg42", () -> new TimelessGunItem(properties -> properties.group(GunMod.HEAVY_MATERIAL)));
    public static final RegistryObject<Item> M1A1_SMG = REGISTER.register("m1a1_smg",
            () -> new TimelessGunItem(properties -> properties.group(GunMod.SMG), GunModifiers.M1A1_MOD));
    public static final RegistryObject<Item> MK14 = REGISTER.register("mk14",
            () -> new TimelessGunItem(properties -> properties.group(GunMod.RIFLE), GunModifiers.MK14_MOD));
    public static final RegistryObject<Item> DEAGLE_357 = REGISTER.register("deagle_357",
            () -> new TimelessGunItem(properties -> properties.group(GunMod.PISTOL), GunModifiers.DEAGLE50_MOD));
    public static final RegistryObject<Item> HK_MP5A5 = REGISTER.register("hk_mp5a5",
            () -> new TimelessGunItem(properties -> properties.group(GunMod.SMG), GunModifiers.MP5A5_MOD));
    public static final RegistryObject<Item> GLOCK_18 = REGISTER.register("glock_18",
            () -> new TimelessPistolGunItem(properties -> properties.group(GunMod.PISTOL), GunModifiers.PISTOL_MOD));
    public static final RegistryObject<Item> CZ75 = REGISTER.register("cz75",
            () -> new TimelessPistolGunItem(properties -> properties.group(GunMod.PISTOL), GunModifiers.PISTOL_MOD));
    public static final RegistryObject<Item> CZ75_AUTO = REGISTER.register("cz75_auto",
            () -> new TimelessPistolGunItem(properties -> properties.group(GunMod.PISTOL), GunModifiers.PISTOL_MOD));
    //public static final RegistryObject<Item> VZ61 = REGISTER.register("vz61", () -> new TimelessPistolGunItem(properties -> properties.group(GunMod.PISTOL)));
    //public static final RegistryObject<Item> QSZ92G1 = REGISTER.register("qsz92g1", () -> new TimelessPistolGunItem(properties -> properties.group(GunMod.PISTOL)));
    public static final RegistryObject<Item> HK416_A5 = REGISTER.register("hk416_a5",
            () -> new TimelessGunItem(properties -> properties.group(GunMod.RIFLE), GunModifiers.HK416_MOD));
    public static final RegistryObject<Item> TYPE81_X = REGISTER.register("type81_x",
            () -> new TimelessGunItem(properties -> properties.group(GunMod.RIFLE), GunModifiers.TYPE81x_MOD));
    //public static final RegistryObject<Item> PKP_PENCHENBERG = REGISTER.register("pkp_penchenberg", () -> new TimelessGunItem(properties -> properties.group(GunMod.HEAVY_MATERIAL)));
    public static final RegistryObject<Item> MP7 = REGISTER.register("mp7",
            () -> new TimelessGunItem(properties -> properties.group(GunMod.SMG), GunModifiers.MP7_MOD));
    public static final RegistryObject<Item> M82A2 = REGISTER.register("m82a2",
            () -> new TimelessGunItem(properties -> properties.group(GunMod.SNIPER), GunModifiers.M82A2_MOD));
    public static final RegistryObject<Item> AI_AWP = REGISTER.register("ai_awp",
            () -> new TimelessGunItem(properties -> properties.group(GunMod.SNIPER), GunModifiers.AIAWP_MOD));
    //public static final RegistryObject<Item> KAR98 = REGISTER.register("kar98", () -> new TimelessGunItem(properties -> properties.group(GunMod.SNIPER), GunModifiers.AIAWP_MOD));
    public static final RegistryObject<Item> MRAD = REGISTER.register("mrad",
            () -> new TimelessGunItem(properties -> properties.group(GunMod.SNIPER), GunModifiers.MRAD_MOD));
    public static final RegistryObject<GunItem> RPG7 = REGISTER.register("rpg7",
            () -> new TimelessGunItem(properties -> properties.group(GunMod.HEAVY_MATERIAL), GunModifiers.RPG7_MOD));
    public static final RegistryObject<Item> TEC_9 = REGISTER.register("tec_9",
            () -> new TimelessPistolGunItem(properties -> properties.group(GunMod.PISTOL), GunModifiers.TEC_9_MOD));
    public static final RegistryObject<Item> RPK = REGISTER.register("rpk",
            () -> new TimelessGunItem(properties -> properties.group(GunMod.HEAVY_MATERIAL), GunModifiers.RPK_MOD));
    public static final RegistryObject<Item> FN_FAL = REGISTER.register("fn_fal",
            () -> new TimelessGunItem(properties -> properties.group(GunMod.RIFLE), GunModifiers.FN_FAL_MOD));
    //public static final RegistryObject<Item> M1_GARAND = REGISTER.register("m1_garand", () -> new M1GunItem(properties -> properties.group(GunMod.RIFLE), GunModifiers.M1_GARAND_MOD));
    public static final RegistryObject<Item> SIG_MCX_SPEAR = REGISTER.register("sig_mcx_spear",
            () -> new TimelessGunItem(properties -> properties.group(GunMod.RIFLE), GunModifiers.SIG_MCX_SPEAR_MOD));
    public static final RegistryObject<Item> MP9 = REGISTER.register("mp9",
            () -> new TimelessGunItem(properties -> properties.group(GunMod.SMG), GunModifiers.MP9_MOD));
    public static final RegistryObject<Item> SKS_TACTICAL = REGISTER.register("sks_tactical",
            () -> new TimelessGunItem(properties -> properties.group(GunMod.RIFLE), GunModifiers.SKS_TAC_MOD));
    public static final RegistryObject<Item> M1014 = REGISTER.register("m1014",
            () -> new TimelessGunItem(properties -> properties.group(GunMod.SHOTGUN), GunModifiers.M1014_MOD));
    public static final RegistryObject<Item> M249 = REGISTER.register("m249",
            () -> new TimelessGunItem(properties -> properties.group(GunMod.HEAVY_MATERIAL), GunModifiers.M249_MOD));
    //public static final RegistryObject<GunItem> M79 = REGISTER.register("m79", () -> new TimelessGunItem(properties -> properties.group(GunMod.EXPLOSIVES), GunModifiers.M79_MOD));
    //public static final RegistryObject<GunItem> MGL_40MM = REGISTER.register("mgl_40mm", () -> new TimelessGunItem(properties -> properties.group(GunMod.EXPLOSIVES), true, GunModifiers.MGL_40MM_MOD));
    public static final RegistryObject<Item> MK23 = REGISTER.register("mk23",
            () -> new TimelessPistolGunItem(properties -> properties.group(GunMod.PISTOL), GunModifiers.PISTOL_MOD));
    public static final RegistryObject<Item> QBZ_191 = REGISTER.register("qbz_191",
            () -> new TimelessGunItem(properties -> properties.group(GunMod.RIFLE), GunModifiers.QBZ_191_MOD));
    //public static final RegistryObject<Item> C96 = REGISTER.register("c96", () -> new TimelessPistolGunItem(properties -> properties.group(GunMod.PISTOL)));
    public static final RegistryObject<Item> M16A4 = REGISTER.register("m16a4",
            () -> new TimelessGunItem(properties -> properties.group(GunMod.RIFLE), GunModifiers.M16A4_MOD));
    public static final RegistryObject<Item> SCAR_H = REGISTER.register("scar_h",
            () -> new TimelessGunItem(properties -> properties.group(GunMod.RIFLE), GunModifiers.SCAR_H_MOD));
    public static final RegistryObject<Item> SCAR_L = REGISTER.register("scar_l",
            () -> new TimelessGunItem(properties -> properties.group(GunMod.RIFLE), GunModifiers.SCAR_L_MOD));
    public static final RegistryObject<Item> MK47 = REGISTER.register("mk47",
            () -> new TimelessGunItem(properties -> properties.group(GunMod.RIFLE), GunModifiers.MK47_MOD));
    public static final RegistryObject<Item> SPR_15 = REGISTER.register("spr15",
            () -> new TimelessGunItem(properties -> properties.group(GunMod.RIFLE), GunModifiers.SPR_15_MOD));
    public static final RegistryObject<Item> TTI_G34 = REGISTER.register("tti_g34",
            () -> new TimelessPistolGunItem(properties -> properties.group(GunMod.PISTOL), GunModifiers.TTI34_MOD));
    public static final RegistryObject<Item> MK18_MOD1 = REGISTER.register("mk18_mod1",
            () -> new TimelessGunItem(properties -> properties.group(GunMod.RIFLE), GunModifiers.MK18_MOD1_MOD));
    public static final RegistryObject<Item> UDP_9 = REGISTER.register("udp_9",
            () -> new TimelessGunItem(properties -> properties.group(GunMod.SMG), GunModifiers.UDP_9_MOD));
    public static final RegistryObject<Item> SCAR_MK20 = REGISTER.register("scar_mk20",
            () -> new TimelessGunItem(properties -> properties.group(GunMod.SNIPER), GunModifiers.SCAR_MK20_MOD));
    public static final RegistryObject<Item> HK_G3 = REGISTER.register("hk_g3",
            () -> new TimelessGunItem(properties -> properties.group(GunMod.RIFLE), GunModifiers.HK_G3_MOD));
    public static final RegistryObject<Item> TIMELESS_50 = REGISTER.register("timeless_50",
            () -> new TimelessGunItem(properties -> properties.group(GunMod.PISTOL), GunModifiers.STI2011_MOD));
    public static final RegistryObject<Item> COLT_PYTHON = REGISTER.register("colt_python",
            () -> new TimelessGunItem(properties -> properties.group(GunMod.PISTOL), GunModifiers.COLT_PYTHON_MOD));
    public static final RegistryObject<Item> P90 = REGISTER.register("p90",
            () -> new TimelessGunItem(properties -> properties.group(GunMod.SMG), GunModifiers.P90_MOD));
    public static final RegistryObject<Item> C96 = REGISTER.register("c96",
            () -> new TimelessGunItem(properties -> properties.group(GunMod.PISTOL), GunModifiers.C96_MOD));

    /*** Ammunition ***/
    public static final RegistryObject<Item> BULLET_MAGNUM = REGISTER.register("b_magnum", TimelessAmmoItem::new);
    public static final RegistryObject<Item> BULLET_46 = REGISTER.register("46x30", TimelessAmmoItem::new);
    public static final RegistryObject<Item> BULLET_45 = REGISTER.register("round45", TimelessAmmoItem::new);
    public static final RegistryObject<Item> BULLET_50AE = REGISTER.register("ae50", TimelessAmmoItem::new);
    public static final RegistryObject<Item> BULLET_30_WIN = REGISTER.register("win_30-30", TimelessLRFAmmoItem::new);
    public static final RegistryObject<Item> BULLET_308 = REGISTER.register("bullet_308", TimelessAmmoItem::new);
    public static final RegistryObject<Item> BULLET_556 = REGISTER.register("nato_556_bullet", TimelessAmmoItem::new);
    public static final RegistryObject<Item> BULLET_9 = REGISTER.register("9mm_round", TimelessAmmoItem::new);
    public static final RegistryObject<Item> BULLET_10g = REGISTER.register("10_gauge_round", TimelessSGAmmoItem::new);
    public static final RegistryObject<Item> BULLET_58x42 = REGISTER.register("58x42", TimelessAmmoItem::new);
    public static final RegistryObject<Item> BULLET_762x25 = REGISTER.register("762x25", TimelessAmmoItem::new);
    public static final RegistryObject<Item> BULLET_762x54 = REGISTER.register("762x54", TimelessAmmoItem::new);
    public static final RegistryObject<Item> BULLET_762x39 = REGISTER.register("762x39", TimelessAmmoItem::new);
    public static final RegistryObject<Item> BULLET_50_BMG = REGISTER.register("50bmg", TimelessRFAmmoItem::new);
    //public static final RegistryObject<Item> BULLET_300MAG = REGISTER.register("762x39", TimelessAmmoItem::new);
    public static final RegistryObject<Item> BULLET_LAPUA338 = REGISTER.register("lapua338", TimelessRFAmmoItem::new);
    //public static final RegistryObject<Item> GRENADE_40MM = REGISTER.register("grenade40mm", Timeless40AmmoItem::new);
    public static final RegistryObject<Item> BULLET_68 = REGISTER.register("bullet68", TimelessAmmoItem::new);
    public static final RegistryObject<Item> BULLET_57 = REGISTER.register("57x28", TimelessAmmoItem::new);

    /*** Explosives ***/
    public static final RegistryObject<Item> RPG7_MISSILE = REGISTER.register("rpg7_missile",
            () -> new AmmoItem(new Item.Properties().maxStackSize(6).group(GunMod.AMMO)));
    public static final RegistryObject<Item> LIGHT_GRENADE = REGISTER.register("light_grenade",
            () -> new LightGrenadeItem(new Item.Properties().maxStackSize(8).group(GunMod.EXPLOSIVES), 25 * 4, 30f, 1f, 1.35f));
    public static final RegistryObject<Item> BASEBALL_GRENADE = REGISTER.register("baseball_grenade",
            () -> new BaseballGrenadeItem(new Item.Properties().maxStackSize(4).group(GunMod.EXPLOSIVES), 20 * 7, 50f, 1.25f, 1.135f));

    /*** Utility throwables ***/
    public static final RegistryObject<Item> STANDARD_FLASH_GRENADE = REGISTER.register("flash_grenade",
            () -> new FlashGrenadeItem(new Item.Properties().maxStackSize(4).group(GunMod.EXPLOSIVES),20 * 5, 0.95f));
    public static final RegistryObject<Item> IMPACT_FLASH_GRENADE = REGISTER.register("impact_flash_grenade",
            () -> new ImpactFlashGrenadeItem(new Item.Properties().maxStackSize(4).group(GunMod.EXPLOSIVES), 20*100, 1.10f));
    //TODO: Implement stun type, save screenstate and overlay, apply some form of accuracy reduction, (more recoil, slow sensitivity, ADS force false, one of which etc.)
    //public static final RegistryObject<Item> STUN_GRENADE = REGISTER.register("stun_grenade", () -> new StunGrenadeItem(new Item.Properties().maxStackSize(4).group(GunMod.EXPLOSIVES), 20 * 5, 0.95f));

    /*** Scope Attachments ***/
    /* Common Scopes */
    public static final RegistryObject<Item> COYOTE_SIGHT = REGISTER.register("coyote_sight",
            () -> new ScopeItem(Scope.create(new ScopeZoomData[]{new ScopeZoomData(1.0F, 0.00F)}, 2.15F, 0.325F, "coyote",
                    new ResourceLocation("tac:coyote_sight")).viewFinderOffset(0.415).viewFinderOffsetSpecial(0.415), new Item.Properties().maxStackSize(1).group(GunMod.GROUP)));

    public static final RegistryObject<Item> AIMPOINT_T2_SIGHT = REGISTER.register("aimpoint_t2",
            () -> new ScopeItem(Scope.create(new ScopeZoomData[]{new ScopeZoomData(1.0F, 0.00F)}, 2.635F, 0.325F, "aimpoint2",
                    new ResourceLocation("tac:aimpoint_t2")).viewFinderOffset(0.39).viewFinderOffsetSpecial(0.39), new Item.Properties().maxStackSize(1).group(GunMod.GROUP)));
    public static final RegistryObject<Item> AIMPOINT_T1_SIGHT = REGISTER.register("aimpoint_t1",
            () -> new ScopeItem(Scope.create(new ScopeZoomData[]{new ScopeZoomData(1.0F, 0.00F)}, 1.3F, 0.325F, "aimpoint1",
                    new ResourceLocation("tac:aimpoint_t1")).viewFinderOffset(0.45).viewFinderOffsetSpecial(0.45), new Item.Properties().maxStackSize(1).group(GunMod.GROUP)));
    public static final RegistryObject<Item> EOTECH_N_SIGHT = REGISTER.register("eotech_n",
            () -> new ScopeItem(Scope.create(new ScopeZoomData[]{new ScopeZoomData(1.0F, 0.00F)}, 2.225F, 0.325F, "eotechn",
                    new ResourceLocation("tac:eotech_n")).viewFinderOffset(0.415).viewFinderOffsetSpecial(0.415), new Item.Properties().maxStackSize(1).group(GunMod.GROUP)));
    public static final RegistryObject<Item> VORTEX_UH_1 = REGISTER.register("vortex_uh_1",
            () -> new ScopeItem(Scope.create(new ScopeZoomData[]{new ScopeZoomData(0.00F, 0.00F)}, 2.525F, 0.325F, "vortex1",
                    new ResourceLocation("tac:vortex_uh_1")).viewFinderOffset(0.3725).viewFinderOffsetSpecial(0.3725), new Item.Properties().maxStackSize(1).group(GunMod.GROUP)));
    public static final RegistryObject<Item> EOTECH_SHORT_SIGHT = REGISTER.register("eotech_short",
            () -> new ScopeItem(Scope.create(new ScopeZoomData[]{new ScopeZoomData(1.0F, 0.00F)}, 2.71F, 0.325F, "eotechshort",
                    new ResourceLocation("tac:eotech_short")).viewFinderOffset(0.455).viewFinderOffsetSpecial(0.455), new Item.Properties().maxStackSize(1).group(GunMod.GROUP)));
    public static final RegistryObject<Item> SRS_RED_DOT_SIGHT = REGISTER.register("srs_red_dot",
            () -> new ScopeItem(Scope.create(new ScopeZoomData[]{new ScopeZoomData(1.0F, 0.00F)}, 2.2675F, 0.325F, "srsdot",
                    new ResourceLocation("tac:srs_red_dot")).viewFinderOffset(0.355).viewFinderOffsetSpecial(0.355), new Item.Properties().maxStackSize(1).group(GunMod.GROUP)));
    public static final RegistryObject<Item> ACOG_4 = REGISTER.register("acog_4x_scope",
            () -> new ScopeItem(Scope.create(new ScopeZoomData[]{new ScopeZoomData(4f, 0.4F, 1.5f)}, 2.325F, 0.22F, "acog4x", true,
                    new ResourceLocation("tac:acog_4x_scope")).viewFinderOffset(0.5).viewFinderOffsetDR(0.4).viewFinderOffsetSpecial(0.425).viewFinderOffsetSpecialDR(0.35), new Item.Properties().maxStackSize(1).group(GunMod.GROUP)));
    public static final RegistryObject<Item> QMK152 = REGISTER.register("qmk152",
            () -> new ScopeItem(Scope.create(new ScopeZoomData[]{new ScopeZoomData(3F, 0.4F, -0.4f)}, 2.39F, 0.24F, "qmk152", true,
                    new ResourceLocation("tac:qmk152")).viewFinderOffset(0.5).viewFinderOffsetDR(0.4).viewFinderOffsetSpecial(0.45).viewFinderOffsetSpecialDR(0.375), new Item.Properties().maxStackSize(1).group(GunMod.GROUP)));
    public static final RegistryObject<Item> ELCAN_DR_14X = REGISTER.register("elcan_14x",
            () -> new ScopeItem(Scope.create(new ScopeZoomData[]{new ScopeZoomData(4f, 0.4225F, 2.0f), new ScopeZoomData(2f, 0.345F, 2.0f)}, 2.45F, 0.23, "elcan14x", true,
                    new ResourceLocation("tac:elcan_14x")).viewFinderOffset(0.515).viewFinderOffsetDR(0.45).viewFinderOffsetSpecial(0.435).viewFinderOffsetSpecialDR(0.38), new Item.Properties().maxStackSize(1).group(GunMod.GROUP)));
    public static final RegistryObject<Item> VORTEX_LPVO_3_6 = REGISTER.register("lpvo_1_6",
            () -> new ScopeItem(Scope.create(new ScopeZoomData[]{new ScopeZoomData(6f, 0.4175F, 2.6f), new ScopeZoomData(3f, 0.365F, 2.6f)}, 2.2625F, 0.1925, "vlpvo6", true,
                    new ResourceLocation("tac:lpvo_1_6")).viewFinderOffset(0.475).viewFinderOffsetDR(0.375).viewFinderOffsetSpecial(0.505).viewFinderOffsetSpecialDR(0.355), new Item.Properties().maxStackSize(1).group(GunMod.GROUP)));
    public static final RegistryObject<Item> STANDARD_6_10x_SCOPE = REGISTER.register("8x_scope",
            () -> new ScopeItem(Scope.create(new ScopeZoomData[]{new ScopeZoomData(10f,0.395F, 4.5f), new ScopeZoomData(6f, 0.4225F, 4.5f)}, 2.45F, 0.2125, "gener8x", true,
                    new ResourceLocation("tac:8x_scope")).viewFinderOffset(0.595).viewFinderOffsetDR(0.3925).viewFinderOffsetSpecial(0.465).viewFinderOffsetSpecialDR(0.415), new Item.Properties().maxStackSize(1).group(GunMod.GROUP)));

    /* Old Scopes */
    public static final RegistryObject<Item> OLD_LONGRANGE_8x_SCOPE = REGISTER.register("old_8x_scope",
            () -> new OldScopeItem(Scope.create(new ScopeZoomData[]{new ScopeZoomData(8F, 0.00F)}, 1.930F, 0.17F, "old8x", true,
                    new ResourceLocation("tac:old_8x_scope")).viewFinderOffset(0.51).viewFinderOffsetDR(0.36).viewFinderOffsetSpecial(0.42).viewFinderOffsetSpecialDR(0.3), new Item.Properties().maxStackSize(1).group(GunMod.GROUP)));
    public static final RegistryObject<Item> OLD_LONGRANGE_4x_SCOPE = REGISTER.register("old_4x_scope",
            () -> new OldScopeItem(Scope.create(new ScopeZoomData[]{new ScopeZoomData(4F, 0.00F)}, 1.930F, 0.21F, "old4x", true,
                    new ResourceLocation("tac:old_4x_scope")).viewFinderOffset(0.5).viewFinderOffsetDR(0.35).viewFinderOffsetSpecial(0.4).viewFinderOffsetSpecialDR(0.28), new Item.Properties().maxStackSize(1).group(GunMod.GROUP)));

    /* Pistol Scopes */
    public static final RegistryObject<Item> MINI_DOT = REGISTER.register("mini_dot",
            () -> new PistolScopeItem(Scope.create(new ScopeZoomData[]{new ScopeZoomData(1.0F, 0.00F)}, 1.475F, 0.325, "minidot",
                    new ResourceLocation("tac:mini_dot")).viewFinderOffset(0.685).viewFinderOffsetSpecial(0.685), new Item.Properties().maxStackSize(1).group(GunMod.GROUP)));
    public static final RegistryObject<Item> SRO_DOT = REGISTER.register("sro_dot",
            () -> new PistolScopeItem(Scope.create(new ScopeZoomData[]{new ScopeZoomData(1.0F, 0.00F)}, 1.615F, 0.325, "sro_dot",
                    new ResourceLocation("tac:sro_dot")).viewFinderOffset(0.685).viewFinderOffsetSpecial(0.685), new Item.Properties().maxStackSize(1).group(GunMod.GROUP)));

    /*** Barrel Attachments ***/
    /* Common Barrel Attachments */
    public static final RegistryObject<Item> SILENCER = REGISTER.register("silencer",
            () -> new BarrelItem(Barrel.create(8.0F, new ResourceLocation("tac:silencer")),
                    new Item.Properties().maxStackSize(1).group(GunMod.GROUP)));
    public static final RegistryObject<Item> MUZZLE_BRAKE = REGISTER.register("muzzle_brake",
            () -> new BarrelItem(Barrel.create(2.0F, new ResourceLocation("tac:muzzle_brake")),
                    new Item.Properties().maxStackSize(1).group(GunMod.GROUP)));
    public static final RegistryObject<Item> MUZZLE_COMPENSATOR = REGISTER.register("muzzle_compensator",
            () -> new BarrelItem(Barrel.create(2.0F, new ResourceLocation("tac:muzzle_compensator")),
                    new Item.Properties().maxStackSize(1).group(GunMod.GROUP)));

    /* Pistol Barrel Attachments */
    public static final RegistryObject<Item> PISTOL_SILENCER = REGISTER.register("pistol_silencer",
            () -> new PistolBarrelItem(Barrel.create(8.0F, new ResourceLocation("tac:pistol_silencer")),
                    new Item.Properties().maxStackSize(1).group(GunMod.GROUP)));

    /*** Stock Attachments ***/
    public static final RegistryObject<Item> LIGHT_STOCK = REGISTER.register("light_stock",
            () -> new StockItem(Stock.create(new ResourceLocation("tac:light_stock")),
                    new Item.Properties().maxStackSize(1).group(GunMod.GROUP), false));
    public static final RegistryObject<Item> TACTICAL_STOCK = REGISTER.register("tactical_stock",
            () -> new StockItem(Stock.create(new ResourceLocation("tac:tactical_stock")),
                    new Item.Properties().maxStackSize(1).group(GunMod.GROUP), false));
    public static final RegistryObject<Item> WEIGHTED_STOCK = REGISTER.register("weighted_stock",
            () -> new StockItem(Stock.create(new ResourceLocation("tac:weighted_stock")),
                    new Item.Properties().maxStackSize(1).group(GunMod.GROUP)));

    /*** Under Barrel Attachments ***/
    public static final RegistryObject<Item> LIGHT_GRIP = REGISTER.register("light_grip",
            () -> new UnderBarrelItem(UnderBarrel.create(new ResourceLocation("tac:light_grip")),
                    new Item.Properties().maxStackSize(1).group(GunMod.GROUP)));
    public static final RegistryObject<Item> SPECIALISED_GRIP = REGISTER.register("specialised_grip",
            () -> new UnderBarrelItem(UnderBarrel.create(new ResourceLocation("tac:specialised_grip")),
                    new Item.Properties().maxStackSize(1).group(GunMod.GROUP)));

    /*** Side rail Attachments ***/
    public static final RegistryObject<Item> BASIC_LASER = REGISTER.register("basic_laser",
            () -> new SideRailItem(SideRail.create(new ResourceLocation("tac:basic_laser")),
                    new Item.Properties().maxStackSize(1).group(GunMod.GROUP)));
    public static final RegistryObject<Item> IR_LASER = REGISTER.register("ir_laser",
            () -> new IrDeviceItem(IrDevice.create(new ResourceLocation("tac:ir_laser")),
                    new Item.Properties().maxStackSize(1).group(GunMod.GROUP)));

    /*** MAG ***/
    public static final RegistryObject<Item> SMALL_EXTENDED_MAG = REGISTER.register("small_extended_mag",
            () -> new ExtendedMagItem(ExtendedMag.create(new ResourceLocation("tac:small_extended_mag")),
                    new Item.Properties().maxStackSize(1).group(GunMod.GROUP)));
    public static final RegistryObject<Item> MEDIUM_EXTENDED_MAG = REGISTER.register("medium_extended_mag",
            () -> new ExtendedMagItem(ExtendedMag.create(new ResourceLocation("tac:medium_extended_mag")),
                    new Item.Properties().maxStackSize(1).group(GunMod.GROUP)));
    public static final RegistryObject<Item> LARGE_EXTENDED_MAG = REGISTER.register("large_extended_mag",
            () -> new ExtendedMagItem(ExtendedMag.create(new ResourceLocation("tac:large_extended_mag")),
                    new Item.Properties().maxStackSize(1).group(GunMod.GROUP)));

    /*** AMMO_PLUG ***/
    public static final RegistryObject<Item> AMMO_FMJ = REGISTER.register("ammo_fmj",
            () -> new AmmoPlugItem(AmmoPlug.create(new ResourceLocation("tac:ammo_fmj")),
                    new Item.Properties().maxStackSize(1).group(GunMod.GROUP)));
    public static final RegistryObject<Item> AMMO_HE = REGISTER.register("ammo_he",
            () -> new AmmoPlugItem(AmmoPlug.create(new ResourceLocation("tac:ammo_he")),
                    new Item.Properties().maxStackSize(1).group(GunMod.GROUP)));
    public static final RegistryObject<Item> AMMO_HP = REGISTER.register("ammo_hp",
            () -> new AmmoPlugItem(AmmoPlug.create(new ResourceLocation("tac:ammo_hp")),
                    new Item.Properties().maxStackSize(1).group(GunMod.GROUP)));
    public static final RegistryObject<Item> AMMO_I = REGISTER.register("ammo_i",
            () -> new AmmoPlugItem(AmmoPlug.create(new ResourceLocation("tac:ammo_i")),
                    new Item.Properties().maxStackSize(1).group(GunMod.GROUP)));

    /*** Misc ***/
    public static final RegistryObject<Item> BLANK_SKIN_MOD_LVL1 = REGISTER.register("blank_skin_mod_lvl1",
            () -> new Item(new Item.Properties().maxStackSize(64).group(GunMod.SKINS)));
    public static final RegistryObject<Item> UNCOMMON_MATERIAL = REGISTER.register("uncommon_material",
            () -> new Item(new Item.Properties().maxStackSize(64).group(GunMod.GROUP)));
    public static final RegistryObject<Item> RARE_MATERIAL = REGISTER.register("rare_material",
            () -> new Item(new Item.Properties().maxStackSize(48).group(GunMod.GROUP)));
    public static final RegistryObject<Item> EPIC_MATERIAL = REGISTER.register("epic_material",
            () -> new Item(new Item.Properties().maxStackSize(32).group(GunMod.GROUP)));
    public static final RegistryObject<Item> LEGENDARY_MATERIAL = REGISTER.register("legendary_material",
            () -> new Item(new Item.Properties().maxStackSize(24).group(GunMod.GROUP)));
    public static final RegistryObject<Item> ULTIMATE_MATERIAL = REGISTER.register("ultimate_material",
            () -> new Item(new Item.Properties().maxStackSize(16).group(GunMod.GROUP)));
    //public static final RegistryObject<Item> LEGENDARY_CERTIFICATE = REGISTER.register("legendary_certificate",() -> new Item(new Item.Properties().maxStackSize(32).group(GunMod.GROUP)));
    //public static final RegistryObject<Item> ULTIMATE_CERTIFICATE = REGISTER.register("ultimate_certificate",() -> new Item(new Item.Properties().maxStackSize(16).group(GunMod.GROUP)));

    /* Skin */
//    public static final RegistryObject<Item> SKIN_CUSTOM = REGISTER.register("skin_custom",
//            () -> new GunSkinItem(GunSkin.create((ResourceLocation) null),new Item.Properties().maxStackSize(1).group(GunMod.SKINS)));
    /*** Skin ***/
    public static final RegistryObject<Item> SKIN_AK_SPENT_BULLET = REGISTER.register("skin_ak_spent_bullet",
            () -> new GunSkinItem(GunSkin.create(new ResourceLocation("tac:skin_ak_spent_bullet")), new Item.Properties().maxStackSize(1).group(GunMod.SKINS)));
    public static final RegistryObject<Item> SKIN_MP9_THUNDER = REGISTER.register("skin_mp9_thunder",
            () -> new GunSkinItem(GunSkin.create(new ResourceLocation("tac:skin_mp9_thunder")), new Item.Properties().maxStackSize(1).group(GunMod.SKINS)));

    /* Common Skin */
    public static final RegistryObject<Item> SKIN_BLACK = REGISTER.register("skin_black",
            () -> new GunSkinItem(GunSkin.create("black"), new Item.Properties().maxStackSize(1).group(GunMod.SKINS)));
    public static final RegistryObject<Item> SKIN_BLUE = REGISTER.register("skin_blue",
            () -> new GunSkinItem(GunSkin.create("blue"), new Item.Properties().maxStackSize(1).group(GunMod.SKINS)));
    public static final RegistryObject<Item> SKIN_BROWN = REGISTER.register("skin_brown",
            () -> new GunSkinItem(GunSkin.create("brown"), new Item.Properties().maxStackSize(1).group(GunMod.SKINS)));
    public static final RegistryObject<Item> SKIN_DARK_BLUE = REGISTER.register("skin_dark_blue",
            () -> new GunSkinItem(GunSkin.create("dark_blue"), new Item.Properties().maxStackSize(1).group(GunMod.SKINS)));
    public static final RegistryObject<Item> SKIN_DARK_GREEN = REGISTER.register("skin_dark_green",
            () -> new GunSkinItem(GunSkin.create("dark_green"), new Item.Properties().maxStackSize(1).group(GunMod.SKINS)));
    public static final RegistryObject<Item> SKIN_GRAY = REGISTER.register("skin_gray",
            () -> new GunSkinItem(GunSkin.create("gray"), new Item.Properties().maxStackSize(1).group(GunMod.SKINS)));
    public static final RegistryObject<Item> SKIN_GREEN = REGISTER.register("skin_green",
            () -> new GunSkinItem(GunSkin.create("green"), new Item.Properties().maxStackSize(1).group(GunMod.SKINS)));
    public static final RegistryObject<Item> SKIN_JADE = REGISTER.register("skin_jade",
            () -> new GunSkinItem(GunSkin.create("jade"), new Item.Properties().maxStackSize(1).group(GunMod.SKINS)));
    public static final RegistryObject<Item> SKIN_LIGHT_GRAY = REGISTER.register("skin_light_gray",
            () -> new GunSkinItem(GunSkin.create("light_gray"), new Item.Properties().maxStackSize(1).group(GunMod.SKINS)));
    public static final RegistryObject<Item> SKIN_MAGENTA = REGISTER.register("skin_magenta",
            () -> new GunSkinItem(GunSkin.create("magenta"), new Item.Properties().maxStackSize(1).group(GunMod.SKINS)));
    public static final RegistryObject<Item> SKIN_ORANGE = REGISTER.register("skin_orange",
            () -> new GunSkinItem(GunSkin.create("orange"), new Item.Properties().maxStackSize(1).group(GunMod.SKINS)));
    public static final RegistryObject<Item> SKIN_PINK = REGISTER.register("skin_pink",
            () -> new GunSkinItem(GunSkin.create("pink"), new Item.Properties().maxStackSize(1).group(GunMod.SKINS)));
    public static final RegistryObject<Item> SKIN_PURPLE = REGISTER.register("skin_purple",
            () -> new GunSkinItem(GunSkin.create("purple"), new Item.Properties().maxStackSize(1).group(GunMod.SKINS)));
    public static final RegistryObject<Item> SKIN_RAD = REGISTER.register("skin_red",
            () -> new GunSkinItem(GunSkin.create("red"), new Item.Properties().maxStackSize(1).group(GunMod.SKINS)));
    public static final RegistryObject<Item> SKIN_SAND = REGISTER.register("skin_sand",
            () -> new GunSkinItem(GunSkin.create("sand"), new Item.Properties().maxStackSize(1).group(GunMod.SKINS)));
    public static final RegistryObject<Item> SKIN_WHITE = REGISTER.register("skin_white",
            () -> new GunSkinItem(GunSkin.create("white"), new Item.Properties().maxStackSize(1).group(GunMod.SKINS)));
}
