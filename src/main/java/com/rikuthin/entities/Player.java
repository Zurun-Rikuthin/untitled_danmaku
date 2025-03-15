package com.rikuthin.entities;

import java.util.Objects;

import javax.swing.JPanel;

/**
 * This class represents the player-controlled character and defines how it
 * interacts with the game world.
 */
public class Player extends Entity {

    // ----- INSTANCE VARIABLES -----
    /**
     * How many pixels to move each frame. Defaults to 0 (stationary).
     */
    private int speed;
    /**
     * Flags indicating movement direction based on player input. Defaults to
     * {@code false}.
     */
    private boolean movingUp;
    private boolean movingDown;
    private boolean movingLeft;
    private boolean movingRight;

    // ----- CONSTRUCTORS -----
    /**
     * Constructs a player entity with specified position, sprite, and movement
     * <p>
     * The player remains stationary by default and moves only when directional
     * inputs are provided.
     *
     * @param panel The parent {@link JPanel} the player belongs to.
     * @param x The initial x-coordinate.
     * @param y The initial y-coordinate.
     * @param spriteUrl The URL for the player's sprite.
     * @param speed The movement speed in pixels per frame.
     */
    public Player(final JPanel panel, final int x, final int y, final String spriteUrl, final int speed) {
        super(panel, x, y, spriteUrl, false, true);
        this.speed = speed;
        this.movingUp = false;
        this.movingDown = false;
        this.movingLeft = false;
        this.movingRight = false;
    }

    // ----- GETTERS -----
    /**
     * Returns the player's movement speed.
     *
     * @return The speed in pixels per frame.
     */
    public int getSpeed() {
        return speed;
    }

    /**
     * Checks if the player is currently moving in any direction.
     *
     * @return {@code true} if moving, otherwise {@code false}.
     */
    public boolean isMoving() {
        return movingUp || movingDown || movingLeft || movingRight;
    }

    /**
     * Checks if the player is moving upward.
     *
     * @return {@code true} if moving up, otherwise {@code false}.
     */
    public boolean isMovingUp() {
        return movingUp;
    }

    /**
     * Checks if the player is moving downward.
     *
     * @return {@code true} if moving down, otherwise {@code false}.
     */
    public boolean isMovingDown() {
        return movingDown;
    }

    /**
     * Checks if the player is moving left.
     *
     * @return {@code true} if moving left, otherwise {@code false}.
     */
    public boolean isMovingLeft() {
        return movingLeft;
    }

    /**
     * Checks if the player is moving right.
     *
     * @return {@code true} if moving right, otherwise {@code false}.
     */
    public boolean isMovingRight() {
        return movingRight;
    }

    // ----- SETTERS -----
    /**
     * Sets the player's movement speed.
     *
     * @param speed The new movement speed in pixels per frame.
     */
    public void setSpeed(final int speed) {
        this.speed = speed;
    }

    /**
     * Sets whether the player is moving up.
     *
     * @param value {@code true} to move up, {@code false} to stop.
     */
    public void setMovingUp(boolean value) {
        movingUp = value;
    }

    /**
     * Sets whether the player is moving down.
     *
     * @param value {@code true} to move down, {@code false} to stop.
     */
    public void setMovingDown(boolean value) {
        movingDown = value;
    }

    /**
     * Sets whether the player is moving left.
     *
     * @param value {@code true} to move left, {@code false} to stop.
     */
    public void setMovingLeft(boolean value) {
        movingLeft = value;
    }

    /**
     * Sets whether the player is moving right.
     *
     * @param value {@code true} to move right, {@code false} to stop.
     */
    public void setMovingRight(boolean value) {
        movingRight = value;
    }

    // ----- BUSINESSS LOGIC METHODS -----
    /**
     * Moves the player based on directional inputs while ensuring they remain
     * within the panel boundaries.
     */
    public void move() {
        int dx = 0;
        int dy = 0;

        if (movingUp) {
            dy -= speed;
        }
        if (movingDown) {
            dy += speed;
        }
        if (movingLeft) {
            dx -= speed;
        }
        if (movingRight) {
            dx += speed;
        }

        // Apply movement
        x += dx;
        y += dy;

        // Ensure the player remains within bounds
        if (!isFullyWithinPanel()) {
            correctPosition();
        }
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

        Player other = (Player) obj;

        return super.equals(obj)
                && this.speed == other.speed
                && this.movingUp == other.movingUp
                && this.movingDown == other.movingDown
                && this.movingLeft == other.movingLeft
                && this.movingRight == other.movingRight;
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
                speed,
                movingUp,
                movingDown,
                movingLeft,
                movingRight
        );
    }

    // ---- HELPER METHODS -----
    /**
     * Ensures the player remains within the visible screen boundaries.
     */
    private void correctPosition() {
        // Trying to use Math.clamp gave out of bounds issues or something. This is simpler.
        x = Math.max(0, Math.min(x, panel.getWidth() - getSpriteWidth()));
        y = Math.max(0, Math.min(y, panel.getHeight() - getSpriteHeight()));
    }
}
