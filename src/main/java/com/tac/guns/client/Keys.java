package com.tac.guns.client;

import com.tac.guns.Config;
import gsf.kbp.client.api.PatchedKeyBinding;
import net.minecraft.client.GameSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraft.client.util.InputMappings.Input;
import net.minecraft.client.util.InputMappings.Type;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.glfw.GLFW;

import java.util.Collections;
import java.util.Set;

@OnlyIn(Dist.CLIENT)
public final class Keys
{
    private static final class Builder extends PatchedKeyBinding.Builder
    {
        private Builder( String description )
        {
            super( description );
            
            this.category = "key.categories.tac";
            this.conflict_context = GunConflictContext.IN_GAME_HOLDING_WEAPON;
        }
    }
    
    private static class MouseKeyBinding extends PatchedKeyBinding
    {
        private MouseKeyBinding( String description, Input mouse_button )
        {
            super(
                description,
                GunConflictContext.IN_GAME_HOLDING_WEAPON,
                mouse_button,
                Collections.emptySet(),
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
    
    public static final PatchedKeyBinding
        PULL_TRIGGER = new MouseKeyBinding(
            "key.tac.pull_trigger",
            Type.MOUSE.getOrMakeInput( GLFW.GLFW_MOUSE_BUTTON_LEFT )
        ),
        AIM_TOGGLE = new MouseKeyBinding(
            "key.tac.aim_toggle",
            InputMappings.INPUT_INVALID
        ) {
            @Override
            public void setKeyAndCombinations( Input key, Set< Input > combinations )
            {
                if ( key != InputMappings.INPUT_INVALID ) {
                    AIM_HOLD.setKeyAndCombinations( InputMappings.INPUT_INVALID, Collections.emptySet() );
                }
                
                super.setKeyAndCombinations( key, combinations );
            }
        },
        AIM_HOLD = new MouseKeyBinding(
            "key.tac.aim_hold",
            Type.MOUSE.getOrMakeInput( GLFW.GLFW_MOUSE_BUTTON_RIGHT )
        ) {
            @Override
            public void setKeyAndCombinations( Input key, Set< Input > combinations )
            {
                if ( key != InputMappings.INPUT_INVALID ) {
                    AIM_TOGGLE.setKeyAndCombinations( InputMappings.INPUT_INVALID, Collections.emptySet() );
                }
                
                super.setKeyAndCombinations( key, combinations );
            }
        };
    static
    {
        ClientRegistry.registerKeyBinding( PULL_TRIGGER );
        ClientRegistry.registerKeyBinding( AIM_HOLD );
        ClientRegistry.registerKeyBinding( AIM_TOGGLE );
    }
    
    public static final PatchedKeyBinding
        RELOAD = new Builder( "key.tac.reload" ).withKeyboardKey( GLFW.GLFW_KEY_R ).buildAndRegis(),
        UNLOAD = new Builder( "key.tac.unload" ).withKeyboardKey( GLFW.GLFW_KEY_R ).withKeyboardCombinations( GLFW.GLFW_KEY_LEFT_ALT ).buildAndRegis(),
        ATTACHMENTS = new Builder( "key.tac.attachments" ).withKeyboardKey( GLFW.GLFW_KEY_Z ).buildAndRegis(),
        FIRE_SELECT = new Builder( "key.tac.fireSelect" ).withKeyboardKey( GLFW.GLFW_KEY_G ).buildAndRegis(),
        INSPECT = new Builder( "key.tac.inspect" ).withKeyboardKey( GLFW.GLFW_KEY_H ).buildAndRegis(),
        SIGHT_SWITCH = new Builder( "key.tac.sight_switch" ).withKeyboardKey( GLFW.GLFW_KEY_V ).buildAndRegis(),
        ACTIVATE_SIDE_RAIL = new Builder( "key.tac.activeSideRail" ).withKeyboardKey( GLFW.GLFW_KEY_B ).buildAndRegis();
    
    public static final PatchedKeyBinding MORE_INFO_HOLD =
        new Builder( "key.tac.more_info_hold" )
            .withKeyboardKey( GLFW.GLFW_KEY_LEFT_SHIFT )
            .withConflictContext( KeyConflictContext.GUI )
            .buildAndRegis();
    
    public static PatchedKeyBinding
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
            SHIFTY = new Builder( "key.tac.ss" ).withKeyboardKey( GLFW.GLFW_KEY_LEFT_SHIFT ).buildAndRegis();
            CONTROLLY = new Builder( "key.tac.cc" ).withKeyboardKey( GLFW.GLFW_KEY_LEFT_CONTROL ).buildAndRegis();
            ALTY = new Builder( "key.tac.aa" ).withKeyboardKey( GLFW.GLFW_KEY_LEFT_ALT ).buildAndRegis();
            SHIFTYR = new Builder( "key.tac.ssr" ).withKeyboardKey( GLFW.GLFW_KEY_RIGHT_SHIFT ).buildAndRegis();
            CONTROLLYR = new Builder( "key.tac.ccr" ).withKeyboardKey( GLFW.GLFW_KEY_RIGHT_CONTROL ).buildAndRegis();
            ALTYR = new Builder("key.tac.aar" ).withKeyboardKey( GLFW.GLFW_KEY_RIGHT_ALT ).buildAndRegis();
            SIZE_OPT = new Builder( "key.tac.sizer" ).withKeyboardKey( GLFW.GLFW_KEY_PERIOD ).buildAndRegis();
            P = new Builder( "key.tac.p" ).withKeyboardKey( GLFW.GLFW_KEY_P ).buildAndRegis();
            L = new Builder( "key.tac.l" ).withKeyboardKey( GLFW.GLFW_KEY_L ).buildAndRegis();
            O = new Builder( "key.tac.o" ).withKeyboardKey( GLFW.GLFW_KEY_O ).buildAndRegis();
            K = new Builder( "key.tac.k" ).withKeyboardKey( GLFW.GLFW_KEY_K ).buildAndRegis();
            M = new Builder( "key.tac.m" ).withKeyboardKey( GLFW.GLFW_KEY_M ).buildAndRegis();
            I = new Builder( "key.tac.i" ).withKeyboardKey( GLFW.GLFW_KEY_I ).buildAndRegis();
            J = new Builder( "key.tac.j" ).withKeyboardKey( GLFW.GLFW_KEY_J ).buildAndRegis();
            N = new Builder( "key.tac.n" ).withKeyboardKey( GLFW.GLFW_KEY_N ).buildAndRegis();
            UP = new Builder( "key.tac.bbb" ).withKeyboardKey( GLFW.GLFW_KEY_UP ).buildAndRegis();
            RIGHT = new Builder( "key.tac.vvv" ).withKeyboardKey( GLFW.GLFW_KEY_RIGHT ).buildAndRegis();
            LEFT = new Builder( "key.tac.ccc" ).withKeyboardKey( GLFW.GLFW_KEY_LEFT ).buildAndRegis();
            DOWN = new Builder( "key.tac.zzz" ).withKeyboardKey( GLFW.GLFW_KEY_DOWN ).buildAndRegis();
        }
    }
    
    private Keys() { }
}
