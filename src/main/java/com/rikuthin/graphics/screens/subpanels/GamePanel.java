package com.rikuthin.graphics.screens.subpanels;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Set;

import com.rikuthin.core.GameManager;
import com.rikuthin.entities.Bullet;
import com.rikuthin.entities.Enemy;
import com.rikuthin.entities.Player;

/**
 * A component that displays all the game entities
 */
public class GamePanel extends Subpanel {

    private final transient GameManager gameManager;

    // ----- CONSTRUCTORS -----
    public GamePanel(final int width, final int height, final String backgroundImageFilepath) {
        super(width, height, backgroundImageFilepath);

        // Background colour used as a backup in case the image deosn't load.
        setBackground(new Color(200, 170, 170));
        gameManager = GameManager.getInstance();
    }

    // ----- OVERRIDDEN METHODS -----
    /**
     * Renders the screen's graphical components.
     */
    @Override
    public void render(Graphics2D g2d) {
        super.render(g2d);

        if (!gameManager.isRunning()) {
            return;
        }

        Player player = gameManager.getPlayer();
        if (player != null) {
            player.safeRender(g2d);
        }

        Set<Enemy> enemies = gameManager.getEnemies();
        if (enemies != null) {
            for (Enemy e : enemies) {
                e.safeRender(g2d);
            }
        }

        Set<Bullet> bullets = gameManager.getBullets();
        if (bullets != null) {
            for (Bullet b : bullets) {
                b.safeRender(g2d);
            }
        }
    }
}
