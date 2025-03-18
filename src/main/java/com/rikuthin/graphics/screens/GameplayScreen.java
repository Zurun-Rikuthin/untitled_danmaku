package com.rikuthin.graphics.screens;

import java.awt.BorderLayout;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Map;

import com.rikuthin.core.GameManager;
import com.rikuthin.entities.Player;
import com.rikuthin.graphics.GameFrame;
import com.rikuthin.graphics.screens.subpanels.GamePanel;
import com.rikuthin.graphics.screens.subpanels.InfoPanel;

public final class GameplayScreen extends Screen {

    private final transient GameManager gameManager;
    private final GamePanel gamePanel;
    private final InfoPanel infoPanel;

    public GameplayScreen(GameFrame gameFrame) {
        super(gameFrame);
        setLayout(new BorderLayout());

        int frameWidth = gameFrame.getWidth();
        int frameHeight = gameFrame.getHeight();
        gamePanel = new GamePanel(frameHeight, frameHeight, "/images/backgrounds/game-panel.png");
        infoPanel = new InfoPanel(frameWidth - frameHeight, frameHeight, "/images/backgrounds/info-panel.png");

        add(gamePanel, BorderLayout.CENTER);
        add(infoPanel, BorderLayout.LINE_END);

        gameManager = GameManager.getInstance();
        gameManager.init(gamePanel, infoPanel);
        Player player = gameManager.getPlayer();

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();
                int playerSpeed = 5;

                // Move up if W or up arrow pressed (but not if S or down arrow already pressed)
                if ((keyCode == KeyEvent.VK_W || keyCode == KeyEvent.VK_UP)
                        && !(keyCode == KeyEvent.VK_S || keyCode == KeyEvent.VK_DOWN)) {
                    player.setVelocityY(playerSpeed);
                }

                // Move down if S or down arrow pressed (but not if W or up arrow already pressed)
                if ((keyCode == KeyEvent.VK_S || keyCode == KeyEvent.VK_DOWN)
                        && !(keyCode == KeyEvent.VK_W || keyCode == KeyEvent.VK_UP)) {
                    player.setVelocityY(-playerSpeed);
                }

                // Move left if A or left arrow pressed (but not if D or right arrow already pressed)
                if ((keyCode == KeyEvent.VK_A || keyCode == KeyEvent.VK_LEFT)
                        && !(keyCode == KeyEvent.VK_D || keyCode == KeyEvent.VK_RIGHT)) {
                    player.setVelocityX(-playerSpeed);
                }

                // Move left if D or right arrow pressed (but not if A or left arrow already pressed)
                if ((keyCode == KeyEvent.VK_D || keyCode == KeyEvent.VK_RIGHT)
                        && !(keyCode == KeyEvent.VK_A || keyCode == KeyEvent.VK_LEFT)) {
                    player.setVelocityX(playerSpeed);
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                int keyCode = e.getKeyCode();
                if (keyCode == KeyEvent.VK_W || keyCode == KeyEvent.VK_UP) {
                    player.setVelocityY(0);
                }
                if (keyCode == KeyEvent.VK_S || keyCode == KeyEvent.VK_DOWN) {
                    player.setVelocityY(0);
                }
                if (keyCode == KeyEvent.VK_A || keyCode == KeyEvent.VK_LEFT) {
                    player.setVelocityX(0);
                }
                if (keyCode == KeyEvent.VK_D || keyCode == KeyEvent.VK_RIGHT) {
                    player.setVelocityX(0);
                }
            }
        });
    }

    // ----- GETTERS -----
    public GamePanel getGamePanel() {
        return gamePanel;
    }

    public InfoPanel getInfoPanel() {
        return infoPanel;
    }

    // ----- OVERRIDDEN METHODS -----
    @Override
    public void update() {
        if (gameManager.isRunning()) {
            gameManager.update();
        }
    }

    @Override
    public void render(Graphics2D g2d) {
        if (gameManager.isRunning()) {
            gamePanel.safeRender(g2d);
            infoPanel.safeRender(g2d);
        }
    }

    // ----- PRIVATE CLASSES -----
    /**
     * Handles key events for player movement and actions.
     */
    private class KeyHandler extends KeyAdapter {

        // ----- INSTANCE VARIABLES -----
        private final Map<Integer, Runnable> keyPressActions = Map.of(
                KeyEvent.VK_W, () -> gameManager.getPlayer().setVelocityY(-20),
                KeyEvent.VK_UP, () -> gameManager.getPlayer().setVelocityY(-20),
                KeyEvent.VK_S, () -> gameManager.getPlayer().setVelocityY(20),
                KeyEvent.VK_DOWN, () -> gameManager.getPlayer().setVelocityY(20),
                KeyEvent.VK_A, () -> gameManager.getPlayer().setVelocityX(-20),
                KeyEvent.VK_LEFT, () -> gameManager.getPlayer().setVelocityY(-20),
                KeyEvent.VK_D, () -> gameManager.getPlayer().setVelocityX(20),
                KeyEvent.VK_RIGHT, () -> gameManager.getPlayer().setVelocityX(20),
                KeyEvent.VK_SPACE, () -> gameManager.getPlayer().setIsFiringBullets(true)
        );

        private final Map<Integer, Runnable> keyReleaseActions = Map.of(
                KeyEvent.VK_W, () -> gameManager.getPlayer().setVelocityY(0),
                KeyEvent.VK_UP, () -> gameManager.getPlayer().setVelocityY(0),
                KeyEvent.VK_S, () -> gameManager.getPlayer().setVelocityY(0),
                KeyEvent.VK_DOWN, () -> gameManager.getPlayer().setVelocityY(0),
                KeyEvent.VK_A, () -> gameManager.getPlayer().setVelocityX(0),
                KeyEvent.VK_LEFT, () -> gameManager.getPlayer().setVelocityY(0),
                KeyEvent.VK_D, () -> gameManager.getPlayer().setVelocityX(0),
                KeyEvent.VK_RIGHT, () -> gameManager.getPlayer().setVelocityX(0),
                KeyEvent.VK_SPACE, () -> gameManager.getPlayer().setIsFiringBullets(false)
        );

        // ----- OVERRIDDEN METHODS -----
        @Override
        public void keyPressed(KeyEvent e) {
            keyPressActions.getOrDefault(e.getKeyCode(), () -> {
            }).run();
        }

        @Override
        public void keyReleased(KeyEvent e) {
            keyReleaseActions.getOrDefault(e.getKeyCode(), () -> {
            }).run();
        }
    }
}
