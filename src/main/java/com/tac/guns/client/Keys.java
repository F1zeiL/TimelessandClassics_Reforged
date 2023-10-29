package com.tac.guns.client;

import com.tac.guns.Config;
import gsf.kbp.client.api.PatchedKeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraft.client.util.InputMappings.Input;
import net.minecraft.client.util.InputMappings.Type;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.glfw.GLFW;

import java.util.Arrays;
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
    
    public static final PatchedKeyBinding
        PULL_TRIGGER = new Builder( "key.tac.pull_trigger" ).withMouseButton( GLFW.GLFW_MOUSE_BUTTON_LEFT ).buildAndRegis();
    
    public static final PatchedKeyBinding
        AIM_TOGGLE = new PatchedKeyBinding(
        "key.tac.aim_toggle",
        GunConflictContext.IN_GAME_HOLDING_WEAPON,
        InputMappings.INPUT_INVALID,
        Collections.emptySet(),
        "key.categories.tac"
    ) {
        @Override
        public void setKeyAndCombinations( Input key, Set< Input > combinations )
        {
            if ( key != InputMappings.INPUT_INVALID ) {
                AIM_HOLD.setKeyAndCombinations( InputMappings.INPUT_INVALID, Collections.emptySet() );
            }
            
            super.setKeyAndCombinations( key, combinations );
        }
        
        @Override
        public void setKeyModifierAndCode( KeyModifier keyModifier, Input keyCode )
        {
            if ( keyCode != InputMappings.INPUT_INVALID ) {
                AIM_HOLD.setKeyAndCombinations( InputMappings.INPUT_INVALID, Collections.emptySet() );
            }
            
            super.setKeyModifierAndCode( keyModifier, keyCode );
        }
    };
    
    public static final PatchedKeyBinding AIM_HOLD = new PatchedKeyBinding(
        "key.tac.aim_hold",
        GunConflictContext.IN_GAME_HOLDING_WEAPON,
        Type.MOUSE.getOrMakeInput( GLFW.GLFW_MOUSE_BUTTON_RIGHT ),
        Collections.emptySet(),
        "key.categories.tac"
    ) {
        @Override
        public void setKeyAndCombinations( Input key, Set< Input > combinations )
        {
            if ( key != InputMappings.INPUT_INVALID ) {
                AIM_TOGGLE.setKeyAndCombinations( InputMappings.INPUT_INVALID, Collections.emptySet() );
            }
            
            super.setKeyAndCombinations( key, combinations );
        }
        
        @Override
        public void setKeyModifierAndCode( KeyModifier keyModifier, Input keyCode )
        {
            if ( keyCode != InputMappings.INPUT_INVALID ) {
                AIM_TOGGLE.setKeyAndCombinations( InputMappings.INPUT_INVALID, Collections.emptySet() );
            }
            
            super.setKeyModifierAndCode( keyModifier, keyCode );
        }
    };
    static
    {
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
            Arrays.asList(
                SHIFTY = new Builder( "key.tac.ss" ).withKeyboardKey( GLFW.GLFW_KEY_LEFT_SHIFT ).build(),
                CONTROLLY = new Builder( "key.tac.cc" ).withKeyboardKey( GLFW.GLFW_KEY_LEFT_CONTROL ).build(),
                ALTY = new Builder( "key.tac.aa" ).withKeyboardKey( GLFW.GLFW_KEY_LEFT_ALT ).build(),
                SHIFTYR = new Builder( "key.tac.ssr" ).withKeyboardKey( GLFW.GLFW_KEY_RIGHT_SHIFT ).build(),
                CONTROLLYR = new Builder( "key.tac.ccr" ).withKeyboardKey( GLFW.GLFW_KEY_RIGHT_CONTROL ).build(),
                ALTYR = new Builder("key.tac.aar" ).withKeyboardKey( GLFW.GLFW_KEY_RIGHT_ALT ).build(),
                SIZE_OPT = new Builder( "key.tac.sizer" ).withKeyboardKey( GLFW.GLFW_KEY_PERIOD ).build(),
                P = new Builder( "key.tac.p" ).withKeyboardKey( GLFW.GLFW_KEY_P ).build(),
                L = new Builder( "key.tac.l" ).withKeyboardKey( GLFW.GLFW_KEY_L ).build(),
                O = new Builder( "key.tac.o" ).withKeyboardKey( GLFW.GLFW_KEY_O ).build(),
                K = new Builder( "key.tac.k" ).withKeyboardKey( GLFW.GLFW_KEY_K ).build(),
                M = new Builder( "key.tac.m" ).withKeyboardKey( GLFW.GLFW_KEY_M ).build(),
                I = new Builder( "key.tac.i" ).withKeyboardKey( GLFW.GLFW_KEY_I ).build(),
                J = new Builder( "key.tac.j" ).withKeyboardKey( GLFW.GLFW_KEY_J ).build(),
                N = new Builder( "key.tac.n" ).withKeyboardKey( GLFW.GLFW_KEY_N ).build(),
                UP = new Builder( "key.tac.bbb" ).withKeyboardKey( GLFW.GLFW_KEY_UP ).build(),
                RIGHT = new Builder( "key.tac.vvv" ).withKeyboardKey( GLFW.GLFW_KEY_RIGHT ).build(),
                LEFT = new Builder( "key.tac.ccc" ).withKeyboardKey( GLFW.GLFW_KEY_LEFT ).build(),
                DOWN = new Builder( "key.tac.zzz" ).withKeyboardKey( GLFW.GLFW_KEY_DOWN ).build()
            ).forEach( ClientRegistry::registerKeyBinding );
        }
    }
    
    private Keys() { }
}
