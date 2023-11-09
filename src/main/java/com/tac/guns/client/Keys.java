package com.tac.guns.client;

import com.tac.guns.Config;
import com.tac.guns.client.TacKeyMapping.TacKeyBuilder;
import net.minecraft.client.GameSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraft.client.util.InputMappings.Input;
import net.minecraft.client.util.InputMappings.Type;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

import net.minecraftforge.client.settings.KeyModifier;

@OnlyIn(Dist.CLIENT)
public final class Keys
{
    private static class MouseKeyBinding extends KeyBinding
    {
        private MouseKeyBinding( String description, Input mouse_button )
        {
            super(
                description,
                GunConflictContext.IN_GAME_HOLDING_WEAPON,
                KeyModifier.NONE,
                mouse_button,
                "key.categories.tac"
            );
        }
        
        @Override
        public boolean conflicts( KeyBinding binding )
        {
            // No conflict with vanilla attack and item use key binding.
            final GameSettings settings = Minecraft.getInstance().gameSettings;
            return(
                binding != settings.keyBindAttack
                && binding != settings.keyBindUseItem
                && super.conflicts( binding )
            );
        }
    }
    
    public static final KeyBinding
            PULL_TRIGGER = Minecraft.getInstance().gameSettings.keyBindAttack,
            AIM_HOLD = Minecraft.getInstance().gameSettings.keyBindUseItem,
            AIM_TOGGLE = AIM_HOLD;
    
    public static final TacKeyMapping
        RELOAD = new TacKeyBuilder( "key.tac.reload" ).withKeyboardKey( GLFW.GLFW_KEY_R ).buildAndRegis(),
        UNLOAD = new TacKeyBuilder( "key.tac.unload" ).withKeyboardKey( GLFW.GLFW_KEY_R ).withKeyModifier( KeyModifier.ALT ).buildAndRegis(),
        ATTACHMENTS = new TacKeyBuilder( "key.tac.attachments" ).withKeyboardKey( GLFW.GLFW_KEY_Z ).buildAndRegis(),
        FIRE_SELECT = new TacKeyBuilder( "key.tac.fireSelect" ).withKeyboardKey( GLFW.GLFW_KEY_G ).buildAndRegis(),
        INSPECT = new TacKeyBuilder( "key.tac.inspect" ).withKeyboardKey( GLFW.GLFW_KEY_H ).buildAndRegis(),
        SIGHT_SWITCH = new TacKeyBuilder( "key.tac.sight_switch" ).withKeyboardKey( GLFW.GLFW_KEY_V ).buildAndRegis(),
        ACTIVATE_SIDE_RAIL = new TacKeyBuilder( "key.tac.activeSideRail" ).withKeyboardKey( GLFW.GLFW_KEY_B ).buildAndRegis();

    public static final TacKeyMapping[] KEYS_VALUE = {RELOAD, UNLOAD, ATTACHMENTS, FIRE_SELECT, INSPECT, SIGHT_SWITCH, ACTIVATE_SIDE_RAIL};

    public static boolean noConflict(TacKeyMapping key) {
        for (TacKeyMapping k : KEYS_VALUE) {
            if (k == key) {
                if (!k.getKeyModifier().isActive(null))
                    return false;
            } else {
                if (k.isKeyDown() && k.getKeyModifier().isActive(null))
                    return false;
            }
        }
        return true;
    }

    public static final KeyBinding MORE_INFO_HOLD =
        new TacKeyBuilder( "key.tac.more_info_hold" )
            .withKeyboardKey( GLFW.GLFW_KEY_LEFT_SHIFT )
            .withConflictContext( KeyConflictContext.GUI )
            .buildAndRegis();
    
    public static KeyBinding
        SHIFTY = null,
        CONTROLLY = null,
        ALTY = null,
        SHIFTYR = null,
        CONTROLLYR = null,
        ALTYR = null,
        SIZE_OPT = null,
        P = null,
        L = null,
        O = null,
        K = null,
        M = null,
        I = null,
        J = null,
        N = null,
        UP = null,
        RIGHT = null,
        LEFT = null,
        DOWN = null;
    
    static
    {
        if ( Config.COMMON.development.enableTDev.get() )
        {
            SHIFTY = new TacKeyBuilder( "key.tac.ss" ).withKeyboardKey( GLFW.GLFW_KEY_LEFT_SHIFT ).buildAndRegis();
            CONTROLLY = new TacKeyBuilder( "key.tac.cc" ).withKeyboardKey( GLFW.GLFW_KEY_LEFT_CONTROL ).buildAndRegis();
            ALTY = new TacKeyBuilder( "key.tac.aa" ).withKeyboardKey( GLFW.GLFW_KEY_LEFT_ALT ).buildAndRegis();
            SHIFTYR = new TacKeyBuilder( "key.tac.ssr" ).withKeyboardKey( GLFW.GLFW_KEY_RIGHT_SHIFT ).buildAndRegis();
            CONTROLLYR = new TacKeyBuilder( "key.tac.ccr" ).withKeyboardKey( GLFW.GLFW_KEY_RIGHT_CONTROL ).buildAndRegis();
            ALTYR = new TacKeyBuilder("key.tac.aar" ).withKeyboardKey( GLFW.GLFW_KEY_RIGHT_ALT ).buildAndRegis();
            SIZE_OPT = new TacKeyBuilder( "key.tac.sizer" ).withKeyboardKey( GLFW.GLFW_KEY_PERIOD ).buildAndRegis();
            P = new TacKeyBuilder( "key.tac.p" ).withKeyboardKey( GLFW.GLFW_KEY_P ).buildAndRegis();
            L = new TacKeyBuilder( "key.tac.l" ).withKeyboardKey( GLFW.GLFW_KEY_L ).buildAndRegis();
            O = new TacKeyBuilder( "key.tac.o" ).withKeyboardKey( GLFW.GLFW_KEY_O ).buildAndRegis();
            K = new TacKeyBuilder( "key.tac.k" ).withKeyboardKey( GLFW.GLFW_KEY_K ).buildAndRegis();
            M = new TacKeyBuilder( "key.tac.m" ).withKeyboardKey( GLFW.GLFW_KEY_M ).buildAndRegis();
            I = new TacKeyBuilder( "key.tac.i" ).withKeyboardKey( GLFW.GLFW_KEY_I ).buildAndRegis();
            J = new TacKeyBuilder( "key.tac.j" ).withKeyboardKey( GLFW.GLFW_KEY_J ).buildAndRegis();
            N = new TacKeyBuilder( "key.tac.n" ).withKeyboardKey( GLFW.GLFW_KEY_N ).buildAndRegis();
            UP = new TacKeyBuilder( "key.tac.bbb" ).withKeyboardKey( GLFW.GLFW_KEY_UP ).buildAndRegis();
            RIGHT = new TacKeyBuilder( "key.tac.vvv" ).withKeyboardKey( GLFW.GLFW_KEY_RIGHT ).buildAndRegis();
            LEFT = new TacKeyBuilder( "key.tac.ccc" ).withKeyboardKey( GLFW.GLFW_KEY_LEFT ).buildAndRegis();
            DOWN = new TacKeyBuilder( "key.tac.zzz" ).withKeyboardKey( GLFW.GLFW_KEY_DOWN ).buildAndRegis();
        }
    }
    
    private Keys() { }
}
