package com.rikuthin.entities;

import java.awt.Graphics2D;

import javax.swing.JPanel;

/**
 * This class represents the player-controlled character and defines how it
 * interacts with the game world.
 */
public class Player extends Entity {

    /**
     * How many pixels to move each frame. Defaults to 0 (not moving).
     */
    private int speed;
    /**
     * Whether there's an input instruction to move up. Defaults to false (not
     * moving up).
     */
    private boolean movingUp;
    /**
     * Whether there's an input instruction to move down. Defaults to false (not
     * moving down).
     */
    private boolean movingDown;
    /**
     * Whether there's an input instruction to move left. Defaults to false (not
     * moving left).
     */
    private boolean movingLeft;
    /**
     * Whether there's an input instruction to move right. Defaults to false
     * (not moving right).
     */
    private boolean movingRight;

    /**
     * Constructor for the player-controlled character.
     *
     * Note that all movement direction flags default to false, meaning the
     * player does not move on its own upon construction. The player object
     * instead waits for the respective keyboard input and moves so long as the
     * input is given.
     *
     * @param panel The parent {@link JPanel} to which the player-character
     * belongs.
     * @param x The player's initial x-coordinate upson spawning.
     * @param y The player's initial y-coordinate upson spawning.
     * @param spriteUrl The initial URL for the player's sprite.
     * @param speed The initial movement speed (pixels per frame);
     */
    public Player(final JPanel panel, final int x, final int y, final String spriteUrl, final int speed) {
        super(panel, x, y, spriteUrl);
        this.speed = speed;
        movingUp = false;
        movingDown = false;
        movingLeft = false;
        movingRight = false;
    }

    @Override
    public void render(Graphics2D g2d) {
        super.render(g2d);
        System.out.println(
                String.format("%s: Rendering at x=%d, y=%d",
                        this.getClass().getName(),
                        x,
                        y
                ));
    }

    public boolean isMovingUp() {
        return movingUp;
    }

    public boolean isMovingDown() {
        return movingDown;
    }

    public boolean isMovingLeft() {
        return movingLeft;
    }

    public boolean isMovingRight() {
        return movingRight;
    }

    public void setMovingUp(boolean value) {
        movingUp = value;
    }

    public void setMovingDown(boolean value) {
        movingDown = value;
    }

    public void setMovingLeft(boolean value) {
        movingLeft = value;
    }

    public void setMovingRight(boolean value) {
        movingRight = value;
    }

    public void move() {
        // Move up
        if (movingUp && !movingDown) {
            y -= speed;
        }

        // Move down
        if (movingDown && !movingUp) {
            y += speed;
        }

        // Move left
        if (movingLeft && !movingRight) {
            x -= speed;
        }

        // Move right
        if (movingRight && !movingLeft) {
            x += speed;
        }

        correctPosition();
    }

    private void correctPosition() {
        int maxX = panel.getWidth() - spriteWidth;
        int maxY = panel.getHeight() - spriteHeight;

        // Stay within screen's upper boundary
        y = y < 0 ? 0 : y;

        // Stay within screen's lower boundary
        y = y > maxY ? maxY : y;

        // Stay within screen's left boundary
        x = x < 0 ? 0 : x;

        // Stay within screen's right boundary
        x = x > maxX ? maxX : x;
    }
}
