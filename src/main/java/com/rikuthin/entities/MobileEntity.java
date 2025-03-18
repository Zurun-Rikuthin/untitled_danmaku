package com.rikuthin.entities;

import java.util.Objects;

import javax.swing.JPanel;

/**
 * Represents a mobile entity in the game that can move around the game world.
 * Extends the Entity class to add movement-related behavior.
 */
public abstract class MobileEntity extends Entity {

    // ----- INSTANCE VARIABLES -----
    /**
     * The movement speed of the entity along the x-axis in pixels per frame.
     */
    protected double velocityX;

    /**
     * The movement speed of the entity along the y-axis in pixels per frame.
     */
    protected double velocityY;

    // ----- CONSTRUCTORS -----
    /**
     * Private constructor used by the builder pattern to instantiate a
     * MobileEntity.
     *
     * @param builder The builder used to construct the entity.
     */
    protected MobileEntity(MobileEntityBuilder builder) {
        super(builder);
    }

    // ----- GETTERS -----
    /**
     * Returns the movement speed of the entity along the x-axis in pixels per
     * frame.
     *
     * @return The speed.
     */
    public double getVelocityX() {
        return velocityX;
    }

    /**
     * Returns the movement speed of the entity along the y-axis in pixels per
     * frame.
     *
     * @return The speed.
     */
    public double getVelocityY() {
        return velocityY;
    }

    // ----- SETTERS -----
    public void setVelocityX(double velocityX) {
        this.velocityX = velocityX;
    }

    public void setVelocityY(double velocityY) {
        this.velocityY = velocityY;
    }

    // ----- OVERRIDDEN METHODS -----
    /**
     * Compares this entity to another object for equality.
     *
     * @param obj The {@link Object} to compare with.
     * @return {@code true} if the objects are equal, {@code false} otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof MobileEntity)) {
            return false;
        }
        MobileEntity other = (MobileEntity) obj;
        return super.equals(other)
                && Double.compare(velocityX, other.getVelocityX()) == 0
                && Double.compare(velocityY, other.getVelocityY()) == 0;
    }

    /**
     * Returns a hash code for this entity.
     *
     * @return The hash code of the entity.
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), velocityX, velocityY);
    }

    /**
     * Updates the state of the entity, including its position based on its
     * velocity.
     */
    @Override
    public void update() {
        super.update(); // Update animations and hitbox

        // Update position based on velocity
        position.x += velocityX;
        position.y += velocityY;

        // Update hitbox position
        setHitboxFromCurrentSprite();
    }

    // ----- BUILDER PATTERN -----
    /**
     * The MobileEntityBuilder class provides a fluent API for constructing an
     * MobileEntity object.
     */
    public static class MobileEntityBuilder<T extends MobileEntityBuilder<T>> extends EntityBuilder<T> {

        // ----- INSTANCE VARIABLES -----
        private double velocityX = 0;
        private double velocityY = 0;

        // ------ CONSTRUCTORS -----
        public MobileEntityBuilder(JPanel panel) {
            super(panel);
        }

        // ---- SETTERS -----
        /**
         * Sets the movement speed on the x-axis (in pixels per frame).
         *
         * @param velocityX The x-axis velocity.
         * @return The builder instance.
         */
        public T velocityX(final double velocityX) {
            this.velocityX = velocityX;
            return self();
        }

        /**
         * Sets the movement speed on the y-axis (in pixels per frame).
         *
         * @param velocityY The y-axis velocity.
         * @return The builder instance.
         */
        public T velocityY(final double velocityY) {
            this.velocityY = velocityY;
            return self();
        }
    }
}
