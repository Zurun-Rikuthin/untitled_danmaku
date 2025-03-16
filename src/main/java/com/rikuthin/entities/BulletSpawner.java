package com.rikuthin.entities;

import java.awt.Point;
import java.util.Objects;

import javax.swing.JPanel;

import com.rikuthin.core.GameManager;
import com.rikuthin.graphics.animations.AnimationTemplate;
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
     * The {@link AnimationTemplate} for the spawned bullet's sprite.
     */
    private AnimationTemplate bulletAnimationTemplate;
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
     * @param position The initial x and y-coordinates.
     * @param bulletAnimationTemplate The {@link AnimationTemplate} for the
     * bullet's animated sprite.
     * @param bearing The bearing/direction to fire bullets in. If note is
     * provided, defaults to firing string down.
     * @param bulletSpeed The speed spawned bullets will move at in pixels per
     * frame.
     * @param spawnDelayMs How many milliseconds to wait before spawning more
     * bullets.
     */
    public BulletSpawner(
            final JPanel panel, final Point position, final AnimationTemplate bulletAnimationTemplate,
            final Bearing2D bearing, final int bulletSpeed, final long spawnDelayMs) {
        super(panel, position, null, true, false, 0, 0);
                
        setBulletAnimation(bulletAnimationTemplate);
        setBearing(bearing);
        this.bulletSpeed = Math.abs(bulletSpeed);
        this.spawnDelayMs = Math.abs(spawnDelayMs);
        this.lastUpdateTime = System.currentTimeMillis();
        this.elapsedDelayTime = 0;
        this.isSpawning = false;
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
     * Returns the {@link AnimationTemplate} for the spawned bullet's sprite.
     *
     * @return The template for the bullet's sprite.
     */
    public AnimationTemplate getBulletAnimationTemplate() {
        return bulletAnimationTemplate;
    }

    /**
     * Returns the speed (in pixels/tick) at which the {@link Bullet} instances
     * will leave the spawner.
     *
     * @return the speed of the bullets
     */
    public double getBulletSpeed() {
        return bulletSpeed;
    }

    /**
     * Returns whether the spawner is creating {@link Bullet} instances.
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
     * Sets the {@link AnimationTemplate} for the spawned {@link Bullet}
     * instance's sprite animation.
     *
     * @param bulletAnimationTemplate The template for the bullet's sprite
     * animation.
     */
    public final void setBulletAnimation(final AnimationTemplate bulletAnimationTemplate) {
        this.bulletAnimationTemplate = bulletAnimationTemplate;
    }

    /**
     * Sets the bearing (direction) of the {@link Bullet} instances leaving the
     * spawner.
     * <p>
     * If the passed value is {@code null}, defaults to aiming straight down.
     *
     * @param bearing the new {@link Bearing2D} direction
     */
    public final void setBearing(Bearing2D bearing) {
        this.bearing = bearing != null ? bearing : new Bearing2D(0, 0, 0, -20);
    }

    /**
     * Sets the speed (in pixels/tick) of the {@link Bullet} instances leaving
     * the spawner.
     *
     * @param bulletSpeed the new bullet speed
     */
    public void setBulletSpeed(double bulletSpeed) {
        this.bulletSpeed = Math.abs(bulletSpeed);
    }

    /**
     * Sets how many milliseconds the spawner must wait before creating more
     * {@link Bullet} instances.
     *
     * @param spawnDelayMs The delay.
     */
    public void setSpawnDelayMs(final long spawnDelayMs) {
        this.spawnDelayMs = Math.abs(spawnDelayMs);
    }

    // ----- BUSINESS LOGIC METHODS -----
    /**
     * Begins the spawning of {@link Bullet} instances.
     */
    public void start() {
        isSpawning = true;
        lastUpdateTime = System.currentTimeMillis();
        elapsedDelayTime = 0;
    }

    /**
     * Stops the spawning of {@link bullet} instances.
     */
    public void stop() {
        isSpawning = false;
    }

    /**
     * Spawns a new {@link Bullet} instance using the current stored values.
     * <p>
     * Following creation, the new bullet is additionally added to the
     * GameManager's managed list of bullets.
     *
     * @return the newly created bullet.
     */
    public Bullet spawnBullet() {
        Bullet bullet = new Bullet(panel, position, bulletAnimationTemplate, bearing, bulletSpeed);
        GameManager.getInstance().addBullet(bullet);
        return bullet;
    }

    // ----- OVERRIDDEN METHODS -----
    /**
     * Compares this {@link BulletSpawner} with another object for equality.
     * <p>
     * Extends {@code equals()} from the {@link Entity} class by comparing
     * spawner-related attributes such at firing bearing/direction, bullet
     * animation template, shot speed, and spawn delay.
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
                && Objects.equals(bulletAnimationTemplate, other.getBulletAnimationTemplate())
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
     * spawner attributes such as bullet animation template, firing
     * bearing/direction, bullet speed, and spawn delay.
     *
     * @return the hash code of this blaster
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(),
                bulletAnimationTemplate,
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

        while (elapsedDelayTime >= spawnDelayMs) {
            spawnBullet();
            elapsedDelayTime -= spawnDelayMs; // Ensures correct timing
        }
    }
}
