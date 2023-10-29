package com.tac.guns.common;

import com.tac.guns.interfaces.IGunModifier;

/**
 * Author: Forked from MrCrayfish, continued by Timeless devs
 */

public class GunModifiers {
    //////////////////////// SPECIFICS PER ATTACHMENT ////////////////////////
    public static final IGunModifier COYOTE_SIGHT_ADS = new IGunModifier() {
        @Override
        public double modifyAimDownSightSpeed(double speed) {
            return speed * 0.97F;
        }

        @Override
        public float additionalWeaponWeight() {
            return 0.20F;
        }
    };

    public static final IGunModifier AIMPOINT_T1_SIGHT_ADS = new IGunModifier() {
        @Override
        public double modifyAimDownSightSpeed(double speed) {
            return speed * 0.97F;
        }

        @Override
        public float additionalWeaponWeight() {
            return 0.20F;
        }
    };

    public static final IGunModifier EOTECH_N_SIGHT_ADS = new IGunModifier() {
        @Override
        public double modifyAimDownSightSpeed(double speed) {
            return speed * 0.96F;
        }

        @Override
        public float additionalWeaponWeight() {
            return 0.35F;
        }
    };

    public static final IGunModifier VORTEX_UH_1_ADS = new IGunModifier() {
        @Override
        public double modifyAimDownSightSpeed(double speed) {
            return speed * 0.98F;
        }

        @Override
        public float additionalWeaponWeight() {
            return 0.25F;
        }
    };

    public static final IGunModifier EOTECH_SHORT_SIGHT_ADS = new IGunModifier() {
        @Override
        public double modifyAimDownSightSpeed(double speed) {
            return speed * 0.98F;
        }

        @Override
        public float additionalWeaponWeight() {
            return 0.30F;
        }
    };

    public static final IGunModifier SRS_RED_DOT_SIGHT_ADS = new IGunModifier() {
        @Override
        public double modifyAimDownSightSpeed(double speed) {
            return speed * 0.97F;
        }

        @Override
        public float additionalWeaponWeight() {
            return 0.30F;
        }
    };

    public static final IGunModifier ACOG_4_ADS = new IGunModifier() {
        @Override
        public double modifyAimDownSightSpeed(double speed) {
            return speed * 0.88F;
        }

        @Override
        public float additionalWeaponWeight() {
            return 0.45F;
        }
    };

    public static final IGunModifier ELCAN_DR_14X_ADS = new IGunModifier() {
        @Override
        public double modifyAimDownSightSpeed(double speed) {
            return speed * 0.86F;
        }

        @Override
        public float additionalWeaponWeight() {
            return 0.5F;
        }
    };

    public static final IGunModifier QMK152_ADS = new IGunModifier() {
        @Override
        public double modifyAimDownSightSpeed(double speed) {
            return speed * 0.90F;
        }

        @Override
        public float additionalWeaponWeight() {
            return 0.4F;
        }
    };

    public static final IGunModifier VORTEX_LPVO_1_6_ADS = new IGunModifier() {
        @Override
        public double modifyAimDownSightSpeed(double speed) {
            return speed * 0.84F;
        }

        @Override
        public float additionalWeaponWeight() {
            return 0.65F;
        }
    };

    public static final IGunModifier LONGRANGE_8x_SCOPE_ADS = new IGunModifier() {
        @Override
        public double modifyAimDownSightSpeed(double speed) {
            return speed * 0.80F;
        }

        @Override
        public float additionalWeaponWeight() {
            return 0.75F;
        }
    };

    public static final IGunModifier OLD_LONGRANGE_8x_SCOPE_ADS = new IGunModifier() {
        @Override
        public double modifyAimDownSightSpeed(double speed) {
            return speed * 0.70F;
        }

        @Override
        public float additionalWeaponWeight() {
            return 1.5F;
        }
    };

    public static final IGunModifier OLD_LONGRANGE_4x_SCOPE_ADS = new IGunModifier() {
        @Override
        public double modifyAimDownSightSpeed(double speed) {
            return speed * 0.75F;
        }

        @Override
        public float additionalWeaponWeight() {
            return 1.2F;
        }
    };

    public static final IGunModifier MINI_DOT_ADS = new IGunModifier() {
        @Override
        public double modifyAimDownSightSpeed(double speed) {
            return speed * 0.985F;
        }

        @Override
        public float additionalWeaponWeight() {
            return 0.125F;
        }
    };

    public static final IGunModifier MICRO_HOLO_SIGHT_ADS = new IGunModifier() {
        @Override
        public double modifyAimDownSightSpeed(double speed) {
            return speed * 0.975F;
        }

        @Override
        public float additionalWeaponWeight() {
            return 0.125F;
        }
    };

    public static final IGunModifier HEAVY_STOCK_MODIFIER = new IGunModifier() {
        @Override
        public float recoilModifier() {
            return 0.55F;
        }

        @Override
        public float kickModifier() {
            return 0.8F;
        }

        @Override
        public float modifyProjectileSpread(float spread) {
            return spread * 0.85F;
        }

        @Override
        public float modifyFirstShotSpread(float spread) {
            return spread * 0.65F;
        }

        @Override
        public float modifyHipFireSpread(float spread) {
            return spread * 1.25F;
        }

        @Override
        public double modifyAimDownSightSpeed(double speed) {
            return speed * 0.80F;
        }

        @Override
        public float horizontalRecoilModifier() {
            return 0.45F;
        }

        @Override
        public float additionalWeaponWeight() {
            return 0.425F;
        }

        @Override
        public float modifyWeaponWeight() {
            return 0.5F;
        }
    };

    public static final IGunModifier TACTICAL_STOCK_MODIFIER = new IGunModifier() {
        @Override
        public float recoilModifier() {
            return 0.75F;
        }

        @Override
        public float kickModifier() {
            return 0.85F;
        }

        @Override
        public float modifyProjectileSpread(float spread) {
            return spread * 0.9F;
        }

        @Override
        public float modifyFirstShotSpread(float spread) {
            return spread * 0.95F;
        }

        @Override
        public double modifyAimDownSightSpeed(double speed) {
            return speed * 0.85F;
        }

        @Override
        public float horizontalRecoilModifier() {
            return 0.7F;
        }

        @Override
        public float additionalWeaponWeight() {
            return 0.3F;
        }

        @Override
        public float modifyWeaponWeight() {
            return 0.3F;
        }
    };

    public static final IGunModifier LIGHT_STOCK_MODIFIER = new IGunModifier() {
        @Override
        public float modifyHipFireSpread(float spread) {
            return spread * 0.925F;
        }

        @Override
        public float kickModifier() {
            return 0.90F;
        }

        @Override
        public float modifyFirstShotSpread(float spread) {
            return spread * 1.10F;
        }

        @Override
        public double modifyAimDownSightSpeed(double speed) {
            return speed * 1.10F;
        }

        @Override
        public float horizontalRecoilModifier() {
            return 0.80F;
        }

        @Override
        public float additionalWeaponWeight() {
            return 0.215F;
        }
    };

    public static final IGunModifier LIGHT_GRIP_MODIFIER = new IGunModifier() {
        @Override
        public float recoilModifier() {
            return 0.92F;
        }

        @Override
        public float horizontalRecoilModifier() {
            return 0.92F;
        }

        @Override
        public double modifyAimDownSightSpeed(double speed) {
            return speed * 1.08F;
        }

        @Override
        public float modifyWeaponWeight() {
            return 0.115F;
        }
    };

    public static final IGunModifier TACTICAL_GRIP_MODIFIER = new IGunModifier() {
        @Override
        public float recoilModifier() {
            return 0.85F;
        }

        @Override
        public float modifyHipFireSpread(float spread) {
            return spread * 0.85F;
        }

        @Override
        public double modifyAimDownSightSpeed(double speed) {
            return speed * 0.88F;
        }

        @Override
        public float horizontalRecoilModifier() {
            return 0.90F;
        }

        @Override
        public float modifyWeaponWeight() {
            return 0.25F;
        }
    };

    public static final IGunModifier TACTICAL_SILENCER = new IGunModifier() {
        @Override
        public boolean silencedFire() {
            return true;
        }

        @Override
        public float modifyFireSoundVolume(float volume) {
            return volume * 0.80F;
        }

        @Override
        public double modifyFireSoundRadius(double radius) {
            return radius * 0.275;
        }

        @Override
        public float modifyFirstShotSpread(float spread) {
            return spread * 0.875F;
        }

        @Override
        public float modifyRecoilSmoothening() {
            return 1.25F;
        }

        @Override
        public double modifyMuzzleFlashSize(double size) {
            return size * 0F;
        }

        @Override
        public float additionalWeaponWeight() {
            return 0.325F;
        }

        @Override
        public float modifyWeaponWeight() {
            return 0.25F;
        }

        @Override
        public double modifyAimDownSightSpeed(double speed) {
            return speed * 0.80;
        }

        //TODO: Model the firerate change, likely wait for more then a single suppressor attachment, tactical suppressor for "flowthrough"
        /*@Override
        public int modifyFireRate(int rate) {
            return (int)(rate*1.15);
        }*/
    };

    public static final IGunModifier BASIC_LASER = new IGunModifier() {
        @Override
        public float modifyProjectileSpread(float spread) {
            return spread * 0.93F;
        }

        @Override
        public float modifyFirstShotSpread(float spread) {
            return spread * 0.90F;
        }

        @Override
        public double modifyAimDownSightSpeed(double speed) {
            return speed * 0.88F;
        }

        @Override
        public float modifyWeaponWeight() {
            return 0.1F;
        }

        @Override
        public float modifyHipFireSpread(float spread) {
            return spread * 0.80F;
        }
    };

    public static final IGunModifier IR_LASER = new IGunModifier() {
        @Override
        public double modifyAimDownSightSpeed(double speed) {
            return speed * 0.85F;
        }

        @Override
        public float modifyWeaponWeight() {
            return 0.175F;
        }

        @Override
        public float modifyFirstShotSpread(float spread) {
            return spread * 0.75F;
        }

        @Override
        public float modifyHipFireSpread(float spread) {
            return spread * 0.55F;
        }

        @Override
        public float modifyProjectileSpread(float spread) {
            return spread * 0.85f;
        }

    };

    public static final IGunModifier SMALL_EXTENDED_MAG = new IGunModifier() {
        @Override
        public double modifyAimDownSightSpeed(double speed) {
            return speed * 0.98F;
        }

        @Override
        public float modifyWeaponWeight() {
            return 0.08F;
        }

        @Override
        public int additionalAmmunition() {
            return 0;
        }
    };

    public static final IGunModifier MEDIUM_EXTENDED_MAG = new IGunModifier() {
        @Override
        public double modifyAimDownSightSpeed(double speed) {
            return speed * 0.95F;
        }

        @Override
        public float modifyWeaponWeight() {
            return 0.13F;
        }

        @Override
        public int additionalAmmunition() {
            return 1;
        }
    };

    public static final IGunModifier LARGE_EXTENDED_MAG = new IGunModifier() {
        @Override
        public double modifyAimDownSightSpeed(double speed) {
            return speed * 0.90F;
        }

        @Override
        public float modifyWeaponWeight() {
            return 0.2F;
        }

        @Override
        public int additionalAmmunition() {
            return 2;
        }
    };

    public static final IGunModifier PISTOL_SILENCER = new IGunModifier() {
        @Override
        public double modifyAimDownSightSpeed(double speed) {
            return speed * 0.84;
        }

        @Override
        public float modifyProjectileSpread(float spread) {
            return spread * 0.9125F;
        }

        @Override
        public float modifyRecoilSmoothening() {
            return 1.30F;
        }

        @Override
        public boolean silencedFire() {
            return true;
        }

        @Override
        public float modifyFireSoundVolume(float volume) {
            return volume * 0.35F;
        }

        @Override
        public double modifyFireSoundRadius(double radius) {
            return radius * 0.125;
        }

        /*@Override
        public float additionalHeadshotDamage() {return 1.55F;}*/

        @Override
        public double modifyMuzzleFlashSize(double size) {
            return size * 0F;
        }

        @Override
        public float additionalWeaponWeight() {
            return 0.375F;
        }
    };

    public static final IGunModifier MUZZLE_BRAKE_MODIFIER = new IGunModifier() {
        @Override
        public float recoilModifier() {
            return 0.75F;
        }

        @Override
        public float modifyFirstShotSpread(float spread) {
            return spread * 0.92F;
        }

        @Override
        public double modifyMuzzleFlashSize(double size) {
            return size * 1.175F;
        }

        @Override
        public double modifyFireSoundRadius(double radius) {
            return radius * 1.3;
        }

        @Override
        public float additionalWeaponWeight() {
            return 0.125F;
        }

        @Override
        public float modifyWeaponWeight() {
            return 0.15F;
        }
    };

    public static final IGunModifier MUZZLE_COMPENSATOR_MODIFIER = new IGunModifier() {
        @Override
        public float horizontalRecoilModifier() {
            return 0.75F;
        }

        @Override
        public double modifyMuzzleFlashSize(double size) {
            return size * 1.175F;
        }

        @Override
        public double modifyFireSoundRadius(double radius) {
            return radius * 1.2;
        }

        @Override
        public float modifyFirstShotSpread(float spread) {
            return spread * 0.95F;
        }

        @Override
        public float additionalWeaponWeight() {
            return 0.125F;
        }

        @Override
        public float modifyWeaponWeight() {
            return 0.10F;
        }
    };

    //////////////////////// SPECIFICS PER WEAPON ////////////////////////
    public static final IGunModifier DE_LISLE_MOD = new IGunModifier() {
        @Override
        public boolean silencedFire() {
            return true;
        }

        @Override
        public float modifyFireSoundVolume(float volume) {
            return volume * 0.175F;
        }

        @Override
        public double modifyFireSoundRadius(double radius) {
            return radius * 0.175;
        }

        @Override
        public double modifyMuzzleFlashSize(double size) {
            return size * 0F;
        }

        @Override
        public double modifyAimDownSightSpeed(double speed) {
            return speed * 0.805;
        }
    };

    public static final IGunModifier M1_GARAND_MOD = new IGunModifier() {
        @Override
        public double modifyFireSoundRadius(double radius) {
            return radius * 1.125;
        }

        @Override
        public double modifyAimDownSightSpeed(double speed) {
            return speed * 0.735;
        }
    };

    public static final IGunModifier SIG_MCX_SPEAR_MOD = new IGunModifier() {
        @Override
        public boolean silencedFire() {
            return true;
        }

        @Override
        public float modifyFireSoundVolume(float volume) {
            return volume * 0.60F;
        }

        @Override
        public double modifyFireSoundRadius(double radius) {
            return radius * 0.275;
        }

        @Override
        public float modifyFirstShotSpread(float spread) {
            return spread * 0.875F;
        }

        @Override
        public float modifyRecoilSmoothening() {
            return 1.225F;
        }

        @Override
        public double modifyMuzzleFlashSize(double size) {
            return size * 0F;
        }

        @Override
        public double modifyAimDownSightSpeed(double speed) {
            return speed * 0.80;
        }

        @Override
        public double additionalProjectileGravity() {
            return 0.015;
        }


    };

    public static final IGunModifier MP9_MOD = new IGunModifier() {
        @Override
        public float modifyFireSoundVolume(float volume) {
            return volume * 0.85F;
        }

        @Override
        public double modifyAimDownSightSpeed(double speed) {
            return speed * 1.15;
        }

        @Override
        public double additionalProjectileGravity() {
            return -0.01;
        }
    };

    public static final IGunModifier UDP_9_MOD = new IGunModifier() {
        @Override
        public double modifyAimDownSightSpeed(double speed) {
            return speed * 1.125;
        }

        @Override
        public float modifyFireSoundVolume(float volume) {
            return volume * 0.90F;
        }
    };

    public static final IGunModifier P90_MOD = new IGunModifier() {
        @Override
        public double modifyAimDownSightSpeed(double speed) {
            return speed * 1.0;
        }

        @Override
        public float modifyFireSoundVolume(float volume) {
            return volume * 0.90F;
        }
    };

    public static final IGunModifier SKS_TAC_MOD = new IGunModifier() {
        @Override
        public double modifyFireSoundRadius(double radius) {
            return radius * 1.125;
        }

        @Override
        public double modifyAimDownSightSpeed(double speed) {
            return speed * 0.9;
        }

        @Override
        public double additionalProjectileGravity() {
            return 0.005;
        }
    };

    public static final IGunModifier M1014_MOD = new IGunModifier() {
        @Override
        public double modifyFireSoundRadius(double radius) {
            return radius * 1.25;
        }

        @Override
        public double modifyAimDownSightSpeed(double speed) {
            return speed * 0.95;
        }
    };
    public static final IGunModifier M249_MOD = new IGunModifier() {
        @Override
        public double modifyAimDownSightSpeed(double speed) {
            return speed * 0.775;
        }

        @Override
        public double additionalProjectileGravity() {
            return 0.015;
        }
    };

    public static final IGunModifier M79_MOD = new IGunModifier() {
        @Override
        public double modifyAimDownSightSpeed(double speed) {
            return speed * 0.75;
        }
    };

    public static final IGunModifier MGL_40MM_MOD = new IGunModifier() {
        @Override
        public double modifyAimDownSightSpeed(double speed) {
            return speed * 0.65;
        }
    };

    public static final IGunModifier RPG7_MOD = new IGunModifier() {
        @Override
        public double modifyAimDownSightSpeed(double speed) {
            return speed * 0.5;
        }
    };

    public static final IGunModifier QBZ_191_MOD = new IGunModifier() {
        @Override
        public double modifyFireSoundRadius(double radius) {
            return radius * 1.15;
        }

        @Override
        public double modifyAimDownSightSpeed(double speed) {
            return speed * 0.90;
        }

        @Override
        public double additionalProjectileGravity() {
            return 0.015;
        }
    };

    public static final IGunModifier AR_MOD = new IGunModifier() {
        @Override
        public double modifyFireSoundRadius(double radius) {
            return radius * 1.05;
        }

        @Override
        public double modifyAimDownSightSpeed(double speed) {
            return speed * 0.95;
        }

        @Override
        public double additionalProjectileGravity() {
            return 0.010;
        }
    };

    public static final IGunModifier STEN_OSS_MOD = new IGunModifier() {
        @Override
        public boolean silencedFire() {
            return true;
        }

        @Override
        public float modifyFireSoundVolume(float volume) {
            return volume * 0.175F;
        }

        @Override
        public double modifyFireSoundRadius(double radius) {
            return radius * 0.205;
        }

        @Override
        public double modifyMuzzleFlashSize(double size) {
            return size * 0F;
        }

        @Override
        public double modifyAimDownSightSpeed(double speed) {
            return speed * 0.85;
        }
    };
    public static final IGunModifier ESPADON_MOD = new IGunModifier() {
        @Override
        public float modifyFireSoundVolume(float volume) {
            return volume * 1.375F;
        }

        @Override
        public double modifyFireSoundRadius(double radius) {
            return radius * 1.205;
        }

        @Override
        public double modifyAimDownSightSpeed(double speed) {
            return speed * 0.80;
        }
    };

    public static final IGunModifier M16A4_MOD = new IGunModifier() {
        @Override
        public double modifyAimDownSightSpeed(double speed) {
            return speed * 0.85;
        }
    };

    public static final IGunModifier FN_FAL_MOD = new IGunModifier() {
        @Override
        public double modifyFireSoundRadius(double radius) {
            return radius * 1.20;
        }

        @Override
        public double modifyAimDownSightSpeed(double speed) {
            return speed * 0.775;
        }

        @Override
        public double additionalProjectileGravity() {
            return 0.010;
        }
    };

    public static final IGunModifier SCAR_H_MOD = new IGunModifier() {
        @Override
        public float modifyFireSoundVolume(float volume) {
            return volume * 1.425F;
        }

        @Override
        public double modifyFireSoundRadius(double radius) {
            return radius * 1.15;
        }

        @Override
        public double modifyAimDownSightSpeed(double speed) {
            return speed * 0.80;
        }

        @Override
        public double additionalProjectileGravity() {
            return 0.015;
        }
    };

    public static final IGunModifier SCAR_MK20_MOD = new IGunModifier() {
        @Override
        public float modifyFireSoundVolume(float volume) {
            return volume * 2.5F;
        }

        @Override
        public double modifyFireSoundRadius(double radius) {
            return radius * 1.25;
        }

        @Override
        public double modifyAimDownSightSpeed(double speed) {
            return speed * 0.75;
        }

        @Override
        public double additionalProjectileGravity() {
            return 0.015;
        }
    };

    public static final IGunModifier HK_G3_MOD = new IGunModifier() {
        @Override
        public float modifyFireSoundVolume(float volume) {
            return volume * 1.25F;
        }

        @Override
        public double modifyFireSoundRadius(double radius) {
            return radius * 1.05;
        }

        @Override
        public double modifyAimDownSightSpeed(double speed) {
            return speed * 0.8;
        }

        @Override
        public double additionalProjectileGravity() {
            return 0.01;
        }
    };

    public static final IGunModifier SCAR_L_MOD = new IGunModifier() {
        @Override
        public float modifyFireSoundVolume(float volume) {
            return volume * 1.125F;
        }

        @Override
        public double modifyFireSoundRadius(double radius) {
            return radius * 1.05;
        }

        @Override
        public double modifyAimDownSightSpeed(double speed) {
            return speed * 0.85;
        }
    };

    public static final IGunModifier MK14_MOD = new IGunModifier() {

        @Override
        public double modifyFireSoundRadius(double radius) {
            return radius * 1.10;
        }

        @Override
        public double modifyAimDownSightSpeed(double speed) {
            return speed * 0.9;
        }

        @Override
        public double additionalProjectileGravity() {
            return 0.015;
        }
    };

    public static final IGunModifier SPR_15_MOD = new IGunModifier() {
        @Override
        public float modifyFireSoundVolume(float volume) {
            return volume * 0.925F;
        }

        @Override
        public double modifyFireSoundRadius(double radius) {
            return radius * 1.15;
        }

        @Override
        public double modifyAimDownSightSpeed(double speed) {
            return speed * 0.9;
        }

        @Override
        public double additionalProjectileGravity() {
            return 0.01;
        }
    };

    public static final IGunModifier MK47_MOD = new IGunModifier() {
        @Override
        public float modifyFireSoundVolume(float volume) {
            return volume * 1.35F;
        }

        @Override
        public double modifyFireSoundRadius(double radius) {
            return radius * 1.25;
        }

        @Override
        public double modifyAimDownSightSpeed(double speed) {
            return speed * 0.885;
        }
    };

    public static final IGunModifier AK47_MOD = new IGunModifier() {
        @Override
        public double modifyFireSoundRadius(double radius) {
            return radius * 1.15;
        }

        @Override
        public double modifyAimDownSightSpeed(double speed) {
            return speed * 0.825;
        }

        @Override
        public double additionalProjectileGravity() {
            return -0.01;
        }
    };

    public static final IGunModifier RPK_MOD = new IGunModifier() {
        @Override
        public double modifyFireSoundRadius(double radius) {
            return radius * 1.25;
        }

        @Override
        public double modifyAimDownSightSpeed(double speed) {
            return speed * 0.85;
        }
    };

    public static final IGunModifier M60_MOD = new IGunModifier() {
        @Override
        public double modifyFireSoundRadius(double radius) {
            return radius * 1.35;
        }

        @Override
        public double modifyAimDownSightSpeed(double speed) {
            return speed * 0.70;
        }

        @Override
        public double additionalProjectileGravity() {
            return 0.015;
        }
    };

    public static final IGunModifier DP28_MOD = new IGunModifier() {
        @Override
        public double modifyFireSoundRadius(double radius) {
            return radius * 1.35;
        }

        @Override
        public double modifyAimDownSightSpeed(double speed) {
            return speed * 0.60;
        }

        @Override
        public double additionalProjectileGravity() {
            return 0.01;
        }
    };

    public static final IGunModifier VECTOR_MOD = new IGunModifier() {
        @Override
        public double modifyFireSoundRadius(double radius) {
            return radius * 0.9;
        }

        @Override
        public double modifyAimDownSightSpeed(double speed) {
            return speed * 0.975;
        }

        @Override
        public double additionalProjectileGravity() {
            return -0.015;
        }
    };

    public static final IGunModifier MICROUZI_MOD = new IGunModifier() {
        @Override
        public float modifyFireSoundVolume(float volume) {
            return volume * 0.75F;
        }

        @Override
        public double modifyFireSoundRadius(double radius) {
            return radius * 0.9;
        }

        @Override
        public double modifyAimDownSightSpeed(double speed) {
            return speed * 1.25;
        }

        @Override
        public double additionalProjectileGravity() {
            return -0.02;
        }
    };

    public static final IGunModifier UZI_MOD = new IGunModifier() {
        @Override
        public double modifyFireSoundRadius(double radius) {
            return radius * 0.9;
        }

        @Override
        public double modifyAimDownSightSpeed(double speed) {
            return speed * 1.20;
        }

        @Override
        public double additionalProjectileGravity() {
            return -0.015;
        }
    };

    public static final IGunModifier SMG_MOD = new IGunModifier() {
        @Override
        public double modifyFireSoundRadius(double radius) {
            return radius * 0.95;
        }

        @Override
        public double modifyAimDownSightSpeed(double speed) {
            return speed * 1.05;
        }

        @Override
        public double additionalProjectileGravity() {
            return -0.005;
        }
    };

    public static final IGunModifier AA_12_MOD = new IGunModifier() {
        @Override
        public double modifyFireSoundRadius(double radius) {
            return radius * 1.15;
        }

        @Override
        public double modifyAimDownSightSpeed(double speed) {
            return speed * 0.75;
        }
    };

    public static final IGunModifier M870_MOD = new IGunModifier() {
        @Override
        public double modifyFireSoundRadius(double radius) {
            return radius * 1.15;
        }

        @Override
        public double modifyAimDownSightSpeed(double speed) {
            return speed * 0.85;
        }
    };

    public static final IGunModifier DEAGLE50_MOD = new IGunModifier() {
        @Override
        public double modifyFireSoundRadius(double radius) {
            return radius * 1.35;
        }

        @Override
        public double modifyAimDownSightSpeed(double speed) {
            return speed * 0.85;
        }

        @Override
        public double additionalProjectileGravity() {
            return -0.015;
        }
    };

    public static final IGunModifier MP5A5_MOD = new IGunModifier() {
        @Override
        public double modifyAimDownSightSpeed(double speed) {
            return speed * 1.125;
        }

        @Override
        public double additionalProjectileGravity() {
            return 0.01;
        }
    };

    public static final IGunModifier M1A1_MOD = new IGunModifier() {
        @Override
        public double modifyAimDownSightSpeed(double speed) {
            return speed * 1.15;
        }

        @Override
        public double additionalProjectileGravity() {
            return -0.015;
        }
    };

    public static final IGunModifier HK416_MOD = new IGunModifier() {
        @Override
        public double modifyAimDownSightSpeed(double speed) {
            return speed * 1.05;
        }

        @Override
        public double additionalProjectileGravity() {
            return 0.015;
        }
    };

    public static final IGunModifier M4A1_MOD = new IGunModifier() {
        @Override
        public double modifyAimDownSightSpeed(double speed) {
            return speed * 0.95;
        }
    };

    public static final IGunModifier MK18_MOD1_MOD = new IGunModifier() {
        @Override
        public double modifyAimDownSightSpeed(double speed) {
            return speed * 1.10;
        }

        @Override
        public double additionalProjectileGravity() {
            return 0.015;
        }

        @Override
        public double modifyFireSoundRadius(double radius) {
            return radius * 0.875;
        }
    };

    public static final IGunModifier TYPE81x_MOD = new IGunModifier() {
        @Override
        public double modifyFireSoundRadius(double radius) {
            return radius * 1.15;
        }

        @Override
        public double additionalProjectileGravity() {
            return -0.02;
        }
    };

    public static final IGunModifier MP7_MOD = new IGunModifier() {
        @Override
        public float modifyFireSoundVolume(float volume) {
            return volume * 0.6F;
        }

        @Override
        public double modifyAimDownSightSpeed(double speed) {
            return speed * 1.25;
        }

        @Override
        public double additionalProjectileGravity() {
            return 0.015;
        }
    };

    public static final IGunModifier M24_MOD = new IGunModifier() {
        @Override
        public double modifyFireSoundRadius(double radius) {
            return radius * 1.45;
        }

        @Override
        public double modifyAimDownSightSpeed(double speed) {
            return speed * 0.85;
        }

        @Override
        public double additionalProjectileGravity() {
            return 0.01;
        }
    };

    public static final IGunModifier AIAWP_MOD = new IGunModifier() {
        @Override
        public double modifyFireSoundRadius(double radius) {
            return radius * 1.5;
        }

        @Override
        public double modifyAimDownSightSpeed(double speed) {
            return speed * 0.75;
        }

        @Override
        public double additionalProjectileGravity() {
            return 0.01;
        }
    };

    public static final IGunModifier KAR98 = new IGunModifier() {
        @Override
        public double modifyFireSoundRadius(double radius) {
            return radius * 1.3;
        }

        @Override
        public double modifyAimDownSightSpeed(double speed) {
            return speed * 0.9;
        }
    };

    public static final IGunModifier MRAD_MOD = new IGunModifier() {
        @Override
        public double modifyFireSoundRadius(double radius) {
            return radius * 1.2;
        }

        @Override
        public double modifyAimDownSightSpeed(double speed) {
            return speed * 0.80;
        }

        @Override
        public double additionalProjectileGravity() {
            return 0.015;
        }
    };

    public static final IGunModifier M82A2_MOD = new IGunModifier() {
        @Override
        public double modifyFireSoundRadius(double radius) {
            return radius * 1.75;
        }

        @Override
        public double modifyAimDownSightSpeed(double speed) {
            return speed * 0.60;
        }

        @Override
        public double additionalProjectileGravity() {
            return 0.02;
        }
    };

    public static final IGunModifier GLOCK17_MOD = new IGunModifier() {
        @Override
        public double modifyFireSoundRadius(double radius) {
            return radius * 0.85;
        }

        @Override
        public double modifyAimDownSightSpeed(double speed) {
            return speed * 1.35;
        }

        @Override
        public double additionalProjectileGravity() {
            return 0.01;
        }
    };

    public static final IGunModifier TEC_9_MOD = new IGunModifier() {
        @Override
        public double modifyFireSoundRadius(double radius) {
            return radius * 0.85;
        }

        @Override
        public double modifyAimDownSightSpeed(double speed) {
            return speed * 1.5;
        }

        @Override
        public double additionalProjectileGravity() {
            return -0.01;
        }
    };

    public static final IGunModifier TTI34_MOD = new IGunModifier() {
        @Override
        public double modifyAimDownSightSpeed(double speed) {
            return speed * 1.25;
        }

        @Override
        public double additionalProjectileGravity() {
            return 0.015;
        }
    };

    public static final IGunModifier M1911_MOD = new IGunModifier() {
        @Override
        public double modifyFireSoundRadius(double radius) {
            return radius * 1.15;
        }

        @Override
        public double modifyAimDownSightSpeed(double speed) {
            return speed * 1.20;
        }

        @Override
        public double additionalProjectileGravity() {
            return 0.01;
        }
    };

    public static final IGunModifier STI2011_MOD = new IGunModifier() {
        @Override
        public double modifyFireSoundRadius(double radius) {
            return radius * 1.15;
        }

        @Override
        public double modifyAimDownSightSpeed(double speed) {
            return speed * 1.225;
        }

        @Override
        public double additionalProjectileGravity() {
            return 0.015;
        }
    };

    public static final IGunModifier COLT_PYTHON_MOD = new IGunModifier() {
        @Override
        public double modifyFireSoundRadius(double radius) {
            return radius * 1.25;
        }

        @Override
        public double modifyAimDownSightSpeed(double speed) {
            return speed * 1.25;
        }

        @Override
        public double additionalProjectileGravity() {
            return 0.0125;
        }
    };

    public static final IGunModifier PISTOL_MOD = new IGunModifier() {
        @Override
        public double modifyAimDownSightSpeed(double speed) {
            return speed * 1.15;
        }

        @Override
        public double additionalProjectileGravity() {
            return 0.01;
        }
    };

    //////////////////////// SPECIFICS PER SKIN ////////////////////////

    public static final IGunModifier SKIN_AK_SPENT_BULLET = new IGunModifier() {
        @Override
        public String additionalSkin() {
            return "SPENT_BULLET";
        }
    };
    public static final IGunModifier SKIN_MP9_THUNDER = new IGunModifier() {
        @Override
        public String additionalSkin() {
            return "THUNDER";
        }
    };

    public static final IGunModifier SKIN_BLACK = new IGunModifier() {
        @Override
        public String additionalSkin() {
            return "BLACK";
        }
    };
    public static final IGunModifier SKIN_BLUE = new IGunModifier() {
        @Override
        public String additionalSkin() {
            return "BLUE";
        }
    };
    public static final IGunModifier SKIN_BROWN = new IGunModifier() {
        @Override
        public String additionalSkin() {
            return "BROWN";
        }
    };
    public static final IGunModifier SKIN_DARK_BLUE = new IGunModifier() {
        @Override
        public String additionalSkin() {
            return "DARK_BLUE";
        }
    };
    public static final IGunModifier SKIN_DARK_GREEN = new IGunModifier() {
        @Override
        public String additionalSkin() {
            return "DARK_GREEN";
        }
    };
    public static final IGunModifier SKIN_GRAY = new IGunModifier() {
        @Override
        public String additionalSkin() {
            return "GRAY";
        }
    };
    public static final IGunModifier SKIN_GREEN = new IGunModifier() {
        @Override
        public String additionalSkin() {
            return "GREEN";
        }
    };
    public static final IGunModifier SKIN_JADE = new IGunModifier() {
        @Override
        public String additionalSkin() {
            return "JADE";
        }
    };
    public static final IGunModifier SKIN_LIGHT_GRAY = new IGunModifier() {
        @Override
        public String additionalSkin() {
            return "LIGHT_GRAY";
        }
    };
    public static final IGunModifier SKIN_MAGENTA = new IGunModifier() {
        @Override
        public String additionalSkin() {
            return "MAGENTA";
        }
    };
    public static final IGunModifier SKIN_ORANGE = new IGunModifier() {
        @Override
        public String additionalSkin() {
            return "ORANGE";
        }
    };
    public static final IGunModifier SKIN_PINK = new IGunModifier() {
        @Override
        public String additionalSkin() {
            return "PINK";
        }
    };
    public static final IGunModifier SKIN_PURPLE = new IGunModifier() {
        @Override
        public String additionalSkin() {
            return "PURPLE";
        }
    };
    public static final IGunModifier SKIN_RED = new IGunModifier() {
        @Override
        public String additionalSkin() {
            return "RED";
        }
    };
    public static final IGunModifier SKIN_SAND = new IGunModifier() {
        @Override
        public String additionalSkin() {
            return "SAND";
        }
    };
    public static final IGunModifier SKIN_WHITE = new IGunModifier() {
        @Override
        public String additionalSkin() {
            return "WHITE";
        }
    };
}
