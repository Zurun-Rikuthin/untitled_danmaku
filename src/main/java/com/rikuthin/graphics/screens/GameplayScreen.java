package com.rikuthin.graphics.screens;

import java.awt.BorderLayout;
import java.awt.Graphics2D;

import com.rikuthin.graphics.GameFrame;
import com.rikuthin.graphics.screens.subpanels.GamePanel;
import com.rikuthin.graphics.screens.subpanels.InfoPanel;

public final class GameplayScreen extends Screen {
    private final GamePanel gamePanel;
    private final InfoPanel infoPanel;

    public GameplayScreen(GameFrame gameFrame) {
        super(gameFrame);

        setLayout(new BorderLayout());
        gamePanel = new GamePanel();
        infoPanel = new InfoPanel();

        add(gamePanel, BorderLayout.CENTER);
        add(infoPanel, BorderLayout.LINE_END);
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
