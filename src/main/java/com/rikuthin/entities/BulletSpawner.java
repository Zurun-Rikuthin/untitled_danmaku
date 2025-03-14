package com.rikuthin.entities;

import java.util.Objects;

import javax.swing.JPanel;

import com.rikuthin.graphics.Animation;
import com.rikuthin.utility.Bearing2D;

/**
 * Represents an invisible object used by other entites to shoot bullets.
 * <p>
 * Bullets start at the spawner's own position and move at a specified speed in
 * a specified direction, "dying" once fully off-screen.
 * <p>
 * The spawner itself exists at the central coordinates of its host entity and
 * moves relative to its host's movement.
 */
public class BulletSpawner extends Entity {

    // ----- INSTANCE VARIABLES -----
    /**
     * The navigational bearing (direction) that bullets initially move along
     * when leaving the blaster. Defaults to 0° (north) upon construction.
     */
    private Bearing2D bearing;

    /**
     * The URL for the spawned bullet's sprite.
     */
    private String bulletSpriteUrl;

    /**
     * The spawned bullet's sprite animation.
     */
    private Animation bulletAnimation;
    /**
     *
     * The speed bullets are shot at in pixels per frame. Defaults to 0.
     */
    private double shotSpeed;
    /**
     * Whether the spawner is currently spawning bullets. Defaults to false.
     */
    private boolean isSpawning;

    // ----- CONSTRUCTORS -----
    /**
     * Constructs a new Spawner for bullets with static sprites.
     *
     * @param panel The parent {@code JPanel} in which the bullets are spawned.
     * @param x The initial x-coordinate.
     * @param y The initial y-coordinate.
     * @param bulletSpriteUrl The type of bullets spawned.
     * @param shotSpeed The speed spawned bullets will move at in pixels per
     * frame.
     * @param isSpawning {@code true} if spawning should begin immediately upon
     * construction; {@code false otherwise.
     */
    public BulletSpawner(final JPanel panel, final int x, final int y, final String bulletSpriteUrl, final int shotSpeed, final boolean isSpawning) {
        super(panel, x, y, null, true, false);
        this.bulletSpriteUrl = bulletSpriteUrl;
        this.shotSpeed = shotSpeed;
        this.isSpawning = isSpawning;
    }

    /**
     * Constructs a new Spawner for bullets with animated sprites.
     *
     * @param panel The parent {@code JPanel} in which the bullets are spawned.
     * @param x The initial x-coordinate.
     * @param y The initial y-coordinate.
     * @param bulletAnimation The type of bullets spawned.
     * @param shotSpeed The speed spawned bullets will move at in pixels per
     * frame.
     * @param isSpawning {@code true} if spawning should begin immediately upon
     * construction; {@code false otherwise.
     */
    public BulletSpawner(final JPanel panel, final int x, final int y, final Animation bulletAnimation, final int shotSpeed, final boolean isSpawning) {
        super(panel, x, y, null, true, false);

        if (animation == null || animation.isEmpty()) {
            throw new IllegalArgumentException(String.format(
                    "%s: Must provide an animation.",
                    this.getClass().getName()
            ));
        }

        this.bulletAnimation = bulletAnimation;
        this.sprite = animation.getCurrentFrameImage();
        this.shotSpeed = shotSpeed;
        this.isSpawning = isSpawning;
    }

    // ----- GETTERS -----
    /**
     * Returns the current bearing (direction) of the blaster relative to the
     * mouse pointer.
     *
     * @return the {@link Bearing2D} representing the current bearing
     */
    public Bearing2D getBearing() {
        return bearing;
    }

    /**
     * Returns the sprite URL for the spawned bullet's sprite.
     *
     * @return The bullet's sprite URL.
     */
    public String getBulletSpriteUrl() {
        return bulletSpriteUrl;
    }

    /**
     * Returns the speed (in pixels/tick) at which the bubbles will move.
     *
     * @return the speed of the bubbles
     */
    public double getShotSpeed() {
        return shotSpeed;
    }

    /**
     * Returns whether the spawner is creating bullets.
     *
     * @return {@code true} if the spawner is creating bullets; {@code false}
     * otherwise.
     */
    public boolean isSpawning() {
        return isSpawning;
    }

    // ----- SETTERS -----
    /**
     * Sets the URL for the spawned bullet's sprite.
     *
     * @param bulletType The bullet's sprite URL.
     */
    public void setBulletSpriteUrl(final String bulletSpriteUrl) {
        this.bulletSpriteUrl = bulletSpriteUrl;
    }

    /**
     * Sets the bearing (direction) of the blaster's movement.
     *
     * @param bearing the new {@link Bearing2D} direction
     */
    public void setBearing(Bearing2D bearing) {
        this.bearing = bearing;
    }

    /**
     * Sets the speed (in pixels/tick) of the bubbles that will be shot from the
     * blaster.
     *
     * @param shotSpeed the new bubble speed
     */
    public void setShotSpeed(double shotSpeed) {
        this.shotSpeed = shotSpeed;
    }

    /**
     * Sets whether the spawner should create bullets.
     *
     * @param isSpawning {@code true} if bullets should be spawned;
     * {@code false} otherwise.
     */
    public void setSpawning(final boolean isSpawning) {
        this.isSpawning = isSpawning;
    }

    // ----- BUSINESS LOGIC METHODS -----
    /**
     * Spawns a new bullet instance using the current stored values.
     * <p>
     * The bullet is created at the spawner's position and moves along the given
     * bearing.
     * <p>
     * The creation of animated bullets is prioritised. If no bullet animation
     * is set, the spawner will use the set static sprite.
     *
     * @return the newly created {@link Bullet} instance
     */
    public Bullet spawnBullet() {
        if (bulletAnimation != null) {
            return new Bullet(panel, x, y, bulletAnimation, bearing, shotSpeed);
        } else {
            return new Bullet(panel, x, y, bulletSpriteUrl, bearing, shotSpeed);
        }
    }

    /**
     * Spawns a new bullet instance using the provided values.
     * <p>
     * The bullet is created at the spawner's position and moves along the given
     * bearing.
     * </p>
     *
     * @param bulletSpriteUrl The URL for the spawned bullet's sprite.
     * @param bearing The direction the bullet will move in.
     * @param shotSpeed The movement speed of the new bullet in pixels per
     * frame.
     * @return the newly created {@link Bullet} instance
     */
    public Bullet spawnBullet(final String bulletSpriteUrl, final Bearing2D bearing, final double shotSpeed) {
        return new Bullet(panel, x, y, bulletSpriteUrl, bearing, shotSpeed);
    }

    // ----- OVERRIDDEN METHODS -----
    /**
     * Compares this spawner with another object for equality.
     * <p>
     * Extends {@code equals()} from the {@link Entity} class by comparing
     * spawner-related attributes such at firining bearing/direction, bullet
     * type, and shot speed.
     *
     * @param obj the object to compare with
     * @return {@code true} if the objects are equal; {@code false} otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof BulletSpawner)) {
            return false;
        }

        BulletSpawner other = (BulletSpawner) obj;
        return super.equals(obj)
                && bulletSpriteUrl.equals(other.bulletSpriteUrl)
                && bearing.equals(other.bearing)
                && java.lang.Double.compare(shotSpeed, other.shotSpeed) == 0;
    }

    /**
     * Computes the hash code for this spawner entity.
     *
     * Extends {@code hashCode()} from the {@link Entity} class by incorporating
     * spawner attributes such as bullet type, firing bearing/direction, and
     * shot speed.
     *
     * @return the hash code of this blaster
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(),
                bulletSpriteUrl,
                bearing,
                shotSpeed
        );
    }
}
