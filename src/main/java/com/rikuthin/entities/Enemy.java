package com.rikuthin.entities;

import java.awt.Dimension;
import java.awt.Point;
import java.util.Objects;

import javax.swing.JPanel;

import com.rikuthin.utility.Bearing2D;

/**
 * Represents a enemy in the game that is mobile and can move around the game
 * world.
 */
public class Enemy extends MobileEntity {

    /**
     * The enemy's bullet spawner.
     */
    protected BulletSpawner bulletSpawner;
    /**
     * Whether the enemy is currently shooting bullets.
     */
    protected boolean isFiringBullets;

    // ----- CONSTRUCTORS -----
    /**
     * Constructor used to create a Enemy instance.
     *
     * @param builder The builder used to construct the enemy.
     */
    public Enemy(EnemyBuilder builder) {
        super(builder);
    }

    // ---- GETTERS -----
    /**
     * Returns the enemy's {@link BulletSpawner}
     *
     * @return the bullet spawner.
     */
    public BulletSpawner getBulletSpawner() {
        return bulletSpawner;
    }

    /**
     * Returns whether the enemy is firing bullets.
     *
     * @return {@code true} if firing bullets, {@code false} otherwise.
     */
    public boolean isFiringBullets() {
        return isFiringBullets;
    }

    // ---- SETTERS -----
    /**
     * Sets the enemy's {@link BulletSpawner}
     *
     * @param bulletSpawner the bullet spawner.
     */
    public final void setBulletSpawner(final BulletSpawner bulletSpawner) {
        this.bulletSpawner = bulletSpawner;
    }

    /**
     * Sets whether the enemy is firing bullets.
     *
     * @@param isFiringBullets {@code true} if firing bullets, {@code false}
     * otherwise.
     */
    public void setIsFiringBullets(final boolean isFiringBullets) {
        if (bulletSpawner == null) {
            this.isFiringBullets = false;
            return;
        }
        this.isFiringBullets = isFiringBullets;
    }

    /**
     * Updates the x and y velocities of new bullets to aim towards the given
     * target coordinates.
     *
     * @param target
     */
    public void setTarget(final Point target) {
        if (target == null || bulletSpawner == null) {
            return;
        }

        Point startCoords = getCentreCoordinates();
        Dimension bulletSpriteDimensions = bulletSpawner.getBulletSpriteDimensions();
        startCoords.x -= bulletSpriteDimensions.width / 2;
        startCoords.y -= bulletSpriteDimensions.height / 2;

        Bearing2D bearing = new Bearing2D(startCoords.x, startCoords.y, target.x, target.y);
        double radians = Math.toRadians(bearing.getDegrees());

        double currentBulletVelocityX = bulletSpawner.bulletVelocityX;
        double currentBulletVelocityY = bulletSpawner.bulletVelocityY;

        bulletSpawner.setBulletVelocityX(currentBulletVelocityX * Math.cos(radians));
        bulletSpawner.setBulletVelocityY(currentBulletVelocityY * Math.sin(radians));
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
        if (!(obj instanceof Enemy)) {
            return false;
        }
        Enemy other = (Enemy) obj;
        return super.equals(other)
                && Objects.equals(bulletSpawner, other.getBulletSpawner())
                && isFiringBullets == other.isFiringBullets();
    }

    /**
     * Returns a hash code for this entity.
     *
     * @return The hash code of the entity.
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), bulletSpawner, isFiringBullets);
    }

    @Override
    public void move() {
        super.move();
        correctPosition();
    }

    // ----- HELPER METHODS -----
    /**
     * Ensures the enemy remains within the visible screen boundaries.
     */
    private void correctPosition() {
        // Trying to use Math.clamp gave out of bounds issues or something. This is simpler.
        position.x = Math.max(0, Math.min(position.x, panel.getWidth() - getSpriteWidth()));
        position.y = Math.max(0, Math.min(position.y, panel.getHeight() - getSpriteHeight()));
    }

    // ----- STATIC BUILDER FOR ENEMY -----
    public static class EnemyBuilder extends MobileEntityBuilder<EnemyBuilder> {

        // ----- CONSTRUCTOR -----
        public EnemyBuilder(JPanel panel) {
            super(panel);
        }

        // ----- BUSINESS LOGIC METHODS -----
        public Enemy build() {
            return new Enemy(this);
        }
    }
}
