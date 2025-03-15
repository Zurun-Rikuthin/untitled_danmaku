package com.rikuthin.graphics.screens.subpanels;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import com.rikuthin.graphics.ImageManager;
import com.rikuthin.interfaces.Renderable;
import com.rikuthin.interfaces.Updateable;

/**
 * Abstract base class for all game screens (e.g., main menu, gameplay).
 * Provides a structure for updating and rendering screens.
 */
public abstract class Subpanel extends JPanel implements Updateable, Renderable {

    // ----- INSTANCE VARIABLES -----
    protected String backgroundImageUrl;
    protected BufferedImage backgroundImage;

    // ----- CONSTRUCTORS -----
    protected Subpanel(final int width, final int height, final String backgroundImageUrl) {
        Dimension panelSize = new Dimension(width, height);
        setPreferredSize(panelSize);
        setMinimumSize(panelSize);
        setMaximumSize(panelSize);
        setBackgroundImage(backgroundImageUrl);
    }

    // ----- SETTERS -----
    /**
     * Sets the background image (and its URL) for this subpanel.
     *
     * @param backgroundImageUrl The image URL.
     */
    public final void setBackgroundImage(final String backgroundImageUrl) {
        this.backgroundImageUrl = backgroundImageUrl;
        backgroundImage = ImageManager.loadBufferedImage(backgroundImageUrl);

        if (backgroundImage == null) {
            System.err.println(String.format(
                    "%s: Could not load background image <'%s'>.",
                    this.getClass().getName(),
                    backgroundImageUrl
            ));
        }
    }

    // ----- OVERRIDDEN METHODS -----
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
     * Renders the screen's graphical components.
     *
     * By default, only renders the background image (if one is set).
     */
    @Override
    public void render(Graphics2D g2d) {
        if (backgroundImage == null) {
            System.err.println(String.format(
                    "%s: Could not load background image <'%s'>.",
                    this.getClass().getName(),
                    backgroundImageUrl
            ));
            return;
        }
        g2d.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
    }
}
