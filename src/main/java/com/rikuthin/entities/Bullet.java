package com.rikuthin.entities;

import java.awt.Point;
import java.util.Objects;

import javax.swing.JPanel;

import com.rikuthin.graphics.animations.AnimationTemplate;
import com.rikuthin.utility.Bearing2D;

/**
 * Represents a generic bullet that moves within a JPanel.
 * <p>
 * The bullet moves along a specified bearing (angle in degrees) at a defined
 * speed (pixels per tick).
 */
public class Bullet extends Entity {

    // ----- INSTANCE VARIABLES -----
    /**
     * Direction of movement (bearing)
     */
    private Bearing2D bearing;

    // ----- CONSTRUCTORS -----
    /**
     * Constructs a new {@link Bullet} with an animated sprite.
     *
     * @param panel The {@link JPanel} to which the bullet belongs.
     * @param position The initial x and y-coordinates.
     * @param animationTemplate The {@AnimationTemplate} for the bullet's
     * sprite.
     * @param bearing The direction the bullet should move in.
     * @param speed The bullet's movement speed in pixels per frame.
     */
    public Bullet(final JPanel panel, final Point position, final AnimationTemplate animationTemplate, final Bearing2D bearing, final double speed) {
        super(panel, position, animationTemplate, false, true, 0, speed);
        this.bearing = bearing;
    }

    // ----- GETTERS -----
    /**
     * Returns whether the movement speed is currently 0.
     *
     * @return {@code true} if the movement speed is zero; {@code false}
     * otherwise.
     */
    public boolean isMoving() {
        return speed != 0;
    }

    /**
     * Returns the direction the bullet is moving in.
     *
     * @return The movement direction.
     */
    public Bearing2D getBearing() {
        return bearing;
    }

    // ----- SETTERS -----
    /**
     * Sets the direction the bullet should move in.
     *
     * @param bearing The movement direction.
     */
    public void setBearing(final Bearing2D bearing) {
        this.bearing = bearing;
    }

    // ----- BUSINESS LOGIC METHODS -----
    /**
     * Moves the bullet based on its bearing and speed.
     */
    public void move() {
        if (!isMoving()) {
            return;
        }

        final double radians = Math.toRadians(bearing.getDegrees());

        position.x += Math.floor(speed * Math.cos(radians));
        position.y -= Math.floor(speed * Math.sin(radians)); // Inverted for screen coordinates
    }

    // ----- OVERRIDDEN METHODS -----
    /**
     * Compares this bullet entity to another object for equality.
     * <p>
     * Extends {@code equals()} from the {@link Entity} class by comparing
     * movement-related attributes such as speed and directional states.
     *
     * @param obj The object to compare with.
     * @return {@code true} if the objects are equal, otherwise {@code false}.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Bullet)) {
            return false;
        }

        Bullet other = (Bullet) obj;

        return super.equals(obj)
                && Objects.equals(bearing, other.getBearing())
                && Double.compare(speed, other.speed) == 0;
    }

    /**
     * Computes the hash code for this bullet entity.
     *
     * Extends {@code hashCode()} from the {@link Entity} class by incorporating
     * movement attributes such as speed and directional states.
     *
     * @return The computed hash code.
     */
    @Override
    public int hashCode() {
        return Objects.hash(
                super.hashCode(),
                bearing,
                speed
        );
    }

    @Override
    public void update() {
        super.update();
        move();

        if (animation != null) {
            animation.update();
        }
    }
}
