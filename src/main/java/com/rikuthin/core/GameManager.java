package com.rikuthin.core;

import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Timer;

import com.rikuthin.entities.Bullet;
import com.rikuthin.entities.Entity;
import com.rikuthin.graphics.GameFrame;
import com.rikuthin.graphics.dialogue.PauseMenuDialogue;
import com.rikuthin.graphics.screens.subpanels.GamePanel;
import com.rikuthin.graphics.screens.subpanels.InfoPanel;

// import com.rikuthin.dialogue_panels.PauseMenuDialogue;
// import com.rikuthin.game_objects.Blaster;
// import com.rikuthin.game_objects.Bubble;
// import com.rikuthin.screen_panels.gameplay_subpanels.BlasterPanel;
// import com.rikuthin.screen_panels.gameplay_subpanels.BubblePanel;
// import com.rikuthin.screen_panels.gameplay_subpanels.StatusPanel;
// import com.rikuthin.utility.RandomColour;
public class GameManager {

    // ----- STATIC VARIABLES -----
    private static GameManager instance;

    // ----- INSTANCE VARIABLES -----
    private GamePanel gamePanel;
    private InfoPanel infoPanel;
    private Timer gameplayTimer;
    private ArrayList<Bullet> bullets;
    // private int elapsedSeconds;
    // private int score;
    // private boolean canShootBlaster;
    // private boolean gameActive;
    private boolean isPaused;

    // ----- CONSTRUCTORS -----
    /**
     * Private constructor to hide the public one.
     */
    private GameManager() {
        init();
    }

    // ----- GETTERS -----
    /**
     * Returns the singleton instance of the GameManager.
     *
     * @return The single instance of GameManager.
     */
    public static GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }

    public GamePanel getGamePanel() {
        return gamePanel;
    }

    public InfoPanel getInfoPanel() {
        return infoPanel;
    }

    public List<Bullet> getBullets() {
        return bullets;
    }

    public boolean isPaused() {
        return isPaused;  // This should be updated when the pause menu is opened/closed
    }

    // ----- SETTERS -----
    public void setGamePanel(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    public void setInfoPanel(InfoPanel infoPanel) {
        this.infoPanel = infoPanel;
    }

    // /**
    //  * Starts a new game by resetting/initialising game values/objects.
    //  */
    // public void startGame() {
    //     if (blasterPanel == null || bubblePanel == null) {
    //         throw new IllegalStateException("Error: Game cannot start. BlasterPanel and BubblePanel must be set first.");
    //     }
    //     remainingBubbles = 100;
    //     elapsedSeconds = 0;
    //     score = 0;
    //     blasterPanel.updateRemainingBubblesCounter(remainingBubbles);
    //     bubblePanel.initialiseWalls();
    //     gameActive = true;
    //     canShootBlaster = true;
    //     isPaused = false;
    //     // Initialise and start the game timer (updates every second).
    //     gameTimer = new Timer(1000, this::onTimerTick);
    //     gameTimer.start();
    // }
    // /**
    //  * Shoots a bubble towards a target point. Reduces the number of remaining
    //  * bubbles if possible.
    //  *
    //  * @param target The point to shoot the bubble towards.
    //  */
    // public void shootBubble(Point target) {
    //     if (!gameActive) {
    //         throw new IllegalStateException("Error: Cannot shoot bubble. Game has not started.");
    //     }
    //     if (remainingBubbles > 0) {
    //         if (canShootBlaster) {
    //             canShootBlaster = false;
    //             Blaster blaster = blasterPanel.getBlaster();
    //             Bubble newBubble = blaster.shootBubble(target, nextRandomColour());
    //             bubblePanel.addBubble(newBubble);
    //             remainingBubbles--;
    //             blasterPanel.updateRemainingBubblesCounter(remainingBubbles);
    //         } else {
    //             System.err.println("Warning: Bubble already fired. Wait for it to stop moving.");
    //         }
    //     } else {
    //         System.err.println("Warning: No more bubbles left to shoot.");
    //     }
    // }
    // public void onBubbleMovementComplete() {
    //     canShootBlaster = true;
    // }
    // /**
    //  * Chooses the next bubble's color randomly.
    //  *
    //  * @return The next random colour.
    //  */
    // private Color nextRandomColour() {
    //     if (!gameActive) {
    //         throw new IllegalStateException("Cannot select color when game is not active.");
    //     }
    //     return RandomColour.getRandomColour();
    // }
    // public int getScore() {
    //     return score;
    // }
    // public void setScore(final int score) {
    //     this.score = score;
    //     updateScoreDisplay();
    // }
    // /**
    //  * Updates the displayed score and internal score counter.
    //  *
    //  * @param score The new score.
    //  */
    // public final void updateScoreDisplay() {
    //     statusPanel.updateScoreDisplay(score);
    // }
    // /**
    //  * Updates the timer label with a formatted elapsed time string.
    //  *
    //  * @param elapsedSeconds The elapsed time in seconds.
    //  */
    // public void updateTimerDisplay() {
    //     statusPanel.updateTimerDisplay(elapsedSeconds);
    // }
    // /**
    //  * Action invoked by the game timer every second. Increments the elapsed
    //  * time and updates the timer display.
    //  *
    //  * @param e The action event triggered by the timer.
    //  */
    // private void onTimerTick(ActionEvent e) {
    //     elapsedSeconds++;
    //     updateTimerDisplay();
    // }
    // ----- BUSINESS LOGIC METHODS -----
    /**
     * Initialises the GameManager and its attributes..
     */
    public final void init() {
        bullets = new ArrayList<>();
        // remainingBubbles = 0;
        // elapsedSeconds = 0;
        // score = 0;
        // gameActive = false;
        // canShootBlaster = false;
        isPaused = false;
    }

    /**
     * Adds a new bullet to the managed list.
     *
     * @param bullet The new bullet
     */
    public void addBullet(final Bullet bullet) {
        bullets.add(bullet);
    }

    /**
     * Removes the first occurance of a specific bullet instance from the
     * managed list.
     *
     * @param bullet
     */
    public void removeBullet(final Bullet bullet) {
        bullets.remove(bullet);
    }

    /**
     * Updates all bullets in the managed list.
     *
     * @param bullet
     */
    public void renderBullets(Graphics2D g2d) {
        for (Bullet b : bullets) {
            b.safeRender(g2d);
        }
    }

    /**
     * Updates all bullets in the managed list.
     *
     * @param bullet
     */
    public void updateBullets() {
        for (Bullet b : bullets) {
            b.update();
        }
    }

    /**
     * Removes bullet that are fully off-screen.
     */
    public void removeOffScreenBullets() {
        bullets.removeIf(Entity::isFullyOutsidePanel);
    }

    /**
     * Pauses the game when the pause button is clicked. Stops the timer and
     * displays the pause menu dialogue.
     *
     * @param e The action event triggered by clicking the pause button.
     */
    public void onPause(ActionEvent e) {
        isPaused = true;
        gameplayTimer.stop();
        showPauseMenu();
    }

    // ----- HELPER METHODS -----
    /**
     * Displays the pause menu dialogue.
     */
    private void showPauseMenu() {
        PauseMenuDialogue pauseMenuDialogue = new PauseMenuDialogue(
                (GameFrame) gamePanel.getTopLevelAncestor(),
                this::onResume
        );
        pauseMenuDialogue.setVisible(true);
    }

    /**
     * Resumes the game by restarting the game timer.
     */
    private void onResume() {
        gameplayTimer.start();
        isPaused = false;
    }
}
