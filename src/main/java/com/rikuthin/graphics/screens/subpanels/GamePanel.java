package com.rikuthin.graphics.screens.subpanels;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;

import com.rikuthin.core.App;
import com.rikuthin.core.GameManager;
import com.rikuthin.entities.BulletSpawner;
import com.rikuthin.entities.Player;
import com.rikuthin.graphics.Animation;
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
    public GamePanel(final int width, final int height, final String backgroundImageURL) {
        super(width, height, backgroundImageURL);

        // Background colour used as a backup in case the image deosn't load.
        setBackground(new Color(200, 170, 170));
        initialisePlayer(width, height);

        spawners = new ArrayList<>();

        gameManager = GameManager.getInstance();
        gameManager.init();

        Animation bulletAnimation = new Animation(new Point(100, 100), true);
        bulletAnimation.loadStripFile("/images/animations/bullet-1.png", App.FRAME_RATE_MS, 15, 24);

        for (int i = 0; i < 10; i++) {
            spawners.add(new BulletSpawner(
                    this,
                    new Point(200, 200),
                    null,
                    bulletAnimation,
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
        player = new Player(this, new Point(0, 0), "/images/sprites/white-queen.png", 20, 5);

        int x = Math.divideExact(panelWidth, 2) - Math.divideExact(player.getSpriteWidth(), 2);
        int y = Math.divideExact(panelHeight, 2);
        player.setPosition(x, y);
    }
}
