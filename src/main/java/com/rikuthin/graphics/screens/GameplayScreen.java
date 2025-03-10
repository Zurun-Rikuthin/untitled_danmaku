package com.rikuthin.graphics.screens;

import java.awt.Graphics2D;

import javax.swing.BoxLayout;

import com.rikuthin.graphics.GameFrame;
// import com.rikuthin.screen_panels.gameplay_subpanels.BlasterPanel;
// import com.rikuthin.screen_panels.gameplay_subpanels.BubblePanel;
// import com.rikuthin.screen_panels.gameplay_subpanels.StatusPanel;

public final class GameplayScreen extends Screen {

    // private final StatusPanel statusPanel;
    // private final BubblePanel bubblePanel;
    // private final BlasterPanel blasterPanel;

    public GameplayScreen(GameFrame gameFrame) {
        super(gameFrame);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // statusPanel = new StatusPanel();
        // bubblePanel = new BubblePanel();
        // blasterPanel = new BlasterPanel();

        // add(statusPanel);
        // add(bubblePanel);
        // add(blasterPanel);
    }

    @Override
    public void update() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public void render(Graphics2D g) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'render'");
    }

    // public StatusPanel getStatusPanel() {
    //     return statusPanel;
    // }

    // public BubblePanel getBubblePanel() {
    //     return bubblePanel;
    // }

    // public BlasterPanel getBlasterPanel() {
    //     return blasterPanel;
    // }
}
