package com.rikuthin.entities;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.util.List;

import com.rikuthin.core.App;
import com.rikuthin.core.GameManager;
import com.rikuthin.utility.Bearing2D;

/**
 * Represents a bubble that moves within a JPanel. The bubble moves along a
 * specified bearing (angle in degrees) at a defined speed (pixels per tick) and
 * bounces off the edges and walls, stopping at the roof.
 */
public class Bubble extends Ellipse2D.Double implements Runnable {

    public static final double SIZE = 30; // Size of the bubble in pixels

    private final Color colour;  // Colour of the bubble
    private boolean isMoving;    // Whether the bubble should move
    private Bearing2D bearing;   // Direction of movement (bearing)
    private double speed;        // Movement speed (in pixels per tick)

    /**
     * Constructs a new Bubble.
     *
     * @param initialX The initial x-coordinate.
     * @param initialY The initial y-coordinate.
     * @param colour The color.
     */
    public Bubble(final int initialX, final int initialY, final Color colour) {
        super(initialX, initialY, SIZE, SIZE);

        if (GameManager.getInstance().getBubblePanel() == null) {
            throw new NullPointerException("BubblePanel must be instantiated before creating bubbles.");
        }

        this.colour = colour;
        this.isMoving = false;
        this.bearing = new Bearing2D(0);
        this.speed = 0;
    }

    public boolean isMoving() {
        return isMoving;
    }

    public Bearing2D getBearing() {
        return bearing;
    }

    public double getSpeed() {
        return speed;
    }

    public void setIsMoving(final boolean isMoving) {
        this.isMoving = isMoving;
    }

    public void setBearing(final Bearing2D bearing) {
        this.bearing = bearing;
    }

    public void setSpeed(final double speed) {
        this.speed = Math.abs(speed);
    }

    /**
     * Moves the bubble based on its bearing and speed.
     */
    public void move() {
        GameManager gameManager = GameManager.getInstance();
        BubblePanel bubblePanel = gameManager.getBubblePanel();
        if (!bubblePanel.isVisible()) {
            return;
        }

        final Dimension panelSize = bubblePanel.getSize();
        final double radians = Math.toRadians(bearing.getDegrees());

        double nextX = x + speed * Math.cos(radians);
        double nextY = y - speed * Math.sin(radians); // Inverted for screen coordinates

        // Break if colliding with a wall
        if (checkWallCollision()) {
            bubblePanel.getBubbles().remove(this);
            bubblePanel.repaint();
            isMoving = false;
            return;
        }

        // Handle Y-axis bouncing or stopping at the top
        if (nextY < 0) {
            y = 0;
            isMoving = false;

            // The line `final int currentScore = gameManager` seems to be incomplete in the provided
            // code snippet. It looks like there might be a typo or an incomplete statement. It seems
            // like there is a missing method call or assignment after `gameManager`.
            final int currentScore = gameManager.getScore();
            gameManager.setScore(currentScore + 100);
            return;
        } else {
            if (nextY - height > panelSize.height) {
                bearing.setDegrees(360 - bearing.getDegrees());  // Reverse Y direction
                nextY = panelSize.height - height;
            }
            y = nextY;
        }

        // Handle X-axis bouncing
        if (nextX < 0 || nextX + width > panelSize.width) {
            bearing.setDegrees(180 - bearing.getDegrees());  // Reverse X direction
            nextX = Math.clamp(nextX, 0, panelSize.width - width);
        }
        x = nextX;

        javax.swing.SwingUtilities.invokeLater(bubblePanel::repaint);  // Thread-safe repaint
    }

    /**
     * Draws the bubble.
     */
    public void draw(final Graphics2D g2) {
        g2.setColor(colour);
        g2.fill(this);
        g2.setColor(Color.BLACK);
        g2.draw(this);
    }

    /**
     * Runs the bubble's movement logic.
     * 
     * Bubbles can freeze if the game is paused while they're in motion.
     * No time to fix this bug now
     */
    @Override
    public void run() {
        
        while (isMoving && !GameManager.getInstance().isPaused()) {
            move();
            try {
                Thread.sleep(App.TICK_SPEED_MS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        GameManager.getInstance().onBubbleMovementComplete();
    }

    /**
     * Checks for collision with walls.
     */
    private boolean checkWallCollision() {
        List<Wall> walls = GameManager.getInstance().getBubblePanel().getWalls();

        for (Wall wall : walls) {
            if (intersects(wall)) {
                return true;
            }
        }
        return false;
    }
}
