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
    private boolean upPressed;
    private boolean downPressed;
    private boolean leftPressed;
    private boolean rightPressed;

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

        upPressed = false;
        downPressed = false;
        leftPressed = false;
        rightPressed = false;

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();
                int playerSpeed = 5;

                // Halve the speed if Shift is held down
                if (e.isShiftDown()) {
                    playerSpeed /= 2;
                }

                // Handle movement keys
                switch (keyCode) {
                    case KeyEvent.VK_W, KeyEvent.VK_UP -> {
                        upPressed = true;
                        player.setVelocityY(playerSpeed);
                        player.setAnimation("player-walk-up");
                    }
                    case KeyEvent.VK_S, KeyEvent.VK_DOWN -> {
                        downPressed = true;
                        player.setVelocityY(-playerSpeed);
                        player.setAnimation("player-walk-up");
                    }
                    case KeyEvent.VK_A, KeyEvent.VK_LEFT -> {
                        leftPressed = true;
                        player.setVelocityX(-playerSpeed);
                        player.setAnimation("player-walk-up-left");
                    }
                    case KeyEvent.VK_D, KeyEvent.VK_RIGHT -> {
                        rightPressed = true;
                        player.setVelocityX(playerSpeed);
                        player.setAnimation("player-walk-up-right");
                    }
                    case KeyEvent.VK_SPACE -> {
                        player.getBulletSpawner().setIsSpawning(true);
                    }
                    default -> {
                        // Doesn't do anything/ignore all other keys
                        break;
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                int keyCode = e.getKeyCode();

                // Handle key release
                switch (keyCode) {
                    case KeyEvent.VK_W, KeyEvent.VK_UP -> {
                        upPressed = false;
                        if (!downPressed) {
                            player.setVelocityY(0);  // Stop Y velocity if no other vertical key is pressed
                        }
                        if (!leftPressed && !rightPressed) {
                            player.setAnimation("player-idle");  // Only reset animation if no direction is pressed
                        }
                    }
                    case KeyEvent.VK_S, KeyEvent.VK_DOWN -> {
                        downPressed = false;
                        if (!upPressed) {
                            player.setVelocityY(0);  // Stop Y velocity if no other vertical key is pressed
                        }
                        if (!leftPressed && !rightPressed) {
                            player.setAnimation("player-idle");  // Only reset animation if no direction is pressed
                        }
                    }
                    case KeyEvent.VK_A, KeyEvent.VK_LEFT -> {
                        leftPressed = false;
                        if (!rightPressed) {
                            player.setVelocityX(0);  // Stop X velocity if no other horizontal key is pressed
                        }
                        if (!upPressed && !downPressed) {
                            player.setAnimation("player-idle");  // Only reset animation if no direction is pressed
                        }
                    }
                    case KeyEvent.VK_D, KeyEvent.VK_RIGHT -> {
                        rightPressed = false;
                        if (!leftPressed) {
                            player.setVelocityX(0);  // Stop X velocity if no other horizontal key is pressed
                        }
                        if (!upPressed && !downPressed) {
                            player.setAnimation("player-idle");  // Only reset animation if no direction is pressed
                        }
                    }
                    case KeyEvent.VK_SPACE -> {
                        player.getBulletSpawner().setIsSpawning(false);
                    }
                    default -> {
                        // Doesn't do anything/ignore all other keys
                        break;
                    }
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
