package com.rikuthin.entities;

import java.util.Objects;

import javax.swing.JPanel;

/**
 * Represents a generic bullet that moves within a JPanel.
 */
public class Bullet extends MobileEntity {

    // ----- INSTANCE VARIABLES -----
    /**
     * The {@link Entity} that owns/controls the {@link BulletSpawner} that
     * created this bullet.
     */
    protected Entity owner;
    /**
     * How many points of damage the bullet should deal to entities it collides
     * with.
     */
    protected int damage;

    // ----- CONSTRUCTORS -----
    /**
     * Constructor used to create a BulletSpawner instance.
     *
     * @param builder The builder used to construct the player.
     */
    public Bullet(BulletBuilder builder) {
        super(builder);
        this.owner = builder.owner;
        setDamage(builder.damage);
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
     * Returns how many points of damage the bullets are dealt to entities this
     * bullet collides with.
     *
     * @return The damage dealt.
     */
    public int getDamage() {
        return damage;
    }

    // ---- SETTERS -----
    public void setDamage(final int damage) {
        if (damage < 0) {
            throw new IllegalArgumentException(String.format(
                    "%s: Damage cannot be less than zero (0).",
                    this.getClass().getName()
            ));
        }
        this.damage = damage;
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
        if (!(obj instanceof Bullet)) {
            return false;
        }
        Bullet other = (Bullet) obj;
        return super.equals(other)
                && damage == other.getDamage();
    }

    /**
     * Returns a hash code for this entity.
     *
     * @return The hash code of the entity.
     */
    @Override
    public int hashCode() {
        return Objects.hash(
                super.hashCode(),
                damage
        );
    }

    // ----- STATIC BUILDER FOR BULLET -----
    /**
     * The BulletBuilder class provides a fluent API for constructing a Bullet
     * object.
     */
    protected static class BulletBuilder extends MobileEntityBuilder {

        // ----- INSTANCE VARIABLES -----
        /**
         * The {@link Entity} that owns/controls the spawner.
         */
        protected Entity owner = null;

        /**
         * How many points of damage the spawned bullets should do.
         */
        protected int damage = 0;

        // ------ CONSTRUCTORS -----
        public BulletBuilder(final JPanel panel, final Entity owner) {
            super(panel);

            if (owner == null) {
                throw new IllegalArgumentException(String.format(
                        "%s: Owner cannot be null.",
                        this.getClass().getName()
                ));
            }
            this.owner = owner;
        }

        // ---- SETTERS -----
        public BulletBuilder damage(final int damage) {
            this.damage = damage;
            return this;
        }

        /**
         * Creates a new {@link BulletSpawner} with the set values.
         *
         * @return The new bullet spawner.
         */
        public Bullet build() {
            return new Bullet(this);
        }
    }
}

//     /**
//      * Constructs a new {@link Bullet} with an animated sprite.
//      *
//      * @param panel The {@link JPanel} to which the bullet belongs.
//      * @param position The initial x and y-coordinates.
//      * @param animationTemplate The {@AnimationTemplate} for the bullet's
//      * sprite.
//      * @param bearing The direction the bullet should move in.
//      * @param speed The bullet's movement speed in pixels per frame.
//      */
//     public Bullet(final JPanel panel, final Point position, final AnimationTemplate animationTemplate, final Bearing2D bearing, final double speed) {
//         super(panel, position, animationTemplate, false, true, 0, speed);
//         this.bearing = bearing;
//     }
//     // ----- GETTERS -----
//     /**
//      * Returns whether the movement speed is currently 0.
//      *
//      * @return {@code true} if the movement speed is zero; {@code false}
//      * otherwise.
//      */
//     public boolean isMoving() {
//         return speed != 0;
//     }
//     /**
//      * Returns the direction the bullet is moving in.
//      *
//      * @return The movement direction.
//      */
//     public Bearing2D getBearing() {
//         return bearing;
//     }
//     // ----- SETTERS -----
//     /**
//      * Sets the direction the bullet should move in.
//      *
//      * @param bearing The movement direction.
//      */
//     public void setBearing(final Bearing2D bearing) {
//         this.bearing = bearing;
//     }
//     // ----- BUSINESS LOGIC METHODS -----
//     /**
//      * Moves the bullet based on its bearing and speed.
//      */
//     public void move() {
//         if (!isMoving()) {
//             return;
//         }
//         final double radians = Math.toRadians(bearing.getDegrees());
//         position.x += Math.floor(speed * Math.cos(radians));
//         position.y -= Math.floor(speed * Math.sin(radians)); // Inverted for screen coordinates
//     }
//     // ----- OVERRIDDEN METHODS -----
//     /**
//      * Compares this bullet entity to another object for equality.
//      * <p>
//      * Extends {@code equals()} from the {@link Entity} class by comparing
//      * movement-related attributes such as speed and directional states.
//      *
//      * @param obj The object to compare with.
//      * @return {@code true} if the objects are equal, otherwise {@code false}.
//      */
//     @Override
//     public boolean equals(Object obj) {
//         if (this == obj) {
//             return true;
//         }
//         if (!(obj instanceof Bullet)) {
//             return false;
//         }
//         Bullet other = (Bullet) obj;
//         return super.equals(obj)
//                 && Objects.equals(bearing, other.getBearing())
//                 && Double.compare(speed, other.speed) == 0;
//     }
//     /**
//      * Computes the hash code for this bullet entity.
//      *
//      * Extends {@code hashCode()} from the {@link Entity} class by incorporating
//      * movement attributes such as speed and directional states.
//      *
//      * @return The computed hash code.
//      */
//     @Override
//     public int hashCode() {
//         return Objects.hash(
//                 super.hashCode(),
//                 bearing,
//                 speed
//         );
//     }
//     @Override
//     public void update() {
//         super.update();
//         move();
//         if (animation != null) {
//             animation.update();
//         }
//     }
// }
