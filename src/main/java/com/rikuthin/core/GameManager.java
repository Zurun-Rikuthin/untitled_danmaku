package com.rikuthin.core;

import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.swing.Timer;

import com.rikuthin.entities.Bullet;
import com.rikuthin.graphics.GameFrame;
import com.rikuthin.graphics.dialogue.PauseMenuDialogue;
import com.rikuthin.graphics.screens.subpanels.GamePanel;
import com.rikuthin.graphics.screens.subpanels.InfoPanel;
import com.rikuthin.interfaces.Renderable;
import com.rikuthin.interfaces.Updateable;

public class GameManager implements Updateable, Renderable {

    // ----- ENUMARATORS -----
    /**
     * Whether the bullet belongs to the player (thus hurting enemies) or to
     * enemies (thus hurting the player).
     */
    public enum BulletTeam {
        PLAYER,
        ENEMY
    }

    // ----- STATIC VARIABLES -----
    private static GameManager instance;

    // ----- INSTANCE VARIABLES -----
    private GamePanel gamePanel;
    private InfoPanel infoPanel;
    private Timer gameplayTimer;
    private HashSet<Bullet> enemyBullets;
    private HashSet<Bullet> playerBullets;
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

    public Set<Bullet> getEnemyBullets() {
        return enemyBullets;
    }

    public Set<Bullet> getPlayerBullets() {
        return playerBullets;
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

    // ----- BUSINESS LOGIC METHODS -----
    /**
     * Initialises the GameManager and its attributes..
     */
    public final void init() {
        enemyBullets = new HashSet<>();
        playerBullets = new HashSet<>();
        isPaused = false;
    }

    /**
     * Adds a new bullet to the managed list.
     *
     * @param bullet The new bullet
     */
    public void addBullet(final Bullet bullet) {
        enemyBullets.add(bullet);
    }

    /**
     * Removes the first occurance of a specific bullet instance from the
     * managed list.
     *
     * @param bullet
     */
    public void removeBullet(final Bullet bullet) {
        enemyBullets.remove(bullet);
    }

    // ----- OVERRIDDEN METHODS -----
    /**
     * Calls the update method for all managed objects.
     */
    @Override
    public void update() {
        updateBullets(enemyBullets);
        updateBullets(playerBullets);
    }

    /**
     * Calls the render method for all managed objects.
     *
     * @param g2d
     */
    @Override
    public void render(Graphics2D g2d) {
        for (Bullet b : enemyBullets) {
            b.safeRender(g2d);
        }
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

    /**
     * Updates a list of managed bullets. Removes any bullet that is fully
     * outside the panel boundaries.
     *
     * @param bullets The list of bullets.
     */
    private void updateBullets(Set<Bullet> bullets) {
        Iterator<Bullet> it = bullets.iterator();
        while (it.hasNext()) {
            Bullet b = it.next();
            if (b != null) {
                b.update();
                if (b.isFullyOutsidePanel()) {
                    it.remove();
                }
            }
        }
    }
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
