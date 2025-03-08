package com.rikuthin.graphics.screens;

import java.awt.Dimension;

import javax.swing.JPanel;

import com.rikuthin.graphics.GameFrame;

/**
 * Abstract base class for all screen panels in the game. This provides common
 * functionality, such as setting the panel size, and storing a reference to the
 * GameFrame.
 */
public abstract class Screen extends JPanel {

    protected final GameFrame gameFrame;

    /**
     * Constructs a ScreenPanel with the specified GameFrame reference. Sets the
     * preferred, minimum, and maximum sizes of the panel to match the GameFrame
     * dimensions.
     *
     * @param gameFrame The GameFrame to which this screen panel belongs.
     */
    protected Screen(GameFrame gameFrame) {
        this.gameFrame = gameFrame;

        // Setting panel size to match the frame's size.
        Dimension panelSize = new Dimension(GameFrame.FRAME_WIDTH, GameFrame.FRAME_HEIGHT);
        setPreferredSize(panelSize);
        setMinimumSize(panelSize);
        setMaximumSize(panelSize);
    }
}
