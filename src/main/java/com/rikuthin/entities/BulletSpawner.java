package com.rikuthin.entities;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.swing.JPanel;

import com.rikuthin.core.GameManager;
import com.rikuthin.graphics.animations.AnimationInstance;
import com.rikuthin.graphics.animations.AnimationManager;
import com.rikuthin.graphics.animations.AnimationTemplate;
import com.rikuthin.interfaces.Updateable;

/**
 * Represents an invisible {@link Bullet} spawner controlled by a game
 * {@link Entity}.
 */
public class BulletSpawner extends Entity {

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
     * Constructor used to create a BulletSpawner instance.
     *
     * @param builder The builder used to construct the player.
     */
    public BulletSpawner(BulletSpawnerBuilder builder) {
        super(builder);

        this.owner = builder.owner;
        setBulletDamage(builder.bulletDamage);
        this.isSpawning = builder.isSpawning;
        this.bulletVelocityX = builder.bulletVelocityX;
        this.bulletVelocityY = builder.bulletVelocityY;
        setBulletAnimationKeys(builder.bulletAnimationKeys);
        setCurrentBulletAnimationKey(builder.currentBulletAnimationKey);
        this.spawnDelayMs = Math.abs(spawnDelayMs);
        this.lastUpdateTime = System.currentTimeMillis();
        this.elapsedDelayTime = 0;
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
    public final void setBulletDamage(final int bulletDamage) {
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

    // ----- BUSINESS LOGIC METHODS -----
    public Dimension getBulletSpriteDimensions() {
        if (currentAnimationKey == null) {
            return new Dimension(0, 0);
        }

        AnimationInstance bulletAnimation = new AnimationInstance(
                AnimationManager.getInstance().getAnimation(currentAnimationKey)
        );

        BufferedImage bulletSprite = bulletAnimation.getCurrentFrameImage();
        if (bulletSprite != null) {
            return new Dimension(bulletSprite.getWidth(), bulletSprite.getHeight());
        }
        return new Dimension(0, 0);
    }

    /**
     * // * Begins the spawning of {@link Bullet} instances. //
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
        Bullet bullet = new Bullet.BulletBuilder(panel, owner)
                .position(position)
                .invisibility(true)
                .damage(bulletDamage)
                .velocityX(bulletVelocityX)
                .velocityY(bulletVelocityY)
                .animationKeys(bulletAnimationKeys)
                .currentAnimationKey(currentBulletAnimationKey)
                .build();
        GameManager.getInstance().addBullet(bullet);
        return bullet;
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
            long currentTime = System.currentTimeMillis();
            elapsedDelayTime += currentTime - lastUpdateTime;
            while (elapsedDelayTime >= spawnDelayMs) {
                spawnBullet();
                elapsedDelayTime -= spawnDelayMs; // Ensures correct timing
            }
        }
    }

    // ----- BUILDER FOR BULLET SPAWNER -----
    /**
     * The EntityBuilder class provides a fluent API for constructing an Entity
     * object.
     */
    public static class BulletSpawnerBuilder extends EntityBuilder<BulletSpawnerBuilder> {

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
        public BulletSpawnerBuilder(final JPanel panel, final Entity owner) {
            super(panel);

            if (owner == null) {
                throw new IllegalArgumentException(String.format(
                        "%s: Owner cannot be null.",
                        this.getClass().getName()
                ));
            }
            this.owner = owner;
            this.isSpawning = false;
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
        public final BulletSpawnerBuilder bulletAnimationKeys(final Set<String> bulletAnimationKeys) {
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
        public final BulletSpawnerBuilder currentBulletAnimationKey(String key) {
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
