package com.rikuthin.entities.enemies;

import javax.swing.JPanel;

/**
 * Represents a red mage entity that extends the {@link Enemy} class.
 */
public class RedMage extends Enemy {

    // ----- CONSTRUCTORS -----
    /**
     * Constructor used to create a Enemy instance.
     *
     * @param builder The builder used to construct the enemy.
     */
    public RedMage(EnemyBuilder builder) {
        super(builder);
    }

    // ---- GETTERS -----

    // ---- SETTERS -----
    

    // ----- OVERRIDDEN METHODS -----

    // ----- HELPER METHODS -----
    

    // ----- STATIC BUILDER FOR ENEMY -----
    public static class RedMageBuilder extends EnemyBuilder {

        // ----- CONSTRUCTOR -----
        public RedMageBuilder(JPanel panel) {
            super(panel);
        }

        // ----- BUSINESS LOGIC METHODS -----
        @Override
        public RedMage build() {
            return new RedMage(this);
        }
    }
}
