package com.rikuthin.graphics.screens.subpanels;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;

import com.rikuthin.core.GameManager;
import com.rikuthin.entities.BulletSpawner;
import com.rikuthin.entities.Player;
import com.rikuthin.graphics.animations.AnimationManager;
import com.rikuthin.graphics.animations.AnimationTemplate;
import com.rikuthin.utility.Bearing2D;

/**
 * A component that displays all the game entities
 */
public class GamePanel extends Subpanel {

    // ----- INSTANCE VARIABLES -----
    private GameManager gameManager;
    private Player player;
    private ArrayList<BulletSpawner> spawners;

    // ----- CONSTRUCTORS -----
    public GamePanel(final int width, final int height, final String backgroundImageFilepath) {
        super(width, height, backgroundImageFilepath);

        // Background colour used as a backup in case the image deosn't load.
        setBackground(new Color(200, 170, 170));
        initialisePlayer(width, height);

        spawners = new ArrayList<>();

        gameManager = GameManager.getInstance();
        gameManager.init();

        AnimationTemplate bulletAnimationTemplate = AnimationManager.getInstance().getAnimation("bullet-1");

        if (bulletAnimationTemplate == null) {
            throw new RuntimeException("getAnimation(\"bullet-1\") returned null, even though key exists.");
        } else {
            System.out.println("Retrieved 'bullet-1' animation template: " + bulletAnimationTemplate);
        }

        for (int i = 0; i < 10; i++) {
            spawners.add(new BulletSpawner(
                    this,
                    new Point(200, 200),
                    bulletAnimationTemplate,
                    new Bearing2D(0, 0, 20 + (i * 10), -20),
                    10,
                    250
            ));
            spawners.get(i).start();
        }
    }

    // ----- OVERRIDDEN METHODS -----
    /**
     * Updates the screen logic every frame.
     */
    @Override
    public void update() {
        if (player != null) {
            player.move();
        }

        for (BulletSpawner spawner : spawners) {
            if (spawner != null) {
                spawner.update();
            }
        }

        gameManager.update();
    }

    public Player getPlayer() {
        return player;
    }

    /**
     * Renders the screen's graphical components.
     */
    @Override
    public void render(Graphics2D g2d) {
        super.render(g2d);

        if (player != null) {
            player.safeRender(g2d);
        }

        for (BulletSpawner spawner : spawners) {
            if (spawner != null) {
                spawner.safeRender(g2d);
            }
        }

        gameManager.render(g2d);
    }

    // ----- HELPER METHODS -----
    private void initialisePlayer(final int panelWidth, final int panelHeight) {
        player = new Player(
                this,
                new Point(0, 0),
                AnimationManager.getInstance().getAnimation("player"),
                20,
                5
        );

        int x = Math.divideExact(panelWidth, 2) - Math.divideExact(player.getSpriteWidth(), 2);
        int y = Math.divideExact(panelHeight, 2);
        player.setPosition(x, y);
    }
}
