package com.rikuthin.graphics;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.Timer;
import javax.swing.WindowConstants;

import static com.rikuthin.core.App.TICK_SPEED_MS;
import com.rikuthin.graphics.screens.MainMenuScreen;
import com.rikuthin.graphics.screens.Screen;

public final class GameFrame extends JFrame {

    public static final int FRAME_WIDTH = 1024;
    public static final int FRAME_HEIGHT = 720;

    private final Timer gameLoopTimer;
    private BufferedImage backBuffer;
    private Graphics2D g2d;
    private Screen currentScreen;

    /**
     * Constructor to initialize the game frame, set the size, title, and add
     * the main menu and gameplay panels. Also initializes the GameManager
     * instance and sets the blaster and bubble panels.
     */
    public GameFrame() {
        setTitle("Untitled Danmaku");
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // Initialize double buffering
        backBuffer = new BufferedImage(FRAME_WIDTH, FRAME_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        g2d = backBuffer.createGraphics();

        setLocationRelativeTo(null);
        setVisible(true);

        setScreen(new MainMenuScreen(this));

        gameLoopTimer = new Timer((int) TICK_SPEED_MS, e -> {
            updateGame();
            renderGame();
            currentScreen.repaint();
        });
        gameLoopTimer.start();
    }

    /**
     * Paints the back buffer onto the JFrame.
     */
    @Override
    public void paint(java.awt.Graphics g) {
        super.paint(g);
        if (backBuffer != null) {
            g.drawImage(backBuffer, 0, 0, this);
        }
    }

    /**
     * Dynamically switches to a new screen, removing the old one to free up
     * memory.
     *
     * @param newScreen The new screen to display.
     */
    public void setScreen(final Screen newScreen) {
        if (currentScreen != null) {
            remove(currentScreen);
            currentScreen.cleanup();
        }

        currentScreen = newScreen;
        add(currentScreen);

        currentScreen.revalidate();
        currentScreen.repaint();
        currentScreen.requestFocusInWindow();
    }

    /**
     * Updates the game logic.
     */
    public void updateGame() {
        if (currentScreen != null) {
            currentScreen.update();
        }
    }

    /**
     * Renders the game onto the back buffer.
     */
    private void renderGame() {
        if (g2d == null || currentScreen == null) {
            return;
        }

        g2d.clearRect(0, 0, FRAME_WIDTH, FRAME_HEIGHT);
        currentScreen.render(g2d);
    }

    /**
     * Stops game loop.
     */
    public void stopGameLoop() {
        gameLoopTimer.stop();
    }
}
