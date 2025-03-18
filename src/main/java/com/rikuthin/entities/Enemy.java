package com.rikuthin.entities;

import javax.swing.JPanel;

/**
 * Represents a enemy in the game that is mobile and can move around the game
 * world.
 */
public class Enemy extends MobileEntity {

    // ----- CONSTRUCTORS -----
    /**
     * Constructor used to create a Enemy instance.
     *
     * @param builder The builder used to construct the enemy.
     */
    public Enemy(EnemyBuilder builder) {
        super(builder);
    }

    // ----- STATIC BUILDER FOR PLAYER -----
    public static class EnemyBuilder extends MobileEntityBuilder<EnemyBuilder> {

        public EnemyBuilder(JPanel panel) {
            super(panel);
        }

        public Enemy build() {
            return new Enemy(this);
        }
    }
}
