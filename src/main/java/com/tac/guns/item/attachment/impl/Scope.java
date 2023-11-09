package com.tac.guns.item.attachment.impl;

import com.tac.guns.client.handler.AimingHandler;
import com.tac.guns.interfaces.IGunModifier;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * An attachment class related to scopes. Scopes need to at least specify the additional zoom (or fov)
 * they provide and the y-offset to the center of the scope for them to render correctly. Use
 * {@link #create(ScopeZoomData[], double, double, String, IGunModifier...)} to create an get.
 * <p>
 * Author: Forked from MrCrayfish, continued by Timeless devs
 */
public class Scope extends Attachment
{
    //private float additionalZoom;
    private double centerOffset;
    private boolean stable = false;
    private double stabilityOffset = 0d;
    private double viewFinderOffset;
    private double viewFinderOffsetSpecial;

    private double viewFinderOffsetDR;
    private double viewFinderOffsetSpecialDR;

    private ScopeZoomData[] zoomData;

    public String getTagName() {return tagName;}
    public void setTagName(String tagName) {this.tagName = tagName;}
    private String tagName;
    private Scope(ScopeZoomData[] additionalZoom, double centerOffset, double stabilityOffset, String tagName, IGunModifier... modifier)
    {
        super(modifier);
        this.zoomData = additionalZoom;
        this.centerOffset = centerOffset;
        this.stabilityOffset = stabilityOffset;
        this.tagName = tagName;
    }

    private Scope(ScopeZoomData[] additionalZoom, double centerOffset, double stabilityOffset, String tagName, ResourceLocation modifier)
    {
        super(modifier);
        this.zoomData = additionalZoom;
        this.centerOffset = centerOffset;
        this.stabilityOffset = stabilityOffset;
        this.tagName = tagName;
    }

    /**
     * Marks this scope to allow it to be stabilised while using a controller. This is essentially
     * holding your breath while looking down the sight.
     */
    public void stabilise()
    {
        this.stable = true;
    }

    /**
     * Sets the offset distance from the camera to the view finder
     *
     * @param offset the view finder offset
     * @return this scope get
     */
    public Scope viewFinderOffset(double offset)
    {
        this.viewFinderOffset = offset;
        return this;
    }
    public Scope viewFinderOffsetSpecial(double offset)
    {
        this.viewFinderOffsetSpecial = offset;
        return this;
    }

    /**
     * Sets the offset distance from the camera to the view finder when Double Render is enabled
     *
     * @param offset the view finder offset when Double Render is enabled
     * @return this scope
     */
    public Scope viewFinderOffsetDR(double offset)
    {
        this.viewFinderOffsetDR = offset;
        return this;
    }
    public Scope viewFinderOffsetSpecialDR(double offset)
    {
        this.viewFinderOffsetSpecialDR = offset;
        return this;
    }

    /**
     * Gets the amount of additional zoom (or reduced fov) this scope provides
     *
     * @return the scopes additional zoom
     */
    @OnlyIn(Dist.CLIENT)
    public ScopeZoomData getAdditionalZoom()
    {
        if(this.zoomData == null || AimingHandler.get() == null)
            return new ScopeZoomData(0,0); // Null, loader might attempt to hit scope data when aimed is detected before init
        if(this.zoomData.length <= AimingHandler.get().getCurrentScopeZoomIndex())
        {
            AimingHandler.get().resetCurrentScopeZoomIndex();
            return this.zoomData[AimingHandler.get().getCurrentScopeZoomIndex()];//new ScopeZoomData(0, 0); // Skip Null
        }
        return this.zoomData[AimingHandler.get().getCurrentScopeZoomIndex()];
    }

    /**
     * Gets the offset to the center of the scope. Used to render scope cross hair exactly in the
     * middle of the screen.
     *
     * @return the scope center offset
     */
    @OnlyIn(Dist.CLIENT)
    public double getCenterOffset()
    {
        return this.centerOffset+0.225;
    }

    /**
     * @return If this scope can be stabilised
     */
    @OnlyIn(Dist.CLIENT)
    public boolean isStable()
    {
        return this.stable;
    }

    /**
     * @return The view finder offset of this scope
     */
    @OnlyIn(Dist.CLIENT)
    public double getViewFinderOffset()
    {
        return this.viewFinderOffset;
    }
    /**
     * @return The view finder offset of this scope if gameplay enchanced is chosen
     */
    @OnlyIn(Dist.CLIENT)
    public double getViewFinderOffsetSpecial()
    {
        return this.viewFinderOffsetSpecial;
    }
    /**
     * @return The view finder offset of this scope and Double Render is enabled
     */

    @OnlyIn(Dist.CLIENT)
    public double getViewFinderOffsetDR()
    {
        return this.viewFinderOffsetDR;
    }
    /**
     * @return The view finder offset of this scope if gameplay enchanced is chosen and Double Render is enabled
     */
    @OnlyIn(Dist.CLIENT)
    public double getViewFinderOffsetSpecialDR()
    {
        return this.viewFinderOffsetSpecialDR;
    }
    /**
     * @return The view finder offset of this scope
     */
    @OnlyIn(Dist.CLIENT)
    public double getStabilityOffset()
    {
        return this.stabilityOffset;
    }

    /**
     * Creates a scope get
     *
     * @param additionalZoom the additional zoom this scope provides
     * @param centerOffset   the length to the center of the view finder from the base of the scope model in pixels
     * @param modifiers      an array of gun modifiers
     * @return a scope get
     */
    public static Scope create(ScopeZoomData[] additionalZoom, double centerOffset, double stabilityOffset, String tagName, IGunModifier... modifiers)
    {
        return new Scope(additionalZoom, centerOffset, stabilityOffset, tagName, modifiers);
    }

    public static Scope create(ScopeZoomData[] additionalZoom, double centerOffset, double stabilityOffset, String tagName, ResourceLocation modifier)
    {
        return new Scope(additionalZoom, centerOffset, stabilityOffset, tagName, modifier);
    }
}
