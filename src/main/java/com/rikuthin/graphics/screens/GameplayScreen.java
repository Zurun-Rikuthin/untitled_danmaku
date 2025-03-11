package com.rikuthin.graphics.screens;

import java.awt.BorderLayout;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import com.rikuthin.graphics.GameFrame;
import com.rikuthin.graphics.screens.subpanels.GamePanel;
import com.rikuthin.graphics.screens.subpanels.InfoPanel;

public final class GameplayScreen extends Screen implements KeyListener {

    private final GamePanel gamePanel;
    private final InfoPanel infoPanel;

    public GameplayScreen(GameFrame gameFrame) {
        super(gameFrame);
        setLayout(new BorderLayout());

        int frameWidth = gameFrame.getWidth();
        int frameHeight = gameFrame.getHeight();
        gamePanel = new GamePanel(frameWidth, frameHeight, "/images/backgrounds/game-panel.png");
        infoPanel = new InfoPanel(frameWidth - frameHeight, frameHeight, "/images/backgrounds/info-panel.png");

        add(gamePanel, BorderLayout.CENTER);
        add(infoPanel, BorderLayout.LINE_END);
    }



    @Override
    public void keyPressed(KeyEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void keyReleased(KeyEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void keyTyped(KeyEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void update() {
        // Does nothing atm
    }

    @Override
    public void render(Graphics2D g) {
        gamePanel.render(g);
        infoPanel.render(g);
    }
}
