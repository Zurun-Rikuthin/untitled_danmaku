package com.rikuthin.entities.enemies;

import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.swing.JPanel;

import com.rikuthin.entities.bullets.BulletSpawner;

/**
 * Represents a Red Mage enemy in the game.
 * <p>
 * This class extends {@link Enemy} and initializes attributes specific to the
 * Red Mage.
 */
public class RedMage extends Enemy {

    // ----- CONSTRUCTORS -----
    /**
     * Constructor used to create a RedMage instance.
     *
     * @param builder The {@link EnemyBuilder} used to construct the RedMage.
     */
    public RedMage(EnemyBuilder builder) {
        super(builder);

        setUpRedMageAnimations();
        setUpRedMageBulletSpawner();
    }

    // ----- HELPER METHODS -----
    /**
     * Set up the animations for the Red Mage, including animation keys and
     * initial animation.
     */
    private void setUpRedMageAnimations() {
        HashSet<String> redMageAnimationKeys = Stream.of(
                "mage-guardian-magenta"
        ).collect(Collectors.toCollection(HashSet::new));

        setAnimationKeys(redMageAnimationKeys);
        setAnimation("mage-guardian-magenta");
    }

    /**
     * Set up the animations for the Red Mage, including animation keys and
     * initial animation.
     */
    private void setUpRedMageBulletSpawner() {
        HashSet<String> redMageBulletAnimationKeys = Stream.of(
            "enemy-bullet"
            ).collect(Collectors.toCollection(HashSet::new));

        BulletSpawner spawner = new BulletSpawner.BulletSpawnerBuilder(panel, this)
                .bulletDamage(1)
                .bulletVelocityY(-20)
                .bulletAnimationKeys(redMageBulletAnimationKeys)
                .currentBulletAnimationKey("enemy-bullet")
                .build();

        setBulletSpawner(spawner);
    }


    // ----- STATIC BUILDER FOR ENEMY -----
    public static class RedMageBuilder extends EnemyBuilder {

        // ----- CONSTRUCTOR -----
        public RedMageBuilder(JPanel panel) {
            super(panel);
        }

        // ----- OVERRIDDEN METHODS -----
        @Override
        public RedMage build() {
            return new RedMage(this);
        }
    }
}
