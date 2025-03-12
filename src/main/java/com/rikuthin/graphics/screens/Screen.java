package com.rikuthin.graphics.screens;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import com.rikuthin.graphics.GameFrame;
import com.rikuthin.interfaces.Renderable;

/**
 * Abstract base class for all game screens (e.g., main menu, gameplay).
 * Provides a structure for updating and rendering screens.
 */
public abstract class Screen extends JPanel implements Renderable {

    /**
     * The parent {@link GameFrame} to which this screen belongs
     */
    protected final GameFrame gameFrame;

    /**
     * Constructs a screen panel and sets its dimensions to match the game
     * window.
     *
     * @param gameFrame The parent {@link GameFrame} to which this screen
     * belongs.
     */
    protected Screen(GameFrame gameFrame) {
        this.gameFrame = gameFrame;

        // Set the panel size to match the game window dimensions.
        Dimension panelSize = new Dimension(GameFrame.FRAME_WIDTH, GameFrame.FRAME_HEIGHT);
        setPreferredSize(panelSize);
        setMinimumSize(panelSize);
        setMaximumSize(panelSize);
    }

    /**
     * Override the paintComponent method to render the game on the screen. This
     * is where custom rendering will occur.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (g instanceof Graphics2D g2d) {
            safeRender(g2d);
        }
    }

    /**
     * Cleans up resources and UI elements when switching screens.
     */
    public void cleanup() {
        removeAll();
        revalidate();
    }

    /**
     * Updates the screen logic every frame.
     */
    public abstract void update();

    /**
     * Renders the screen's graphical components.
     */
    public abstract void render(Graphics2D g);
}
