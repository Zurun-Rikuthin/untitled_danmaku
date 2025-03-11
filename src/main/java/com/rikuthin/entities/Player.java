package com.rikuthin.entities;

import javax.swing.JPanel;

/**
 * This class represents the player-controlled character and defines how it
 * interacts with the game world.
 */
public class Player extends Entity {
    /**
     * Constructor for the player-controlled character.
     *
     * @param panel The parent {@link JPanel} to which the player-character
     * belongs.
     * @param x The player's initial x-coordinate upson spawning.
     * @param y The player's initial y-coordinate upson spawning.
     * @param spriteUrl The initial URL for the player's sprite.
     */
    public Player(final JPanel panel, final int x, final int y, final String spriteUrl) {
        super(panel, x, y, spriteUrl);
    }

    @Override
    public void update() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
