package com.tac.guns.client;

import net.minecraft.client.util.InputMappings;
import net.minecraft.client.util.InputMappings.Input;
import net.minecraft.client.util.InputMappings.Type;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.client.settings.IKeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;

import java.util.LinkedList;

@OnlyIn( Dist.CLIENT )
public final class TacKeyMapping extends KeyBinding
{
    private final LinkedList< Runnable > press_callbacks = new LinkedList<>();
    private boolean is_down;
    private boolean isPressed = false;

    public TacKeyMapping(
            String description,
            IKeyConflictContext keyConflictContext,
            KeyModifier keyModifier,
            Input keyCode,
            String category
    ) { super( description, keyConflictContext, keyModifier, keyCode, category ); }

    public void addPressCallback( Runnable callback ) {
        this.press_callbacks.add( callback );
    }

    @Override
    public boolean isKeyDown() {
        return this.isPressed;
    }

    @Override
    public void setPressed( boolean is_down )
    {
        if ( is_down && !this.is_down ) {
            this.press_callbacks.forEach( Runnable::run );
        }
        this.isPressed = is_down;

        super.setPressed( is_down );
    }

    public static final class TacKeyBuilder
    {
        private final String description;
        private IKeyConflictContext conflict_context = GunConflictContext.IN_GAME_HOLDING_WEAPON;
        private KeyModifier modifier = KeyModifier.NONE;
        private Input key = InputMappings.INPUT_INVALID;

        public TacKeyBuilder( String description ) {
            this.description = description;
        }

        public TacKeyBuilder withConflictContext( IKeyConflictContext context )
        {
            this.conflict_context = context;
            return this;
        }

        public TacKeyBuilder withKeyboardKey( int key_code )
        {
            this.key = Type.KEYSYM.getOrMakeInput( key_code );
            return this;
        }

        public TacKeyBuilder withKeyModifier( KeyModifier modifier )
        {
            this.modifier = modifier;
            return this;
        }

        public TacKeyMapping buildAndRegis()
        {
            final TacKeyMapping kb = new TacKeyMapping(
                    this.description,
                    this.conflict_context,
                    this.modifier,
                    this.key,
                    "key.categories.tac"
            );
            ClientRegistry.registerKeyBinding( kb );
            return kb;
        }
    }
}