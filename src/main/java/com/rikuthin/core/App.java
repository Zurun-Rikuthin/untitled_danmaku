package com.rikuthin.core;

import javax.swing.SwingUtilities;

import com.rikuthin.graphics.GameFrame;

public class App {

    /**
     * How often the various threads should update themselves (milliseconds).
     */
    public static final long TICK_SPEED_MS = (long) 16.7;    // 16.7 ms is approx. 60 FPS

    /**
     * The entry point for the application. This method schedules the creation
     * of the {@link GameFrame} on the Event Dispatch Thread (EDT).
     *
     * @param args The command line arguments (not used in this application).
     */
    public static void main(String[] args) {
        // Schedules GameFrame creation on the EDT
        SwingUtilities.invokeLater(GameFrame::new);
    }
}
