package com.rikuthin.entities;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
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
     * The x and y coordinates of the entity sprite's top left corner.
     */
    protected Point position;
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
     * The entity's rectangular hitbox.
     */
    protected Rectangle hitbox;
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
     * Note: Currently, ALL hitboxes are standard rectangles.
     *
     * @param panel The parent {@link JPanel} to which the entity belongs.
     * @param position The initial position.
     * @param spriteUrl The URL for the entity's sprite.
     * @param isInvisible {@code true} if the entity should not render its
     * sprite; {@code false} otherwise.
     * @param isCollidable Whether the entity can collide with others.
     */
    protected Entity(final JPanel panel, final Point position, final String spriteUrl, final boolean isInvisible, final boolean isCollidable) {
        if (panel == null) {
            throw new IllegalArgumentException(String.format(
                    "%s: Panel cannot be null",
                    this.getClass().getName()
            ));
        }

        this.panel = panel;
        setPosition(position);
        setInvisibility(isInvisible);
        setSprite(spriteUrl);
        setAnimation(null);
        this.isAnimated = false;
        setHitboxFromCurrentSprite();
        setCollidability(isCollidable);
    }

    /**
     * Constructs an entity with a animated sprite.
     * <p>
     * The entity's hitbox defaults to the dimensions and position of the
     * animation's first sprite if one is available.
     * <p>
     * Note: Currently, ALL hitboxes are standard rectangles.
     *
     * @param panel The parent {@link JPanel} to which the entity belongs.
     * @param position The initial position.
     * @param spriteUrl The URL for the entity's sprite.
     * @param isCollidable Whether the entity can collide with others.
     */
    protected Entity(final JPanel panel, final Point position, final Animation animation, final boolean isCollidable) {
        if (panel == null) {
            throw new IllegalArgumentException(String.format(
                    "%s: Panel cannot be null",
                    this.getClass().getName()
            ));
        }

        this.panel = panel;
        setPosition(position);
        setInvisibility(false);
        setAnimation(animation);
        this.isAnimated = true;
        setHitboxFromCurrentSprite();
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
        return position.x;
    }

    /**
     * Returns the y-coordinate of the entity's top-left corner.
     *
     * @return The y-coordinate.
     */
    public int getY() {
        return position.y;
    }

    /**
     * Returns the coordinates of the entity's top-left corner.
     *
     * @return The coordinates.
     */
    public Point getPosition() {
        return position;
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
     * Checks whether the entity's sprite animation (if one is set) is currently
     * playing.
     *
     * @return {@code true} if the animation is playing; {@code false}
     * otherwise.
     */
    public boolean isAnimated() {
        return animation != null && animation.isPlaying();
    }

    /**
     * Returns the entity's hitbox.
     *
     * @return The hitbox.
     */
    public Rectangle getHitbox() {
        return hitbox;
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
        position.x = x;
    }

    /**
     * Sets the y-coordinate of the entity's top-left corner.
     *
     * @param y The new y-coordinate.
     */
    public final void setY(final int y) {
        position.y = y;
    }

    /**
     * Sets the position of the entity's top-left corner.
     *
     * @param x The new x-coordinate.
     * @param y The new y-coordinate.
     */
    public final void setPosition(final int x, final int y) {
        position.x = x;
        position.y = y;
    }

    /**
     * Sets the position of the entity's top-left corner.
     *
     * @param position The new coordinates.
     */
    public final void setPosition(final Point position) {
        this.position = position;
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
            this.animation.setPosition(position);
        }
    }

    /**
     * Sets the entity hitbox.
     *
     * @param x The x-coordinate of the hitbox's upper-left corner.
     * @param y The y-coordinate of the hitbox's upper-left corner.
     * @param width The width of the hitbox in pixels
     * @param height The height of the hitbox in pixels
     */
    public final void setHitbox(final int x, final int y, final int width, final int height) {
        hitbox = new Rectangle(x, y, width, height);
    }

    /**
     * Sets the entity hitbox.
     *
     * @param position The coordinates of the hitbox's upper-left corner.
     * @param width The width of the hitbox in pixels
     * @param height The height of the hitbox in pixels
     */
    public final void setHitbox(final Point position, final int width, final int height) {
        hitbox = new Rectangle(position.x, position.y, width, height);
    }

    /**
     * Sets the size of the entity's hitbox using the dimensions of the entity's
     * current sprite.
     * <p>
     * Note: If a sprite is currently not set, the the width and height are both
     * set to 0.
     */
    public final void setHitboxFromCurrentSprite() {
        int hitboxWidth = sprite != null ? sprite.getWidth() : 0;
        int hitboxHeight = sprite != null ? sprite.getHeight() : 0;

        setHitbox(position, hitboxWidth, hitboxHeight);
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
    /**
     * Checks if the entity is fully within the display of its parent panel.
     * 
     * @return {@code true} if the sprite can be fully rendered inside the panel; {@code false} otherwise.
     */
    public boolean isFullyWithinPanel() {
        return position.x >= 0
                && position.y >= 0
                && position.x + getSpriteWidth() <= panel.getWidth()
                && position.y + getSpriteHeight() <= panel.getHeight();
    }

    /**
     * Checks if the entity is fully outside the display of its parent panel.
     * 
     * @return {@code true} if the sprite cannot be rendered at all inside the panel; {@code false otherwise}.
     */
    public boolean isFullyOutsidePanel() {
        Rectangle spriteBounds = new Rectangle(position.x, position.y, getSpriteWidth(), getSpriteHeight());
        Rectangle panelBounds = new Rectangle(0, 0, panel.getWidth(), panel.getHeight());

        return !spriteBounds.intersects(panelBounds);
    }

    /**
     * Checks whether this entity's (rectangular) hitbox intersects with another
     * rectangular space.
     *
     * @param x The rectangular space being checked for collision.
     * @return {@code true} if the two spaces intersect;
     * {@code false otherwise}.
     */
    public boolean collides(final Rectangle rectangle) {
        if (!isCollidable || hitbox == null || rectangle == null) {
            return false;
        }

        return hitbox.intersects(rectangle);
    }

    /**
     * Checks whether this entity's (rectangular) hitbox intersects with
     * another's.
     *
     * @param entity The other entity being checked for collision.
     * @return {@code true} if the two spaces intersect;
     * {@code false otherwise}.
     */
    public boolean collides(final Entity entity) {
        return collides(entity.getHitbox());
    }

    // ----- OVERRIDDEN METHODS -----
    /**
     * Compares this entity with another object for equality. May be overridden
     * to provide futher functionality.
     * <p>
     * By default, two entities are considered equal if they have the same
     * parent panel, position, sprite, hitbox, and invisibility and
     * collidability statuses.
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
                && Objects.equals(position, other.getPosition())
                && Objects.equals(sprite, other.getSprite())
                && Boolean.compare(isInvisible, other.isInvisible()) == 0
                && Objects.equals(animation, other.getAnimation())
                && Objects.equals(hitbox, other.getHitbox())
                && Boolean.compare(isCollidable, other.isCollidable()) == 0;
    }

    /**
     * Computes the hash code for this entity.
     * <p>
     * By default, the hash is calculated using the entity's parent panel,
     * position, sprite, hitbox, and invisibility and collidability statuses.
     *
     * @return the hash code of this entity
     */
    @Override
    public int hashCode() {
        return Objects.hash(
                panel,
                position,
                sprite,
                isInvisible,
                animation,
                hitbox,
                isCollidable
        );
    }

    @Override
    public void update() {
        if (animation != null) {
            animation.setPosition(position);
            animation.update();
            setSprite(animation.getCurrentFrameImage());
            setHitboxFromCurrentSprite();
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
            g2d.drawImage(sprite, position.x, position.y, getSpriteWidth(), getSpriteHeight(), null);
        }
    }
}
