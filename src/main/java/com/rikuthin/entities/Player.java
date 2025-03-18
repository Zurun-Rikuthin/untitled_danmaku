package com.rikuthin.entities;

import java.util.Objects;

import javax.swing.JPanel;

/**
 * Represents a player controlled character in the game.
 */
public class Player extends MobileEntity {

    protected boolean isFiringBullets;

    // ----- CONSTRUCTORS -----
    /**
     * Constructor used to create a Player instance.
     *
     * @param builder The builder used to construct the player.
     */
    protected Player(PlayerBuilder builder) {
        super(builder);
        this.isFiringBullets = false;
    }

    // ---- GETTERS -----
    public boolean isFiringBullets() {
        return isFiringBullets;
    }

    // ---- SETTERS -----
    public void setIsFiringBullets(final boolean isFiringBullets) {
        this.isFiringBullets = isFiringBullets;
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
        if (!(obj instanceof Player)) {
            return false;
        }
        Player other = (Player) obj;
        return super.equals(other) && isFiringBullets == other.isFiringBullets();
    }

    /**
     * Returns a hash code for this entity.
     *
     * @return The hash code of the entity.
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), isFiringBullets);
    }

    // ----- STATIC BUILDER FOR PLAYER -----
    public static class PlayerBuilder extends MobileEntityBuilder<PlayerBuilder> {

        // ----- INSTANCE VARIABLES -----
        // ----- CONSTRUCTOR -----
        public PlayerBuilder(final JPanel panel) {
            super(panel);
        }

        // ---- SETTERS -----
        // ----- BUSINESS LOGIC METHODS -----
        public Player build() {
            return new Player(this);
        }
    }
}

// import java.awt.Point;
// import java.util.Objects;
// import javax.swing.JPanel;
// import com.rikuthin.graphics.animations.AnimationManager;
// import com.rikuthin.graphics.animations.AnimationTemplate;
// import com.rikuthin.utility.Bearing2D;
// /**
//  * This class represents the player-controlled character and defines how it
//  * interacts with the game world.
//  */
// public class Player extends MobileEntity {
//     // ----- INSTANCE VARIABLES -----
//     /**
//      * Flags indicating movement direction based on player input. Defaults to
//      * {@code false}.
//      */
//     private boolean movingUp;
//     private boolean movingDown;
//     private boolean movingLeft;
//     private boolean movingRight;
//     /**
//      * The player's bullet spawner.
//      */
//     private BulletSpawner bulletSpawner;
//     /**
//      * Whether the player is currently firing bullets.
//      */
//     private boolean isFiringBulletsBullets;
//     // ----- CONSTRUCTORS -----
//     /**
//      * Constructs a new {@link Player}.
//      * <p>
//      * The player remains stationary by default and moves only when directional
//      * inputs are provided.
//      *
//      * @param panel The parent {@link JPanel} the player belongs to.
//      * @param position The initial x and y-coordinates.
//      * @param animationTemplate The {@link AnimationTemplate} for the player's
//      * sprite.
//      * @param maxHitpoints The player's maximum hitpoints.
//      * @param speed The player's movement speed in pixels per frame.
//      */
//     public Player(final JPanel panel, final Point position, final AnimationTemplate animationTemplate, final int maxHitPoints, final int speed) {
//         super(panel, position, animationTemplate, false, true, maxHitPoints, speed);
//         this.movingUp = false;
//         this.movingDown = false;
//         this.movingLeft = false;
//         this.movingRight = false;
//         this.bulletSpawner = new BulletSpawner(
//                 this.panel,
//                 this,
//                 position,
//                 AnimationManager.getInstance().getAnimation("player-bullet-1"),
//                 new Bearing2D(0, 10, 0, 0),
//                 20,
//                 300
//         );
//         this.isFiringBulletsBullets = false;
//     }
//     // ----- GETTERS -----
//     /**
//      * Checks if the player is currently moving in any direction.
//      *
//      * @return {@code true} if moving, otherwise {@code false}.
//      */
//     public boolean isMoving() {
//         return movingUp || movingDown || movingLeft || movingRight;
//     }
//     /**
//      * Checks if the player is moving upward.
//      *
//      * @return {@code true} if moving up, otherwise {@code false}.
//      */
//     public boolean isMovingUp() {
//         return movingUp;
//     }
//     /**
//      * Checks if the player is moving downward.
//      *
//      * @return {@code true} if moving down, otherwise {@code false}.
//      */
//     public boolean isMovingDown() {
//         return movingDown;
//     }
//     /**
//      * Checks if the player is moving left.
//      *
//      * @return {@code true} if moving left, otherwise {@code false}.
//      */
//     public boolean isMovingLeft() {
//         return movingLeft;
//     }
//     /**
//      * Checks if the player is moving right.
//      *
//      * @return {@code true} if moving right, otherwise {@code false}.
//      */
//     public boolean isMovingRight() {
//         return movingRight;
//     }
//     /**
//      * Returns the player's {@link BulletSpawner}.
//      *
//      * @return The player's bullet spawner.
//      */
//     public BulletSpawner getBulletSpawner() {
//         return bulletSpawner;
//     }
//     /**
//      * Checks if the player is firing bullets.
//      *
//      * @return
//      */
//     public boolean isFiringBulletsBullets() {
//         return isFiringBulletsBullets;
//     }
//     // ----- SETTERS -----
//     /**
//      * Sets whether the player is moving up.
//      *
//      * @param value {@code true} to move up, {@code false} to stop.
//      */
//     public void setMovingUp(boolean value) {
//         movingUp = value;
//     }
//     /**
//      * Sets whether the player is moving down.
//      *
//      * @param value {@code true} to move down, {@code false} to stop.
//      */
//     public void setMovingDown(boolean value) {
//         movingDown = value;
//     }
//     /**
//      * Sets whether the player is moving left.
//      *
//      * @param value {@code true} to move left, {@code false} to stop.
//      */
//     public void setMovingLeft(boolean value) {
//         movingLeft = value;
//     }
//     /**
//      * Sets whether the player is moving right.
//      *
//      * @param value {@code true} to move right, {@code false} to stop.
//      */
//     public void setMovingRight(boolean value) {
//         movingRight = value;
//     }
//     /**
//      * Returns the player's {@link BulletSpawner}.
//      *
//      * @return The player's bullet spawner.
//      */
//     public BulletSpawner setBulletSpawner(final BulletSpawner bulletSpawner) {
//         return bulletSpawner;
//     }
//     /**
//      * Sets whether the player is firing {@link Bullet}s.
//      *
//      * @param value {@code true} to fire bullets, {@code false} to stop.
//      */
//     public void setFiringBullets(final boolean value) {
//         this.isFiringBulletsBullets = value;
//     }
//     // ----- BUSINESSS LOGIC METHODS -----
//     /**
//      * Moves the player based on directional inputs while ensuring they remain
//      * within the panel boundaries.
//      */
//     public void move() {
//         int dx = 0;
//         int dy = 0;
//         if (movingUp) {
//             dy -= speed;
//         }
//         if (movingDown) {
//             dy += speed;
//         }
//         if (movingLeft) {
//             dx -= speed;
//         }
//         if (movingRight) {
//             dx += speed;
//         }
//         // Apply movement
//         position.translate(dx, dy);
//         // Ensure the player remains within bounds
//         if (!isFullyWithinPanel()) {
//             correctPosition();
//         }
//     }
//     // ----- OVERRIDDEN METHODS -----
//     /**
//      * Compares this player entity to another object for equality.
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
//         if (!(obj instanceof Player)) {
//             return false;
//         }
//         Player other = (Player) obj;
//         return super.equals(obj)
//                 && speed == other.speed
//                 && movingUp == other.movingUp
//                 && movingDown == other.movingDown
//                 && movingLeft == other.movingLeft
//                 && movingRight == other.movingRight
//                 && Objects.equals(bulletSpawner, other.getBulletSpawner())
//                 && isFiringBulletsBullets == other.isFiringBulletsBullets();
//     }
//     /**
//      * Computes the hash code for this player entity.
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
//                 speed,
//                 movingUp,
//                 movingDown,
//                 movingLeft,
//                 movingRight,
//                 bulletSpawner,
//                 isFiringBulletsBullets
//         );
//     }
//     // ---- HELPER METHODS -----
//     /**
//      * Ensures the player remains within the visible screen boundaries.
//      */
//     private void correctPosition() {
//         // Trying to use Math.clamp gave out of bounds issues or something. This is simpler.
//         position.x = Math.max(0, Math.min(position.x, panel.getWidth() - getSpriteWidth()));
//         position.y = Math.max(0, Math.min(position.y, panel.getHeight() - getSpriteHeight()));
//     }
// }
