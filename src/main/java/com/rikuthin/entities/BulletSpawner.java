package com.rikuthin.entities;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import com.rikuthin.graphics.animations.AnimationInstance;
import com.rikuthin.graphics.animations.AnimationManager;
import com.rikuthin.graphics.animations.AnimationTemplate;
import com.rikuthin.interfaces.Updateable;

/**
 * Represents an invisible {@link Bullet} spawner controlled by a game
 * {@link Entity}.
 */
public class BulletSpawner implements Updateable {

    /**
     * The {@link Entity} that owns/controls the spawner.
     */
    protected Entity owner;

    /**
     * {@code true} if the spawner is currently creating bullets; {@code false}
     * otherwise.
     */
    protected boolean isSpawning;

    /**
     * How many points of damage the spawned bullets should do.
     */
    protected int bulletDamage;

    /**
     * The number of pixels along the x-axis that a spawned {@link Bullet} will
     * move per frame.
     */
    protected double bulletVelocityX;

    /**
     * The number of pixels along the y-axis that a spawned {@link Bullet} will
     * move per frame.
     */
    protected double bulletVelocityY;

    /**
     * A map of all the animations associated with the spawned bullets, keyed by
     * animation name.
     */
    protected HashSet<String> bulletAnimationKeys;

    /**
     * The key of the spawned bullet's initial active animation.
     */
    protected String currentBulletAnimationKey;

    /**
     * The key of the animation used for spawned bullets.
     */
    protected String currentAnimationKey;

    // ----- CONSTRUCTORS -----
    /**
     * Constructor used to create a BulletSpawner instance.
     *
     * @param builder The builder used to construct the player.
     */
    public BulletSpawner(BulletSpawnerBuilder builder) {
        this.owner = builder.owner;
        setBulletDamage(builder.bulletDamage);
        this.isSpawning = builder.isSpawning;
        this.bulletVelocityX = builder.bulletVelocityX;
        this.bulletVelocityY = builder.bulletVelocityY;
        setBulletAnimationKeys(builder.bulletAnimationKeys);
        setCurrentBulletAnimationKey(builder.currentBulletAnimationKey);
    }

    // ---- GETTERS -----
    /**
     * Returns a reference to the {@link Entity} that owns/controls this
     * spawner.
     *
     * @return The owner entity.
     */
    public Entity getOwner() {
        return owner;
    }

    /**
     * Returns how many point of damage the spawned bullets should do.
     *
     * @return The damage dealt by the spawned bullets.
     */
    public int getBulletDamage() {
        return bulletDamage;
    }

    /**
     * Returns whether the spawner is creating bullets.
     *
     * @return {@code true} if creating bullets, {@code false} otherwise.
     */
    public boolean isSpawning() {
        return isSpawning;
    }

    /**
     * Returns the number of pixels along the x-axis that a spawned
     * {@link Bullet} will move per frame.
     */
    public double getBulletVelocityX() {
        return bulletVelocityX;
    }

    /**
     * Returns the number of pixels along the y-axis that a spawned
     * {@link Bullet} will move per frame.
     */
    public double getBulletVelocityY() {
        return bulletVelocityY;
    }

    /**
     * Returns the set of keys this spawner can use to query
     * {@link Animation Instance} for an {@link AnimationTemplate}.
     *
     * @return The set of bullet animation keys.
     */
    public Set<String> getBulletAnimationKeys() {
        return bulletAnimationKeys;
    }

    /**
     * /**
     * Returns the key associated with the current {@link AnimationInstance}
     * used for a spawned {@link Bullet}.
     *
     * @return The current bullet animation key.
     */
    public String getCurrentBulletAnimatioKey() {
        return currentBulletAnimationKey;
    }

    // ---- SETTERS -----
    public void setBulletDamage(final int bulletDamage) {
        if (bulletDamage < 0) {
            throw new IllegalArgumentException(String.format(
                    "%s: Bullet damage cannot be less than zero (0).",
                    this.getClass().getName()
            ));
        }
        this.bulletDamage = bulletDamage;
    }

    /**
     * Sets whether the spawner should create bullets.
     *
     * @param isSpawning {@code true} if creating bullets, {@code false}
     * otherwise.
     */
    public void setIsSpawning(final boolean isSpawning) {
        this.isSpawning = isSpawning;
    }

    /**
     * Sets the movement speed of spawned bullets along the x-axis in pixels per
     * frame.
     *
     * @param bulletVelocityX The bullet's x-axis velocity.
     */
    public void setBulletVelocityX(final double bulletVelocityX) {
        this.bulletVelocityX = bulletVelocityX;
    }

    /**
     * Sets the movement speed of spawned bullets along the y-axis in pixels per
     * frame.
     *
     * @param bulletVelocityY The bullet's y-axis velocity.
     */
    public void setBulletVelocityY(final double bulletVelocityY) {
        this.bulletVelocityY = bulletVelocityY;
    }

    /**
     * Sets the set of keys this entity can query {@link AnimationManager} with.
     *
     * @param bulletAnimationKeys The set of animation keys.
     * @throws IllegalArgumentException if, when a non-{@code null} set is
     * passed, it contains {@code null} or blank keys, or contains a key that
     * doesn't exist in {@link AnimationManager}'s key set.
     */
    public final void setBulletAnimationKeys(final Set<String> bulletAnimationKeys) throws IllegalArgumentException {
        if (bulletAnimationKeys == null) {
            this.bulletAnimationKeys = new HashSet<>();
            return;
        }

        for (String key : bulletAnimationKeys) {
            if (key == null || key.isBlank()) {
                throw new IllegalArgumentException(String.format(
                        "%s: Keys cannot be null nor empty.",
                        this.getClass().getName()
                ));
            }
            if (AnimationManager.getInstance().getAnimation(key) == null) {
                throw new IllegalArgumentException(String.format(
                        "%s: Keys must exist within AnimationManager's key set.",
                        this.getClass().getName()
                ));
            }
        }

        this.bulletAnimationKeys = new HashSet<>(bulletAnimationKeys);
    }

    /**
     * Sets the animation for the entity.
     * <p>
     * Key must either be {@code null} (for no animation) or a valid (non-blank)
     * string.
     * <p>
     * Valid strings must be a {@link AnimationTemplate} key within
     * {@link AnimationManager} and must exist within the entity's current key
     * set.
     *
     * @param key The key identifying the animation.
     * @throws IllegalAccessException if the provided key (when not
     * {@code null}) is blank, is not within the entity's animation key set, or
     * does not map to a loaded template within {@link AnimationManager}.
     */
    public final void setCurrentBulletAnimationKey(String key) throws IllegalArgumentException {
        if (key == null) {
            currentBulletAnimationKey = null;
            return;
        }

        if (key.isBlank()) {
            throw new IllegalArgumentException(String.format(
                    "%s: Non-null keys cannot be blank.",
                    this.getClass().getName()
            ));
        }

        if (!bulletAnimationKeys.contains(key)) {
            throw new IllegalArgumentException(String.format(
                    "%s: Animation key <'%s'> not found within key set. Please add key to set.",
                    this.getClass().getName(),
                    key
            ));
        }

        AnimationTemplate template = AnimationManager.getInstance().getAnimation(key);
        if (template == null) {
            throw new IllegalArgumentException(String.format(
                    "%s: Could not find template in AnimationManager mapped to key <'%s'>.",
                    this.getClass().getName(),
                    key
            ));
        }
        this.currentAnimationKey = key;
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
        if (!(obj instanceof BulletSpawner)) {
            return false;
        }
        BulletSpawner other = (BulletSpawner) obj;
        return Objects.equals(owner, other.getOwner())
                && isSpawning == other.isSpawning()
                && bulletDamage == other.bulletDamage
                && Double.compare(bulletVelocityX, other.getBulletVelocityX()) == 0
                && Double.compare(bulletVelocityY, other.getBulletVelocityY()) == 0
                && Objects.equals(bulletAnimationKeys, other.getBulletAnimationKeys())
                && Objects.equals(currentBulletAnimationKey, other.getCurrentBulletAnimatioKey());
    }

    /**
     * Returns a hash code for this entity.
     *
     * @return The hash code of the entity.
     */
    @Override
    public int hashCode() {
        return Objects.hash(
                isSpawning,
                bulletDamage,
                bulletVelocityX,
                bulletVelocityY,
                bulletAnimationKeys,
                currentBulletAnimationKey
        );
    }

    /**
     * Updates the entity's state.
     */
    @Override
    public void update() {
        if (isSpawning) {
            // Bullet newBullet = Bullet()
        }
    }

    // ----- BUILDER FOR BULLET SPAWNER -----
    /**
     * The EntityBuilder class provides a fluent API for constructing an Entity
     * object.
     */
    protected static class BulletSpawnerBuilder {

        // ----- INSTANCE VARIABLES -----
        /**
         * The {@link Entity} that owns/controls the spawner.
         */
        protected Entity owner = null;

        /**
         * {@code true} if the spawner is currently creating bullets;
         * {@code false} otherwise.
         */
        protected boolean isSpawning = false;

        /**
         * How many points of damage the spawned bullets should do.
         */
        protected int bulletDamage = 0;

        /**
         * The number of pixels along the x-axis that a spawned {@link Bullet}
         * will move per frame.
         */
        protected double bulletVelocityX = 0;

        /**
         * The number of pixels along the y-axis that a spawned {@link Bullet}
         * will move per frame.
         */
        protected double bulletVelocityY = 0;

        /**
         * A map of all the animations associated with the spawned bullets,
         * keyed by animation name.
         */
        protected HashSet<String> bulletAnimationKeys = new HashSet<>();

        /**
         * The key of the spawned bullet's initial active animation.
         */
        protected String currentBulletAnimationKey = null;

        // ------ CONSTRUCTORS -----
        public BulletSpawnerBuilder(final Entity owner) {
            if (owner == null) {
                throw new IllegalArgumentException(String.format(
                        "%s: Owner cannot be null.",
                        this.getClass().getName()
                ));
            }
            this.owner = owner;
        }

        // ---- SETTERS -----
        public BulletSpawnerBuilder bulletDamage(final int bulletDamage) {
            this.bulletDamage = bulletDamage;
            return this;
        }

        /**
         * Sets whether the spawner should create bullets.
         *
         * @param isSpawning {@code true} if creating bullets, {@code false}
         * otherwise.
         */
        public BulletSpawnerBuilder isSpawning(final boolean isSpawning) {
            this.isSpawning = isSpawning;
            return this;
        }

        /**
         * Sets the movement speed of spawned bullets along the x-axis in pixels
         * per frame.
         *
         * @param bulletVelocityX The bullet's x-axis velocity.
         */
        public BulletSpawnerBuilder bulletVelocityX(final double bulletVelocityX) {
            this.bulletVelocityX = bulletVelocityX;
            return this;
        }

        /**
         * Sets the movement speed of spawned bullets along the y-axis in pixels
         * per frame.
         *
         * @param bulletVelocityY The bullet's y-axis velocity.
         */
        public BulletSpawnerBuilder bulletVelocityY(final double bulletVelocityY) {
            this.bulletVelocityY = bulletVelocityY;
            return this;
        }

        /**
         * Sets the set of keys this entity can query {@link AnimationManager}
         * with.
         *
         * @param bulletAnimationKeys The set of animation keys.
         */
        public final BulletSpawnerBuilder setAnimationKeys(final Set<String> bulletAnimationKeys) {
            this.bulletAnimationKeys = (bulletAnimationKeys == null) ? new HashSet<>() : new HashSet<>(bulletAnimationKeys);
            return this;
        }

        /**
         * Sets the animation for the entity.
         * <p>
         * Key must either be {@code null} (for no animation) or a valid
         * (non-blank) string.
         * <p>
         * Valid strings must be a {@link AnimationTemplate} key within
         * {@link AnimationManager} and must exist within the entity's current
         * key set.
         *
         * @param key The key identifying the animation.
         */
        public final BulletSpawnerBuilder setCurrentBulletAnimationKey(String key) {
            this.currentBulletAnimationKey = key;
            return this;
        }

        /**
         * Creates a new {@link BulletSpawner} with the set values.
         *
         * @return The new bullet spawner.
         */
        public BulletSpawner build() {
            return new BulletSpawner(this);
        }
    }
}

// package com.rikuthin.entities;
// import java.awt.Point;
// import java.util.HashMap;
// import java.util.Map;
// import com.rikuthin.core.GameManager;
// import com.rikuthin.graphics.animations.AnimationInstance;
// import com.rikuthin.graphics.animations.AnimationTemplate;
// /**
//  * Represents an invisible object used by other entites to shoot bullets.
//  * <p>
//  * Bullets start at the spawner's own position and move at a specified speed in
//  * a specified direction, "dying" once fully off-screen.
//  * <p>
//  * The spawner itself exists at the central coordinates of its host entity and
//  * moves relative to its host's movement.
//  */
//     // ----- INSTANCE VARIABLES -----
//     /**
//      * The {@link Entity} that owns/controls the spawner.
//      */
//     private Entity owner;
//     /**
//      * The navigational bearing (direction) that bullets initially move along
//      * when leaving the blaster. Defaults to 0° (north) upon construction.
//      */
//     /**
//      * The {@link AnimationTemplate} for the spawned bullet's sprite.
//      */
//     private AnimationTemplate bulletAnimationTemplate;
//     /**
//      *
//      * The speed bullets are shot at in pixels per frame. Defaults to 0.
//      */
//     private double bulletSpeed;
//     /**
//      * Whether the spawner is currently spawning bullets. Defaults to false.
//      */
//     private boolean isSpawning;
//     /**
//      * How many milliseconds to wait before spawning more bullets.
//      */
//     private long spawnDelayMs;
//     /**
//      * When the spawner was last updated.
//      */
//     private long lastUpdateTime;
//     /**
//      * How much of the delay has already passed.
//      */
//     // ----- CONSTRUCTORS -----
//     /**
//      * Constructs a new bullet spawner.
//      *
//      * @param panel The parent {@code JPanel} in which the bullets are spawned.
//      * @param position The initial x and y-coordinates.
//      * @param bulletAnimationTemplate The {@link AnimationTemplate} for the
//      * bullet's animated sprite.
//      * @param bearing The bearing/direction to fire bullets in. If note is
//      * provided, defaults to firing string down.
//      * @param bulletSpeed The speed spawned bullets will move at in pixels per
//      * frame.
//      * @param spawnDelayMs How many milliseconds to wait before spawning more
//      * bullets.
//      */
//     public BulletSpawner(
//             final JPanel panel, final Entity host, final Point position, final AnimationTemplate bulletAnimationTemplate,
//             final Bearing2D bearing, final int bulletSpeed, final long spawnDelayMs) {
//         setBulletAnimation(bulletAnimationTemplate);
//         setBearing(bearing);
//         this.owner = host;
//         this.bulletSpeed = Math.abs(bulletSpeed);
//         this.spawnDelayMs = Math.abs(spawnDelayMs);
//         this.lastUpdateTime = System.currentTimeMillis();
//         this.elapsedDelayTime = 0;
//         this.isSpawning = false;
//     // ----- GETTERS -----
//     /**
//      * Returns the {@link Entity} that owns/controls the spawner.
//      *
//      * @return The spawner's owner.
//      */
//     public Entity getOwner() {
//         return owner;
//     /**
//      * Returns the current bearing (direction) of the blaster relative to the
//      * mouse pointer.
//      *
//      * @return the {@link Bearing2D} representing the current bearing
//      */
//     public Bearing2D getBearing() {
//         return bearing;
//     /**
//      * Returns the {@link AnimationTemplate} for the spawned bullet's sprite.
//      *
//      * @return The template for the bullet's sprite.
//      */
//     public AnimationTemplate getBulletAnimationTemplate() {
//         return bulletAnimationTemplate;
//     /**
//      * Returns the speed (in pixels/tick) at which the {@link Bullet} instances
//      * will leave the spawner.
//      *
//      * @return the speed of the bullets
//      */
//     public double getBulletSpeed() {
//         return bulletSpeed;
//     /**
//      * Returns whether the spawner is creating {@link Bullet} instances.
//      *
//      * @return {@code true} if the spawner is creating bullets; {@code false}
//      * otherwise.
//      */
//     public boolean isSpawning() {
//         return isSpawning;
//     /**
//      * Returns how many milliseconds the spawner must wait before spawning more
//      * bullets.
//      *
//      * @return The delay;
//      */
//     public long getSpawnDelayMs() {
//         return spawnDelayMs;
//     /**
//      * Returns how many milliseconds of the delay have passed.
//      *
//      * @return The elasped time.
//      */
//     private long getElapsedDelayTime() {
//         return elapsedDelayTime;
//     /**
//      * Returns the last time the spawner was updated in milliseconds.
//      *
//      * @return The last update time.
//      */
//     private long getLastUpdateTime() {
//         return lastUpdateTime;
//     // ----- SETTERS -----
//     /**
//      * Sets the {@link AnimationTemplate} for the spawned {@link Bullet}
//      * instance's sprite animation.
//      *
//      * @param bulletAnimationTemplate The template for the bullet's sprite
//      * animation.
//      */
//     public final void setBulletAnimation(final AnimationTemplate bulletAnimationTemplate) {
//         this.bulletAnimationTemplate = bulletAnimationTemplate;
//     /**
//      * Sets the bearing (direction) of the {@link Bullet} instances leaving the
//      * spawner.
//      * <p>
//      * If the passed value is {@code null}, defaults to aiming straight down.
//      *
//      * @param bearing the new {@link Bearing2D} direction
//      */
//     public final void setBearing(Bearing2D bearing) {
//         this.bearing = bearing != null ? bearing : new Bearing2D(0, 0, 0, -20);
//     /**
//      * Sets the speed (in pixels/tick) of the {@link Bullet} instances leaving
//      * the spawner.
//      *
//      * @param bulletSpeed the new bullet speed
//      */
//     public void setBulletSpeed(double bulletSpeed) {
//         this.bulletSpeed = Math.abs(bulletSpeed);
//     /**
//      * Sets how many milliseconds the spawner must wait before creating more
//      * {@link Bullet} instances.
//      *
//      * @param spawnDelayMs The delay.
//      */
//     public void setSpawnDelayMs(final long spawnDelayMs) {
//         this.spawnDelayMs = Math.abs(spawnDelayMs);
//     // ----- BUSINESS LOGIC METHODS -----
//     /**
//      * Begins the spawning of {@link Bullet} instances.
//      */
//     public void start() {
//         isSpawning = true;
//         lastUpdateTime = System.currentTimeMillis();
//         elapsedDelayTime = 0;
//     /**
//      * Stops the spawning of {@link bullet} instances.
//      */
//     public void stop() {
//         isSpawning = false;
//     /**
//      * Spawns a new {@link Bullet} instance using the current stored values.
//      * <p>
//      * Following creation, the new bullet is additionally added to the
//      * GameManager's managed list of bullets.
//      *
//      * @return the newly created bullet.
//      */
//     public Bullet spawnBullet() {
//         Bullet bullet = new Bullet(panel, position, bulletAnimationTemplate, bearing, bulletSpeed);
//         GameManager.getInstance().addBullet(bullet);
//         return bullet;
//     // ----- OVERRIDDEN METHODS -----
//     /**
//      * Compares this {@link BulletSpawner} with another object for equality.
//      * <p>
//      * Extends {@code equals()} from the {@link Entity} class by comparing
//      * spawner-related attributes such at firing bearing/direction, bullet
//      * animation template, shot speed, and spawn delay.
//      *
//      * @param obj the object to compare with
//      * @return {@code true} if the objects are equal; {@code false} otherwise
//      */
//     @Override
//     public boolean equals(Object obj) {
//         if (this == obj) {
//             return true;
//         if (!(obj instanceof BulletSpawner)) {
//             return false;
//         BulletSpawner other = (BulletSpawner) obj;
//         return super.equals(obj)
//                 && Objects.equals(owner, other.getOwner())
//                 && Objects.equals(bearing, other.getBearing())
//                 && Objects.equals(bulletAnimationTemplate, other.getBulletAnimationTemplate())
//                 && java.lang.Double.compare(bulletSpeed, other.getBulletSpeed()) == 0
//                 && Boolean.compare(isSpawning, other.isSpawning()) == 0
//                 && Long.compare(spawnDelayMs, other.getSpawnDelayMs()) == 0
//                 && Long.compare(lastUpdateTime, other.getLastUpdateTime()) == 0
//                 && Long.compare(elapsedDelayTime, other.getElapsedDelayTime()) == 0;
//     /**
//      * Computes the hash code for this spawner entity.
//      *
//      * Extends {@code hashCode()} from the {@link Entity} class by incorporating
//      * spawner attributes such as bullet animation template, firing
//      * bearing/direction, bullet speed, and spawn delay.
//      *
//      * @return the hash code of this blaster
//      */
//     @Override
//     public int hashCode() {
//         return Objects.hash(super.hashCode(),
//                 bulletAnimationTemplate,
//                 bearing,
//                 bulletSpeed
//         );
//     /**
//      * Updates the bullet spawner based on elapsed time.
//      */
//     @Override
//     public void update() {
//         if (!isSpawning) {
//             return;
//         long currentTime = System.currentTimeMillis();
//         elapsedDelayTime += currentTime - lastUpdateTime;
//         while (elapsedDelayTime >= spawnDelayMs) {
//             spawnBullet();
//             elapsedDelayTime -= spawnDelayMs; // Ensures correct timing
//         }
//     }

    
// }
