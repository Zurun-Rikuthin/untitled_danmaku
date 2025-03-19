package com.rikuthin.entities.enemies;

import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.swing.JPanel;

import com.rikuthin.entities.bullets.BulletSpawner;

/**
 * Represents a Blue Mage enemy in the game.
 * <p>
 * This class extends {@link Enemy} and initializes attributes specific to the
 * Blue Mage.
 */
public class BlueMage extends Enemy {

    // ----- CONSTRUCTORS -----
    /**
     * Constructor used to create a BlueMage instance.
     *
     * @param builder The {@link EnemyBuilder} used to construct the BlueMage.
     */
    public BlueMage(EnemyBuilder builder) {
        super(builder);

        setUpBlueMageAnimations();
        setUpBlueMageBulletSpawner();
    }

    // ----- HELPER METHODS -----
    /**
     * Set up the animations for the Blue Mage, including animation keys and
     * initial animation.
     */
    private void setUpBlueMageAnimations() {
        HashSet<String> blueMageAnimationKeys = Stream.of(
                "mage-guardian-magenta"
        ).collect(Collectors.toCollection(HashSet::new));

        setAnimationKeys(blueMageAnimationKeys);
        setAnimation("mage-guardian-magenta");
    }

    /**
     * Set up the animations for the Blue Mage, including animation keys and
     * initial animation.
     */
    private void setUpBlueMageBulletSpawner() {
        HashSet<String> blueMageBulletAnimationKeys = Stream.of(
            "enemy-bullet"
            ).collect(Collectors.toCollection(HashSet::new));

        BulletSpawner spawner = new BulletSpawner.BulletSpawnerBuilder(panel, this)
                .bulletDamage(1)
                .bulletVelocityY(-20)
                .bulletAnimationKeys(blueMageBulletAnimationKeys)
                .currentBulletAnimationKey("enemy-bullet")
                .build();

        setBulletSpawner(spawner);
    }


    // ----- STATIC BUILDER FOR ENEMY -----
    public static class BlueMageBuilder extends EnemyBuilder {

        // ----- CONSTRUCTOR -----
        public BlueMageBuilder(JPanel panel) {
            super(panel);
        }

        // ----- OVERRIDDEN METHODS -----
        @Override
        public BlueMage build() {
            return new BlueMage(this);
        }
    }
}
