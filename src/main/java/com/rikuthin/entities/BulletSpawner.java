package com.rikuthin.entities;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.Objects;

import javax.swing.JPanel;

import com.rikuthin.core.GameManager;
import com.rikuthin.utility.Bearing2D;

/**
 * Represents an invisible object used by other entites to shoot bullets.
 *
 * Bullets start at the spawner's own position and move at a specified speed in
 * a specified direction, "dying" once fully off-screen.
 *
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
     * The diameter (in pixels) of the bubbles shot from the blaster.
     */
    private int shotSize;

    /**
     * The speed (in pixels per tick) at which bubbles are shot from the
     * blaster.
     */
    private double shotSpeed;

    // ----- CONSTRUCTORS -----
    /**
     * Constructs a new Blaster.
     *
     * @param x the x-coordinate of the blaster
     * @param y the y-coordinate of the blaster
     * @param shotSize the diameter (in pixels) of the blaster (and the bubbles)
     * @param shotSpeed the speed (in pixels/tick) at which bubbles are shot
     * @param colour the colour of the blaster
     */
    public BulletSpawner(final JPanel panel, final int x, final int y, final int shotSize, final double shotSpeed, final Color colour) {
        super(panel, x, y, true, null);
        this.bearing = new Bearing2D(0);
        this.shotSize = shotSize;
        this.shotSpeed = shotSpeed;
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
     * Returns the size (in pixels) of the bubbles that will be shot from the
     * blaster.
     *
     * @return the diameter of the bubbles
     */
    public int getShotSize() {
        return shotSize;
    }

    /**
     * Returns the speed (in pixels/tick) at which the bubbles will move.
     *
     * @return the speed of the bubbles
     */
    public double getShotSpeed() {
        return shotSpeed;
    }

    // ----- SETTERS -----
    /**
     * Sets the colour of the blaster.
     *
     * @param colour the new {@link Color} for the blaster
     */
    public void setColour(Color colour) {
        this.colour = colour;
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
     * Sets the size (in pixels) of the bubbles that will be shot from the
     * blaster.
     *
     * @param shotSize the new bubble diameter
     */
    public void setShotSize(final int shotSize) {
        this.shotSize = shotSize;
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

    // ----- FIRING METHODS -----
    /**
     * Shoots a new Bubble instance towards the given target location.
     * <p>
     * The bubble is created at the centre of the blaster and its movement
     * direction is determined by calculating the bearing from the blaster's
     * centre to the target.
     * </p>
     *
     * @param target the mouse position where the bubble should travel
     * @param bubbleColour the colour of the bubble
     * @return the newly created {@link Bullet} instance
     * @throws IllegalArgumentException if the target is null
     */
    public Bullet shootBubble(final Point target, final Color bubbleColour) {
        if (target == null) {
            throw new IllegalArgumentException("Target position cannot be null");
        }

        // Account for the blaster panel being below the bubble panel
        final int bubblePanelHeight = gameManager.getBubblePanel().getHeight();

        final int startX = (int) Math.floor(getCenterX());
        final int startY = (int) Math.floor(getCenterY()) + bubblePanelHeight;

        Bullet bubble = new Bullet(startX, startY, bubbleColour);
        bubble.setBearing(new Bearing2D(startX, startY, target.x, target.y));
        bubble.setSpeed(shotSpeed);
        bubble.setIsMoving(true);

        new Thread(bubble).start();

        return bubble;
    }

    /**
     * Draws the blaster onto the provided {@link Graphics2D} context.
     *
     * @param g2 the {@link Graphics2D} object used for rendering
     */
    public void draw(Graphics2D g2) {
        g2.setColor(colour);
        g2.fill(this);
        g2.setColor(Color.BLACK);
        g2.draw(this);
    }

    // ----- OVERRIDDEN METHODS -----
    // /**
    //  * Runs the blaster's movement logic in a loop.
    //  * <p>
    //  * Note: Rotation logic is currently disabled. Future implementations may
    //  * enable dynamic movement or rotation.
    //  * </p>
    //  */
    // @Override
    // public void run() {
    //     while (true) {
    //         // Future implementation: rotate();
    //         try {
    //             Thread.sleep(20); // Controls the update interval (speed)
    //         } catch (InterruptedException e) {
    //             Thread.currentThread().interrupt(); // Restore interrupted state
    //             break;
    //         }
    //     }
    // }

    /**
     * Compares this blaster with another object for equality. Two blasters are
     * considered equal if they have the same position, size, colour, movement
     * status, bearing, and shot properties.
     *
     * @param obj the object to compare with
     * @return {@code true} if the blasters are equal; {@code false} otherwise
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
        return java.lang.Double.compare(x, other.x) == 0
                && java.lang.Double.compare(y, other.y) == 0
                && java.lang.Double.compare(width, other.width) == 0
                && java.lang.Double.compare(height, other.height) == 0
                && colour.equals(other.colour)
                && bearing.equals(other.bearing)
                && shotSize == other.shotSize
                && java.lang.Double.compare(shotSpeed, other.shotSpeed) == 0;
    }

    /**
     * Computes the hash code for this blaster based on its position, size,
     * colour, bearing, and shot properties.
     *
     * @return the hash code of this blaster
     */
    @Override
    public int hashCode() {
        return Objects.hash(x, y, width, height, colour, bearing, shotSize, shotSpeed);
    }

    @Override
    public void update() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
