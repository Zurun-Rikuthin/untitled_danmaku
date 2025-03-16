package com.rikuthin.entities;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Objects;

import javax.swing.JPanel;

import com.rikuthin.graphics.animations.AnimationInstance;
import com.rikuthin.graphics.animations.AnimationTemplate;
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
    protected BufferedImage currentSprite;
    /**
     * {@code true} if the should not render its sprite; {@code false}
     * otherwise.
     */
    protected boolean isInvisible;
    /**
     * The {@link AnimationInstance} for the entity's sprite.
     */
    protected AnimationInstance animation;
    /**
     * The entity's rectangular hitbox.
     */
    protected Rectangle hitbox;
    /**
     * {@code true} if the entity can collide with others; {@code false}
     * otherwise.
     */
    protected boolean isCollidable;
    /**
     * The maximum number of hit points the player can have.
     */
    protected int maxHitPoints;
    /**
     * The player's current hit points. Defaults to the max HP's value upon
     * construction.
     */
    protected int currentHitPoints;
    /**
     * How many pixels to move each frame. Defaults to 0 (stationary).
     */
    protected double speed;

    // ----- CONSTRUCTORS -----
    /**
     * Constructs an new {@link Entity}.
     * <p>
     * The entity's hitbox defaults to the dimensions and position of the
     * animation's first sprite if one is available.
     * <p>
     * Note: Currently, ALL hitboxes are standard rectangles.
     *
     * @param panel The parent {@link JPanel} to which the entity belongs.
     * @param position The initial position.
     * @param animationTemplate The shared {@link AnimationTemplate} for the
     * entity's sprite.
     * @param isInvisible {@code true} if the entity will visible render;
     * otherwise {@code false}.
     * @param isCollidable {@code true} if entity can collide with others;
     * otherwise {@code false}.
     * @param maxHitPoints The entity's maximum hit points.
     * @param speed How many pixels per frame the entity can move.
     */
    protected Entity(final JPanel panel, final Point position, final AnimationTemplate animationTemplate, final boolean isInvisible, final boolean isCollidable, final int maxHitPoints, final double speed) {
        if (panel == null) {
            throw new IllegalArgumentException(String.format(
                    "%s: Panel cannot be null",
                    this.getClass().getName()
            ));
        }

        this.panel = panel;
        setPosition(new Point(position));
        this.isInvisible = isInvisible;
        setAnimation(animationTemplate);
        setHitboxFromCurrentSprite();
        setCollidability(isCollidable);
        setMaxHitPoints(maxHitPoints);
        setCurrentHitPoints(currentHitPoints);
        setSpeed(speed);

        // Start the animation once everything else is loaded.
        if (this.animation != null) {
            this.animation.start();
        }
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
     * Returns the entity's current sprite image.
     *
     * @return The current sprite image if one exists, otherwise {@code null}.
     */
    public BufferedImage getCurrentSprite() {
        return animation != null ? animation.getCurrentFrameImage() : null;
    }

    /**
     * Returns the width of the entity's sprite in pixels.
     *
     * @return The sprite image's width if one exists, otherwise zero (0).
     */
    public final int getSpriteWidth() {
        return currentSprite != null ? currentSprite.getWidth() : 0;
    }

    /**
     * Returns the height of the entity's sprite in pixels.
     *
     * @return The sprite image's height.
     */
    public final int getSpriteHeight() {
        return currentSprite != null ? currentSprite.getHeight() : 0;
    }

    /**
     * Returns the entity's sprite animation.
     *
     * @return The sprite animation.
     */
    public AnimationInstance getAnimation() {
        return animation;
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

    /**
     * Returns the player's maximum hit points.
     *
     * @return The max hit points.
     */
    public int getMaxHitPoints() {
        return maxHitPoints;
    }

    /**
     * Returns the player's current hit points.
     *
     * @return The current hit points.
     */
    public int getCurrentHitPoints() {
        return currentHitPoints;
    }

    /**
     * Returns the player's movement speed.
     *
     * @return The speed in pixels per frame.
     */
    public double getSpeed() {
        return speed;
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
        this.position = new Point(position);
    }

    /**
     * Sets the entity's invisibility.
     *
     * @param isInvisible {@code true} if the entity is invisible, otherwise
     * {@code false}.
     */
    public void setInvisibility(final boolean isInvisible) {
        this.isInvisible = isInvisible;
    }

    /**
     * Sets the animation used for (and updates) the entity's sprite.
     *
     * @param animation The template for the animation.
     */
    public final void setAnimation(final AnimationTemplate animationTemplate) {
        if (animationTemplate != null) {
            animation = new AnimationInstance(animationTemplate);
            updateCurrentSprite();
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
     * Note: If there is no sprite, the width and height are both set to 0.
     */
    public final void setHitboxFromCurrentSprite() {
        int hitboxWidth = currentSprite != null ? currentSprite.getWidth() : 0;
        int hitboxHeight = currentSprite != null ? currentSprite.getHeight() : 0;

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

    /**
     * Sets the player's maximum hit points.
     * <p>
     * Uses the absolute value of the provided parameter.
     *
     * @param The max hit points.
     */
    public final void setMaxHitPoints(final int maxHitPoints) {

        this.maxHitPoints = Math.abs(maxHitPoints);
    }

    /**
     * Sets the player's current hit points.
     * <p>
     * Uses the absolute value of the provided parameter. If larger than the
     * maximum hit points, it is automatically set to the maximum value.
     *
     * @param The current hit points.
     */
    public final void setCurrentHitPoints(final int currentHitPoints) {
        this.currentHitPoints = Math.min(Math.abs(currentHitPoints), this.maxHitPoints);
    }

    /**
     * Sets the player's movement speed.
     *
     * @param speed The new movement speed in pixels per frame.
     */
    public final void setSpeed(final double speed) {
        this.speed = speed;
    }

    // ----- BUSINESS LOGIC METHODS -----
    /**
     * Checks if the entity is fully within the display of its parent panel.
     *
     * @return {@code true} if the sprite can be fully rendered inside the
     * panel; {@code false} otherwise.
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
     * @return {@code true} if the sprite cannot be rendered at all inside the
     * panel; {@code false otherwise}.
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
     * parent panel, position, sprite, hitbox, invisibility and collidability
     * statuses, hitpoints, and speed.
     *
     * @param obj the object to compare with
     * @return {@code true} if the entities are equal; {@code false} otherwise
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Entity)) {
            return false;
        }
        Entity other = (Entity) obj;
        return panel.equals(other.getPanel())
                && Objects.equals(position, other.getPosition())
                && Objects.equals(currentSprite, other.getCurrentSprite())
                && Boolean.compare(isInvisible, other.isInvisible()) == 0
                && Objects.equals(animation, other.getAnimation())
                && Objects.equals(hitbox, other.getHitbox())
                && Boolean.compare(isCollidable, other.isCollidable()) == 0
                && Integer.compare(maxHitPoints, other.getMaxHitPoints()) == 0
                && Integer.compare(currentHitPoints, other.getCurrentHitPoints()) == 0
                && Double.compare(speed, other.getSpeed()) == 0;
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
                currentSprite,
                isInvisible,
                animation,
                hitbox,
                isCollidable,
                maxHitPoints,
                currentHitPoints,
                speed
        );
    }

    @Override
    public void update() {
        if (animation != null) {
            animation.update();
            updateCurrentSprite();
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

        if (currentSprite != null) {
            g2d.drawImage(currentSprite, position.x, position.y, getSpriteWidth(), getSpriteHeight(), null);
        }
    }

    // ----- HELPER METHODS -----
    private void updateCurrentSprite() {
        if (animation != null) {
            currentSprite = animation.getCurrentFrameImage();
        }
    }
}
