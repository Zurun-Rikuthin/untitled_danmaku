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
     * The URL for the spawned bullet's sprite. Used if no animation is set.
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
    private double bulletSpeed;
    /**
     * Whether the spawner is currently spawning bullets. Defaults to false.
     */
    private boolean isSpawning;
    /**
     * How many milliseconds to wait before spawning more bullets.
     */
    private long spawnDelayMs;
    /**
     * When the spawner was last updated.
     */
    private long lastUpdateTime;
    /**
     * How much of the delay has already passed.
     */
    private long elapsedDelayTime;

    // ----- CONSTRUCTORS -----
    /**
     * Constructs a new bullet spawner.
     *
     * @param panel The parent {@code JPanel} in which the bullets are spawned.
     * @param x The initial x-coordinate.
     * @param y The initial y-coordinate.
     * @param bulletSpriteUrl The URL for the bullet's static sprite. Used if no
     * animation is provided.
     * @param bulletAnimation The animation for the bullet's sprite.
     * @param bulletSpeed The speed spawned bullets will move at in pixels per
     * frame.
     * @param shotDelayMs How many milliseconds to wait before spawning more
     * bullets.
     */
    public BulletSpawner(final JPanel panel, final int x, final int y,
            final String bulletSpriteUrl, final Animation bulletAnimation,
            final int bulletSpeed, final long spawnDelayMs) {
        super(panel, x, y, null, true, false);

        if (bulletAnimation == null && (bulletSpriteUrl == null || bulletSpriteUrl.isEmpty())) {
            throw new IllegalArgumentException(String.format(
                    "%s: Must have either a sprite URL or an animation.",
                    this.getClass().getName()
            ));
        }

        this.bulletSpriteUrl = bulletSpriteUrl;
        this.bulletAnimation = bulletAnimation;
        this.bulletSpeed = Math.abs(bulletSpeed);
        this.spawnDelayMs = Math.abs(spawnDelayMs);
        this.lastUpdateTime = System.currentTimeMillis();
        this.elapsedDelayTime = 0;

        if (bulletAnimation != null) {
            this.sprite = bulletAnimation.getCurrentFrameImage();
        }
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
     * Returns the animation for the spawned bullet's sprite.
     *
     * @return The bullet's sprite animation.
     */
    public Animation getBulletAnimation() {
        return bulletAnimation;
    }

    /**
     * Returns the speed (in pixels/tick) at which the bullets will move.
     *
     * @return the speed of the bullets
     */
    public double getBulletSpeed() {
        return bulletSpeed;
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

    /**
     * Returns how many milliseconds the spawner must wait before spawning more
     * bullets.
     *
     * @return The delay;
     */
    public long getSpawnDelayMs() {
        return spawnDelayMs;
    }

    /**
     * Returns how many milliseconds of the delay have passed.
     *
     * @return The elasped time.
     */
    private long getElapsedDelayTime() {
        return elapsedDelayTime;
    }

    /**
     * Returns the last time the spawner was updated in milliseconds.
     *
     * @return The last update time.
     */
    private long getLastUpdateTime() {
        return lastUpdateTime;
    }

    // ----- SETTERS -----
    /**
     * Sets the URL for the spawned bullet's static sprite.
     *
     * @param bulletType The bullet's sprite URL.
     */
    public void setBulletSpriteUrl(final String bulletSpriteUrl) {
        this.bulletSpriteUrl = bulletSpriteUrl;
    }

    /**
     * Sets the animation for the spawned bullet's sprite.
     *
     * @param bulletAnimation The bullet's sprite animation.
     */
    public void setBulletAnimation(final Animation bulletAnimation) {
        this.bulletAnimation = bulletAnimation;
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
     * Sets the speed (in pixels/tick) of the bullets that will be shot from the
     * blaster.
     *
     * @param shotSpeed the new bullet speed
     */
    public void setBulletSpeed(double shotSpeed) {
        this.bulletSpeed = Math.abs(shotSpeed);
    }

    /**
     * Sets how many milliseconds the spawner must wait before creating more
     * bullets.
     *
     * @param spawnDelayMs The delay.
     */
    public void setSpawnDelayMs(final long spawnDelayMs) {
        this.spawnDelayMs = Math.abs(spawnDelayMs);
    }

    // ----- BUSINESS LOGIC METHODS -----
    /**
     * Begins the spawning of bullets.
     */
    public void start() {
        isSpawning = true;
        lastUpdateTime = System.currentTimeMillis();
        elapsedDelayTime = 0;
    }

    /**
     * Stops the spawning of bullets.
     */
    public void stop() {
        isSpawning = false;
    }

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
            return new Bullet(panel, x, y, bulletAnimation, bearing, bulletSpeed);
        } else {
            return new Bullet(panel, x, y, bulletSpriteUrl, bearing, bulletSpeed);
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
     * spawner-related attributes such at firing bearing/direction, bullet type,
     * shot speed, and spawn delay.
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
                && Objects.equals(bearing, other.getBearing())
                && Objects.equals(bulletSpriteUrl, other.getBulletSpriteUrl())
                && Objects.equals(bulletAnimation, other.getBulletAnimation())
                && java.lang.Double.compare(bulletSpeed, other.getBulletSpeed()) == 0
                && Boolean.compare(isSpawning, other.isSpawning()) == 0
                && Long.compare(spawnDelayMs, other.getSpawnDelayMs()) == 0
                && Long.compare(lastUpdateTime, other.getLastUpdateTime()) == 0
                && Long.compare(elapsedDelayTime, other.getElapsedDelayTime()) == 0;
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
                bulletSpeed
        );
    }

    /**
     * Updates the bullet spawner based on elapsed time.
     */
    @Override
    public void update() {
        super.update();

        if (!isSpawning) {
            return;
        }

        long currentTime = System.currentTimeMillis();
        elapsedDelayTime += currentTime - lastUpdateTime;
        lastUpdateTime = currentTime;

        if (elapsedDelayTime >= spawnDelayMs) {
            elapsedDelayTime -= spawnDelayMs;
            spawnBullet();
        }
    }
}
