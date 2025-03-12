package com.rikuthin.graphics.screens;

import java.awt.BorderLayout;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import com.rikuthin.graphics.GameFrame;
import com.rikuthin.graphics.screens.subpanels.GamePanel;
import com.rikuthin.graphics.screens.subpanels.InfoPanel;

public final class GameplayScreen extends Screen {

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

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();

                // Move up if W or up arrow pressed (but not if S or down arrow already pressed)
                if ((keyCode == KeyEvent.VK_W || keyCode == KeyEvent.VK_UP)
                        && !(keyCode == KeyEvent.VK_S || keyCode == KeyEvent.VK_DOWN)) {
                    gamePanel.getPlayer().setMovingUp(true);
                }

                // Move down if S or down arrow pressed (but not if W or up arrow already pressed)
                if ((keyCode == KeyEvent.VK_S || keyCode == KeyEvent.VK_DOWN)
                        && !(keyCode == KeyEvent.VK_W || keyCode == KeyEvent.VK_UP)) {
                    gamePanel.getPlayer().setMovingDown(true);
                }

                // Move left if A or left arrow pressed (but not if D or right arrow already pressed)
                if ((keyCode == KeyEvent.VK_A || keyCode == KeyEvent.VK_LEFT)
                        && !(keyCode == KeyEvent.VK_D || keyCode == KeyEvent.VK_RIGHT)) {
                    gamePanel.getPlayer().setMovingLeft(true);
                }

                // Move left if D or right arrow pressed (but not if A or left arrow already pressed)
                if ((keyCode == KeyEvent.VK_D || keyCode == KeyEvent.VK_RIGHT)
                        && !(keyCode == KeyEvent.VK_A || keyCode == KeyEvent.VK_LEFT)) {
                    gamePanel.getPlayer().setMovingRight(true);
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                int keyCode = e.getKeyCode();
                if (keyCode == KeyEvent.VK_W || keyCode == KeyEvent.VK_UP) {
                    gamePanel.getPlayer().setMovingUp(false);
                }
                if (keyCode == KeyEvent.VK_S || keyCode == KeyEvent.VK_DOWN) {
                    gamePanel.getPlayer().setMovingDown(false);
                }
                if (keyCode == KeyEvent.VK_A || keyCode == KeyEvent.VK_LEFT) {
                    gamePanel.getPlayer().setMovingLeft(false);
                }
                if (keyCode == KeyEvent.VK_D || keyCode == KeyEvent.VK_RIGHT) {
                    gamePanel.getPlayer().setMovingRight(false);
                }
            }
        });
    }

    @Override
    public void update() {
        gamePanel.update();
        infoPanel.update();
    }

    @Override
    public void render(Graphics2D g) {
        gamePanel.safeRender(g);
        infoPanel.safeRender(g);
    }
}
