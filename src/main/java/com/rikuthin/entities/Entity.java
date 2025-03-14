package com.rikuthin.entities;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Objects;

import javax.swing.JPanel;

import com.rikuthin.graphics.Animation;
import com.rikuthin.graphics.ImageManager;
import com.rikuthin.interfaces.Renderable;
import com.rikuthin.interfaces.Updateable;

/**
 * The base class for all entities in the game.
 * <p>
 * This class represents any object that has behavior (e.g., movement,
 * animation, or interaction with other entities) in the game world.
 */
public abstract class Entity implements Updateable, Renderable {

    // ----- INSTANCE VARIABLES -----
    /**
     * The parent {@link JPanel} to which the entity belongs.
     */
    protected JPanel panel;
    /**
     * The x-coordinate of the entity sprite's top-left corner.
     */
    protected int x;
    /**
     * The y-coordinate of the entity sprite's top-left corner.
     */
    protected int y;
    /**
     * The entity's current sprite image.
     */
    protected BufferedImage sprite;
    /**
     * {@code true} if the should not render its sprite; {@code false}
     * otherwise.
     */
    protected boolean isInvisible;
    /**
     * The entity's sprite animation.
     */
    protected Animation animation;
    /**
     * {@code true} if the entity's sprite animation (if set) is currently
     * playing; {@code false} if the sprite is static.
     */
    protected boolean isAnimated;
    /**
     * The x-coordinate of the entity hitbox's top-left corner. Defaults to that
     * of the current sprite.
     */
    protected int hitboxX;
    /**
     * The y-coordinate of the entity hitbox's top-left corner. Defaults to that
     * of the current sprite.
     */
    protected int hitboxY;
    /**
     * Width of the entity's hitbox in pixels. Defaults to that of the current
     * sprite.
     */
    protected int hitboxWidth;
    /**
     * Height of the entity's hitbox in pixels. Defaults to that of the current
     * sprite.
     */
    protected int hitboxHeight;
    /**
     * {@code true} if the entity can collide with others; {@code false}
     * otherwise.
     */
    protected boolean isCollidable;

    // ----- CONSTRUCTORS -----
    /**
     * Constructs an entity with a static (i.e., non-animated) sprite.
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
     * @param spriteUrl The URL for the entity's sprite.
     * @param isInvisible {@code true} if the entity should not render its
     * sprite; {@code false} otherwise.
     * @param isCollidable Whether the entity can collide with others.
     */
    protected Entity(final JPanel panel, final int x, final int y, final String spriteUrl, final boolean isInvisible, final boolean isCollidable) {
        if (panel == null) {
            throw new IllegalArgumentException(String.format(
                    "%s: Panel cannot be null",
                    this.getClass().getName()
            ));
        }

        this.panel = panel;
        setPosition(x, y);
        setInvisibility(isInvisible);
        setSprite(spriteUrl);
        setAnimation(null);
        this.isAnimated = false;
        setHitboxPosition(x, y);
        setHitboxSizeFromCurrentSprite();
        setCollidability(isCollidable);
    }

    /**
     * Constructs an entity with a animated sprite.
     * <p>
     * The entity's hitbox defaults to the dimensions and position of the
     * animation's first sprite if one is available.
     * <p>
     * Note: ALL hitboxes are rectangles (NOT quadrilaterals or other polygonal
     * shapes).
     *
     * @param panel The parent {@link JPanel} to which the entity belongs.
     * @param x The initial x-coordinate.
     * @param y The initial y-coordinate.
     * @param spriteUrl The URL for the entity's sprite.
     * @param isCollidable Whether the entity can collide with others.
     */
    protected Entity(final JPanel panel, final int x, final int y, final Animation animation, final boolean isCollidable) {
        if (panel == null) {
            throw new IllegalArgumentException(String.format(
                    "%s: Panel cannot be null",
                    this.getClass().getName()
            ));
        }

        this.panel = panel;
        setPosition(x, y);
        setInvisibility(isInvisible);
        setAnimation(animation);
        this.isAnimated = true;
        setSprite();
        setHitboxPosition(x, y);
        setHitboxSizeFromCurrentSprite();
        setCollidability(isCollidable);

        // Start the animation once everything else is loaded.
        this.animation.start();
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
     * @return The sprite image's width.
     */
    public final int getSpriteWidth() {
        return sprite != null ? sprite.getWidth() : 0;
    }

    /**
     * Returns the height of the entity's sprite in pixels.
     *
     * @return The sprite image's height.
     */
    public final int getSpriteHeight() {
        return sprite != null ? sprite.getHeight() : 0;
    }

    /**
     * Returns the entity's sprite animation.
     *
     * @return The sprite animation.
     */
    public Animation getAnimation() {
        return animation;
    }
    
    /**
     * Checks whether the entity's sprite animation (if one is set) is currently playing.
     * 
     * @return {@code true} if the animation is playing; {@code false} otherwise.
     */
    public boolean isAnimated() {
        return animation!= null && animation.isPlaying();
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
     * Sets the x-coordinate of the entity's top-left corner.
     *
     * @param x The new x-coordinate.
     */
    public final void setX(final int x) {
        this.x = x;
    }

    /**
     * Sets the y-coordinate of the entity's top-left corner.
     *
     * @param x The new y-coordinate.
     */
    public final void setY(final int y) {
        this.y = y;
    }

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
     * Sets the entity's sprite using an image URL and updates its dimensions.
     * <p>
     * If a URL is provided and the sprite can be successfully loaded, then the
     * sprite height/width are set according to the width and height of the
     * loaded image.
     * <p>
     * Otherwise, the sprite will be set to {@code null}.
     * <p>
     * Note: If the entity is invisible, the sprite will still be set, just not
     * rendered.
     *
     * @param spriteUrl The URL for the entity's sprite.
     */
    public final void setSprite(final String spriteUrl) {
        sprite = ImageManager.loadBufferedImage(spriteUrl);
    }

    /**
     * Sets the entity's sprite using the current frame of the entity's
     * {@link Animation}.
     * <p>
     * Note: If the entity is invisible, the sprite will still be set, just not
     * rendered.
     */
    public final void setSprite() {
        if (animation != null && !animation.isEmpty()) {
            sprite = animation.getCurrentFrameImage();
        }
    }

    /**
     * Sets the entity's sprite using a pre-loaded {@link BufferedImage}.
     * <p>
     * Note: If the entity is invisible, the sprite will still be set, just not
     * rendered.
     *
     * @param image The image for the entity's sprite.
     */
    public final void setSprite(final BufferedImage image) {
        sprite = image;
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
     * Sets the animation used for the entity's sprite.
     *
     * @param animation The animation.
     */
    public final void setAnimation(final Animation animation) {
        this.animation = animation;

        if (this.animation != null && !this.animation.isEmpty()) {
            setSprite(this.animation.getCurrentFrameImage());
        }
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
     * Sets the size of the entity's hitbox using the provided values.
     *
     * @param width The new hitbox width in pixels.
     * @param height The new hitbox height in pixels.
     */
    public final void setHitboxSize(final int width, final int height) {
        this.hitboxWidth = Math.abs(width);
        this.hitboxHeight = Math.abs(height);
    }

    /**
     * Sets the size of the entity's hitbox using the dimensions of the entity's
     * current sprite.
     * <p>
     * Note: If a sprite is currently not set, the the width and height are both
     * set to 0.
     */
    public final void setHitboxSizeFromCurrentSprite() {
        this.hitboxWidth = sprite != null ? sprite.getWidth() : 0;
        this.hitboxHeight = sprite != null ? sprite.getHeight() : 0;
    }

    /**
     * Sets whether the entity can collide with others.
     *
     * @param isCollidable {@code true} if the entity can collide, otherwise
     * {@code false}.
     */
    public final void setCollidability(final boolean isCollidable) {
        this.isCollidable = isCollidable;
    }

    // ----- BUSINESS LOGIC METHODS -----
    public boolean isWithinPanel() {
        return x >= 0
                && y >= 0
                && x + getSpriteWidth() < panel.getWidth()
                && y + getSpriteHeight() < panel.getHeight();
    }

    /**
     * Checks whether this entity's (rectangular) hitbox collides with another
     * rectangular space using the Axis Aligned Bounding Boxes (AABB) algorithm.
     *
     * @return {@code true} if the two spaces intersect;
     * {@code false otherwise}.
     */
    public boolean collides(final int x, final int y, final int width, final int height) {
        if (!isCollidable) {
            return false;
        }

        return (this.hitboxX < x + width)
                && (this.hitboxX + this.hitboxWidth > x)
                && (this.hitboxY < y + height)
                && (this.hitboxY + this.hitboxHeight > y);
    }

    /**
     * Checks whether this entity's (rectangular) hitbox collides another's
     * using the Axis Aligned Bounding Boxes (AABB) algorithm.
     *
     * @return {@code true} if the two spaces intersect;
     * {@code false otherwise}.
     */
    public boolean collides(final Entity entity) {
        if (!isCollidable || !entity.isCollidable()) {
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
     * parent panel, position, sprite, hitbox (position and dimensions), and
     * invisibility and collidability statuses.
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
                && Objects.equals(sprite, other.getSprite())
                && Boolean.compare(isInvisible, other.isInvisible()) == 0
                && animation.equals(other.getAnimation())
                && Integer.compare(hitboxX, other.getHitboxX()) == 0
                && Integer.compare(hitboxY, other.getHitboxY()) == 0
                && Integer.compare(hitboxWidth, other.getHitboxWidth()) == 0
                && Integer.compare(hitboxHeight, other.getHitboxHeight()) == 0
                && Boolean.compare(isCollidable, other.isCollidable()) == 0;
    }

    /**
     * Computes the hash code for this entity.
     * <p>
     * By default, the hash is calculated using the entity's parent panel,
     * position, sprite (image, URL, and dimensions), hitbox (position and
     * dimensions, and invisibility and collidability statuses.
     *
     * @return the hash code of this entity
     */
    @Override
    public int hashCode() {
        return Objects.hash(
                panel,
                x,
                y,
                sprite,
                isInvisible,
                animation,
                hitboxX,
                hitboxY,
                hitboxWidth,
                hitboxHeight,
                isCollidable
        );
    }

    @Override
    public void update() {
        if (animation != null) {
            animation.update();
            setSprite(animation.getCurrentFrameImage());
            setHitboxSizeFromCurrentSprite();
        }
    }

    /**
     * Renders the entity's sprite at its current x and y position.
     *
     * @param g2 The Graphics2D object used for rendering the entity.
     */
    @Override
    public void render(final Graphics2D g2d) {
        if (isInvisible) {
            return;
        }

        if (sprite != null) {
            g2d.drawImage(sprite, x, y, getSpriteWidth(), getSpriteHeight(), null);
        }
    }
}
