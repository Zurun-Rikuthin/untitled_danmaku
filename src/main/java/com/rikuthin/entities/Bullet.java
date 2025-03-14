package com.rikuthin.entities;

import java.util.Objects;

import javax.swing.JPanel;

import com.rikuthin.utility.Bearing2D;

/**
 * Represents a bubble that moves within a JPanel. The bubble moves along a
 * specified bearing (angle in degrees) at a defined speed (pixels per tick) and
 * bounces off the edges and walls, stopping at the roof.
 */
public class Bullet extends Entity {

    // ----- INSTANCE VARIABLES -----
    /**
     * Direction of movement (bearing)
     */
    private Bearing2D bearing;
    /**
     * Movement speed in pixels per frame. Defaults to 0 (i.e., not moving).
     */
    private double speed;

    // ----- CONSTRUCTORS -----
    /**
     * Constructs a new, non-moving Bullet.
     *
     * @param panel The {@link JPanel} to which the bullet belongs.
     * @param x The initial x-coordinate.
     * @param y The initial y-coordinate.
     * @param spriteUrl The URL for the bullet's sprite.
     */
    public Bullet(final JPanel panel, final int x, final int y, final String spriteUrl) {
        super(panel, x, y, true, spriteUrl, true);
        this.bearing = null;
        this.speed = 0;
    }

    /**
     * Constructs a new Bullet that moves in a given direction.
     *
     * @param panel The {@link JPanel} to which the bullet belongs.
     * @param x The initial x-coordinate.
     * @param y The initial y-coordinate.
     * @param spriteUrl The URL for the bullet's sprite.
     * @param bearing The direction the bullet should move in.
     * @param speed The bullet's movement speed in pixels per frame.
     */
    public Bullet(final JPanel panel, final int x, final int y, final String spriteUrl, final Bearing2D bearing, final double speed) {
        super(panel, x, y, true, spriteUrl, true);
        this.bearing = bearing;
        this.speed = speed;
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

    /**
     * Returns the bullet's current movement speed in pixels per frame.
     *
     * @return The movement speed.
     */
    public double getSpeed() {
        return speed;
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

    /**
     * Sets the speed the bullet should move at in pixels per frame. Uses the
     * the absolute (positive) value.
     *
     * @param speed The movement speed.
     */
    public void setSpeed(final double speed) {
        this.speed = Math.abs(speed);
    }

    // ----- BUSINESS LOGIC METHODS -----
    /**
     * Moves the bubble based on its bearing and speed.
     */
    public void move() {
        if (!isMoving()) {
            return;
        }

        final double radians = Math.toRadians(bearing.getDegrees());

        x += speed * Math.cos(radians);
        y -= speed * Math.sin(radians); // Inverted for screen coordinates
    }

    // ----- OVERRIDDEN METHODS -----
    /**
     * Compares this player entity to another object for equality.
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

        if (!(obj instanceof Player)) {
            return false;
        }

        Bullet other = (Bullet) obj;

        return super.equals(obj)
                && this.bearing.equals(other.getBearing())
                && this.speed == other.speed;
    }

    /**
     * Computes the hash code for this player entity.
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
}
