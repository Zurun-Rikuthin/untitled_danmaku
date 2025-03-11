package com.rikuthin.entities;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import com.rikuthin.graphics.ImageManager;
import com.rikuthin.interfaces.Renderable;

/**
 * The base class for all entities in the game. This class represents any object
 * that has behavior (e.g., movement, animation, or interaction with other
 * entities) in the game world.
 */
public abstract class Entity implements Renderable {

    /**
     * The parent {@link JPanel} to which the entity belongs.
     */
    protected JPanel panel;
    /**
     * The x-coordinate of the entity (left-most edge).
     */
    protected int x;
    /**
     * The y-coordinate of the entity (upper-most edge).
     */
    protected int y;
    /**
     * The URL for the entity's sprite;
     */
    protected String spriteUrl;
    /**
     * The entity's sprite;
     */
    protected BufferedImage sprite;
    /**
     * Width of the entity's sprite in pixels.
     */
    protected int spriteWidth;
    /**
     * Height of the entity's sprite in pixels.
     */
    protected int spriteHeight;
    /**
     * The x-coordinate of the entity's hitbox (left-most edge).
     */
    protected int hitboxX;
    /**
     * The y-coordinate of the entity's hitbox (upper-most edge).
     */
    protected int hitboxY;
    /**
     * Width of the entity's hitbox in pixels. Default value is the same as
     * spriteWidth.
     */
    protected int hitboxWidth;
    /**
     * Height of the entity's hitbox in pixels. Default value is the same as
     * spriteHeight.
     */
    protected int hitboxHeight;

    /**
     * Constructor to initialize the entity's parent panel, position (upper
     * left-most corner of sprite), and sprite.
     *
     * The hitbox position defaults to the entity's position.
     *
     * If a spriteUrl is provided and the sprite can be successfully loaded,
     * then the sprite height/width are set accordingly, with the hitbox
     * height/width being set to the same values.
     *
     * If no spriteURL is provided or the sprite cannot be loaded, then the
     * sprite height/width and hitbox height/width default to 0;
     *
     * @param panel The parent {@link JPanel} to which the entity belongs.
     * @param x The initial x-coordinate of the entity.
     * @param y The initial y-coordinate of the entity.
     * @param sprite The URL for the entity's sprite.
     */
    protected Entity(final JPanel panel, final int x, final int y, final String spriteUrl) {
        this.panel = panel;
        setPosition(x, y);
        setHitboxPosition(x, y);
        setSprite(spriteUrl);
        setHitboxSize(spriteWidth, spriteHeight);
    }

    /**
     * Sets the position of the upper-left-most corner of the entity's hitbox.
     *
     * @param x The x-coordinate of the entity's hitbox.
     * @param y The y-coordinate of the entity's hitbox.
     */
    public final void setHitboxPosition(final int x, final int y) {
        this.hitboxX = x;
        this.hitboxY = y;
    }

    /**
     * Sets the position of the upper-left-most corner of the entity's hitbox.
     *
     * @param width The width of the entity's hitbox in pixels.
     * @param height The height of the entity's hitbox in pixels.
     */
    public final void setHitboxSize(final int width, final int height) {
        this.hitboxWidth = width;
        this.hitboxHeight = height;
    }

    /**
     * Sets the position of the upper-left-most corner of the entity.
     *
     * @param x The entity's x-coordinate.
     * @param y The entity's y-coordinate;
     */
    public final void setPosition(final int x, final int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Updates the entity's sprite.
     *
     * If a URL is provided and the sprite can be successfully loaded, then the
     * sprite height/width are set according to the width and height of the
     * loaded image.
     *
     * If no spriteURL is provided or the sprite cannot be loaded, then the
     * sprite height/width default to 0;
     *
     * @param spriteUrl The URL for the entity's sprite.
     */
    public final void setSprite(final String spriteUrl) {
        this.spriteUrl = spriteUrl;
        sprite = ImageManager.loadBufferedImage(this.spriteUrl);

        spriteWidth = sprite != null ? sprite.getWidth() : 0;
        spriteHeight = sprite != null ? sprite.getHeight() : 0;
    }

    /**
     * Renders the entity's sprite at its current x and y position. Can be
     * overridden to provide additional functionality.
     *
     * @param g2 The Graphics2D object used for rendering the entity.
     */
    @Override
    public void render(final Graphics2D g2d) {
        if (sprite != null && g2d != null) {
            g2d.drawImage(sprite, x, y, spriteWidth, spriteHeight, null);
        } else {
            System.err.println(String.format("%s: Could not load sprite <'%s'>.", this.getClass().getName(), spriteUrl));
        }
    }

    /**
     * Abstract method to update the entity's behavior.
     */
    public abstract void update();

}
