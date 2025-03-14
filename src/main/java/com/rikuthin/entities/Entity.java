package com.rikuthin.entities;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Objects;

import javax.swing.JPanel;

import com.rikuthin.graphics.ImageManager;
import com.rikuthin.interfaces.Renderable;

/**
 * The base class for all entities in the game.
 * <p>
 * This class represents any object that has behavior (e.g., movement,
 * animation, or interaction with other entities) in the game world.
 */
public abstract class Entity implements Renderable {

    // ----- INSTANCE VARIABLES -----
    /**
     * The parent {@link JPanel} to which the entity belongs.
     */
    protected JPanel panel;
    /**
     * The x-coordinate of the entity's top-left corner.
     */
    protected int x;
    /**
     * The y-coordinate of the entity's top-left corner.
     */
    protected int y;
    /**
     * Determines if the entity is invisible, meaning any sprite set is not
     * rendered.
     */
    protected boolean isInvisible;
    /**
     * Determines if the entity can collide with others.
     */
    protected boolean isCollidable;
    /**
     * The URL for the entity's sprite;
     */
    protected String spriteUrl;
    /**
     * The entity's sprite image.
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
     * The x-coordinate of the entity hitbox's top-left corner.
     */
    protected int hitboxX;
    /**
     * The y-coordinate of the entity hitbox's top-left corner.
     */
    protected int hitboxY;
    /**
     * Width of the entity's hitbox in pixels. Defaults to the sprite height.
     */
    protected int hitboxWidth;
    /**
     * Height of the entity's hitbox in pixels. Defaults to the sprite width.
     */
    protected int hitboxHeight;

    // ----- CONSTRUCTORS -----
    /**
     * Constructs an entity with the specified properties.
     * <p>
     * The entity's hitbox defaults to the sprite dimensions and position if a
     * sprite is loaded.
     * <p>
     * Note: ALL hitboxes are rectangles (NOT quadrilaterals or other polygonal
     * shapes).
     *
     * @param panel The parent {@link JPanel} to which the entity belongs.
     * @param x The initial x-coordinate.
     * @param y The initial y-coordinate.
     * @param isInvisible Whether the entity is invisible and does not render
     * its sprite (even if one is set).
     * @param spriteUrl The URL for the entity's sprite.
     * @param isCollidable Whether the entity can collide with others.
     */
    protected Entity(final JPanel panel, final int x, final int y, final boolean isInvisible, final String spriteUrl, final boolean isCollidable) {
        this.panel = panel;

        if (panel == null) {
            throw new IllegalArgumentException(String.format(
                    "%s: Panel cannot be null",
                    this.getClass().getName()
            ));
        }

        setPosition(x, y);
        setInvisibility(isInvisible);
        setSprite(spriteUrl);
        setHitboxPosition(x, y);
        setHitboxSize(spriteWidth, spriteHeight);
        setCollidability(isCollidable);
    }

    // ----- GETTERS -----
    /**
     * Returns the parent {@link JPanel} to which the entity belongs.
     *
     * @return The parent panel.
     */
    public JPanel getPanel() {
        return panel;
    }

    /**
     * Returns the x-coordinate of the entity's top-left corner.
     *
     * @return The x-coordinate.
     */
    public int getX() {
        return x;
    }

    /**
     * Returns the y-coordinate of the entity's top-left corner.
     *
     * @return The y-coordinate.
     */
    public int getY() {
        return y;
    }

    /**
     * Returns whether the entity is invisible.
     *
     * @return {@code true} if the entity is invisible, otherwise {@code false}.
     */
    public boolean isInvisible() {
        return isInvisible;
    }

    /**
     * Returns the URL for the entity's sprite image.
     *
     * @return The sprite URL.
     */
    public String getSpriteUrl() {
        return spriteUrl;
    }

    /**
     * Returns the entity's sprite image.
     *
     * @return The sprite image.
     */
    public BufferedImage getSprite() {
        return sprite;
    }

    /**
     * Returns the width of the entity's sprite in pixels.
     *
     * @return The sprite width.
     */
    public int getSpriteWidth() {
        return spriteWidth;
    }

    /**
     * Returns the height of the entity's sprite in pixels.
     *
     * @return The sprite height.
     */
    public int getSpriteHeight() {
        return spriteHeight;
    }

    /**
     * Returns the x-coordinate of the entity hitbox's top-left corner.
     *
     * @return The x-coordinate.
     */
    public int getHitboxX() {
        return hitboxX;
    }

    /**
     * Returns the y-coordinate of the entity hitbox's top-left corner.
     *
     * @return The y-coordinate.
     */
    public int getHitboxY() {
        return hitboxY;
    }

    /**
     * Returns the width of the entity's hitbox in pixels.
     *
     * @return The hitbox width.
     */
    public int getHitboxWidth() {
        return hitboxWidth;
    }

    /**
     * Returns the height of the entity's hitbox in pixels.
     *
     * @return The height.
     */
    public int getHitboxHeight() {
        return hitboxHeight;
    }

    /**
     * Returns whether the entity can collide with others.
     *
     * @return {@code true} if the entity can collide, otherwise {@code false}.
     */
    public boolean isCollidable() {
        return isCollidable;
    }

    // ----- SETTERS -----
    /**
     * Sets the position of the entity's top-left corner.
     *
     * @param x The new x-coordinate.
     * @param y The new y-coordinate.
     */
    public final void setPosition(final int x, final int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Sets the entity's invisibility.
     *
     * @param isInvisible {@code true} if the entity is invisible, otherwise
     * {@code false}.
     */
    public final void setInvisibility(final boolean isInvisible) {
        this.isInvisible = isInvisible;
    }

    /**
     * Sets the entity's sprite and updates its dimensions.
     * <p>
     * If a URL is provided and the sprite can be successfully loaded, then the
     * sprite height/width are set according to the width and height of the
     * loaded image.
     * <p>
     * Note: If the entity is invisible, the sprite will still be set, just not
     * rendered.
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
     * Sets the position of the entity hitbox's top-left corner.
     *
     * @param x The new hitbox x-coordinate.
     * @param y The new hitbox y-coordinate.
     */
    public final void setHitboxPosition(final int x, final int y) {
        this.hitboxX = x;
        this.hitboxY = y;
    }

    /**
     * Sets the size of the entity's hitbox.
     *
     * @param width The new hitbox width in pixels.
     * @param height The new hitbox height in pixels.
     */
    public final void setHitboxSize(final int width, final int height) {
        this.hitboxWidth = Math.abs(width);
        this.hitboxHeight = Math.abs(height);
    }

    /**
     * Sets whether the entity can collide with others.
     *
     * @param isInvisible {@code true} if the entity can collide, otherwise
     * {@code false}.
     */
    public final void setCollidability(final boolean isCollidable) {
        this.isCollidable = isCollidable;
    }

    // ----- BUSINESS LOGIC METHODS -----
    public boolean isWithinPanel() {
        return x >= 0
                && y >= 0
                && x + spriteWidth < panel.getWidth()
                && y + spriteHeight < panel.getHeight();
    }

    /**
     * Checks whether this entity's (rectangular) hitbox collides with another
     * rectangular space using the Axis Aligned Bounding Boxes (AABB) algorithm.
     *
     * @return {@code true} if the two spaces intersect;
     * {@code false otherwise}.
     */
    public boolean collides(final int x, final int y, final int width, final int height) {
        if (!isCollidable){
            return false;
        }

        return ((this.x + this.hitboxWidth >= x) && (this.y + hitboxHeight >= y))
                || ((x + width >= this.x) && (y + height >= this.y));
    }

    /**
     * Checks whether this entity's (rectangular) hitbox collides another's
     * using the Axis Aligned Bounding Boxes (AABB) algorithm.
     *
     * @return {@code true} if the two spaces intersect;
     * {@code false otherwise}.
     */
    public boolean collides(final Entity entity) {
        if (!isCollidable || !entity.isCollidable()){
            return false;
        }
        return collides(entity.getX(), entity.getY(), entity.getHitboxWidth(), entity.getHitboxHeight());
    }

    // ----- OVERRIDDEN METHODS -----
    /**
     * Compares this entity with another object for equality. May be overridden
     * to provide futher functionality.
     * <p>
     * By default, two entities are considered equal if they have the same
     * parent panel, position, sprite (image, URL, and dimensions) and hitbox
     * (position and dimensions.
     *
     * @param obj the object to compare with
     * @return {@code true} if the entities are equal; {@code false} otherwise
     */
    @Override
    public boolean equals(final Object obj
    ) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Entity)) {
            return false;
        }
        Entity other = (Entity) obj;
        return panel.equals(other.getPanel())
                && Integer.compare(x, other.getX()) == 0
                && Integer.compare(y, other.getY()) == 0
                && Objects.equals(spriteUrl, other.getSpriteUrl())
                && Objects.equals(sprite, other.getSprite())
                && Integer.compare(spriteWidth, other.getSpriteWidth()) == 0
                && Integer.compare(spriteHeight, other.getSpriteHeight()) == 0
                && Integer.compare(hitboxX, other.getHitboxX()) == 0
                && Integer.compare(hitboxY, other.getHitboxY()) == 0
                && Integer.compare(hitboxWidth, other.getHitboxWidth()) == 0
                && Integer.compare(hitboxHeight, other.getHitboxHeight()) == 0;
    }

    /**
     * Computes the hash code for this entity.
     * <p>
     * By default, the hash is calculated using the entity's parent panel,
     * position, sprite (image, URL, and dimensions) and hitbox (position and
     * dimensions.
     *
     * @return the hash code of this entity
     */
    @Override
    public int hashCode() {
        return Objects.hash(
                panel,
                x,
                y,
                spriteUrl,
                sprite,
                spriteWidth,
                spriteHeight,
                hitboxX,
                hitboxY,
                hitboxWidth,
                hitboxHeight
        );
    }

    /**
     * Renders the entity's sprite at its current x and y position.
     *
     * @param g2 The Graphics2D object used for rendering the entity.
     */
    @Override
    public void render(final Graphics2D g2d
    ) {
        if (isInvisible) {
            return;
        }

        if (sprite != null) {
            g2d.drawImage(sprite, x, y, spriteWidth, spriteHeight, null);
        }
    }
}
