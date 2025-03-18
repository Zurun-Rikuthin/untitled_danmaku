package com.rikuthin.core;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.swing.Timer;

import com.rikuthin.entities.Bullet;
import com.rikuthin.entities.Enemy;
import com.rikuthin.entities.Player;
import com.rikuthin.graphics.GameFrame;
import com.rikuthin.graphics.dialogue.PauseMenuDialogue;
import com.rikuthin.graphics.screens.GameplayScreen;
import com.rikuthin.graphics.screens.subpanels.GamePanel;
import com.rikuthin.graphics.screens.subpanels.InfoPanel;
import com.rikuthin.interfaces.Updateable;

public class GameManager implements Updateable {

    // ----- STATIC VARIABLES -----
    private static GameManager instance;

    // ----- INSTANCE VARIABLES -----
    private Timer gameplayTimer;
    private Player player;
    private HashSet<Enemy> enemies;
    private HashSet<Bullet> bullets;
    private boolean isPaused;
    private boolean isGameplayActive;
    private GamePanel gamePanel;
    private InfoPanel infoPanel;
    private boolean wasInitCalled;

    // ----- CONSTRUCTORS -----
    /**
     * Private constructor to hide the public one.
     */
    private GameManager() {
        wasInitCalled = false;
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

    /**
     * Returns the current active {@link Player}.
     *
     * @return The player.
     */
    public Player getPlayer() {
        if (!wasInitCalled) {
            throw new IllegalStateException(String.format(
                    "%s: Call init() before attempting to access the player.",
                    this.getClass().getName()
            ));
        }
        return player;
    }

    /**
     * Returns all active {@link Enemy} instances.
     *
     * @return The enemies.
     */
    public Set<Enemy> getEnemies() {
        if (!wasInitCalled) {
            throw new IllegalStateException(String.format(
                    "%s: Call init() before attempting to access managed enemies.",
                    this.getClass().getName()
            ));
        }
        return enemies;
    }

    /**
     * Returns all active {@link Bullet} instances.
     *
     * @return The bullets.
     */
    public Set<Bullet> getBullets() {
        if (!wasInitCalled) {
            throw new IllegalStateException(String.format(
                    "%s: Call init() before attempting to access manages bullets.",
                    this.getClass().getName()
            ));
        }
        return bullets;
    }

    /**
     * Returns whether the game is currently paused.
     *
     * @return {@code true} if the game is paused; {@code false} otherwise.
     */
    public boolean isPaused() {
        return isPaused;
    }

    public boolean isGameplayActive() {
        return isGameplayActive;
    }

    public boolean wasInitCalled() {
        return wasInitCalled;
    }

    // ----- SETTERS -----
    public void setGameplayActive(boolean b) {
        this.isGameplayActive = b;
    }

    // /**
    //  * Sets the active {@link Player} instance.
    //  *
    //  * @param player The player.
    //  */
    // public void setPlayer(Player player) {
    //     if (!wasInitCalled) {
    //         throw new IllegalStateException(String.format(
    //                 "%s: Call init() before attempting to set the player.",
    //                 this.getClass().getName()
    //         ));
    //     }
    //     this.player = player;
    // }
    // ----- BUSINESS LOGIC METHODS -----
    /**
     * Initialises the GameManager for a new game.
     * <p>
     * Only works if the {@link GameplayScreen} is currently active.
     */
    public final void init(final GamePanel gamePanel, final InfoPanel infoPanel) {
        if (!isGameplayActive) {
            System.err.println(String.format(
                    "%s: Cannot initialize unless gameplay is active.",
                    this.getClass().getName()
            ));
            return;
        }

        if (gamePanel == null || infoPanel == null) {
            throw new IllegalStateException(String.format(
                    "%s: GamePanel and InfoPanel must be provided.",
                    this.getClass().getName()
            ));
        }

        this.gamePanel = gamePanel;
        this.infoPanel = infoPanel;

        // if (gameplayTimer != null) {
        //     gameplayTimer.stop(); // Stop any existing timer to prevent conflicts
        // }
        // gameplayTimer = new Timer(1000, e -> update()); // 16ms ~ 60 FPS
        // gameplayTimer.start();
        initialisePlayer(); // Player setup happens AFTER `gamePanel` is assigned
        enemies = new HashSet<>();
        bullets = new HashSet<>();
        isPaused = false;
        wasInitCalled = true;
    }

    /**
     * Clears game data (used when transitioning back to the main menu).
     */
    public final void clear() {
        if (!wasInitCalled) {
            return;
        }

        if (gameplayTimer != null) {
            gameplayTimer.stop();
            gameplayTimer = null;
        }
        gamePanel = null;
        infoPanel = null;
        player = null;
        enemies.clear();
        bullets.clear();
        isPaused = false;
        wasInitCalled = false;
    }

    /**
     * Adds a new {@link Enemy} instance to the managed list.
     * <p>
     * Only works if the {@link GameplayScreen} is currently active.
     *
     * @param enemy The new enemy.
     */
    public void addEnemy(final Enemy enemy) {
        if (!wasInitCalled) {
            throw new IllegalStateException(String.format(
                    "%s: Call init() before attempting to add an enemy.",
                    this.getClass().getName()
            ));
        }
        enemies.add(enemy);
    }

    /**
     * Adds a new bullet to the managed list.
     * <p>
     * Only works if the {@link GameplayScreen} is currently active.
     *
     * @param bullet The new bullet
     */
    public void addBullet(final Bullet bullet) {
        if (!wasInitCalled) {
            throw new IllegalStateException(String.format(
                    "%s: Call init() before attempting to add a bullet.",
                    this.getClass().getName()
            ));
        }
        bullets.add(bullet);
    }

    /**
     * Removes the first occurance of a specific bullet instance from the
     * managed list.
     * <p>
     * Only works if the {@link GameplayScreen} is currently active.
     *
     * @param bullet
     */
    public void removeBullet(final Bullet bullet) {
        if (!wasInitCalled) {
            throw new IllegalStateException(String.format(
                    "%s: Call init() before attempting to remove a bullet.",
                    this.getClass().getName()
            ));
        }
        bullets.remove(bullet);
    }

    // ----- OVERRIDDEN METHODS -----
    /**
     * Updates all managed objects and the current game state.
     * <p>
     * Only works if the {@link GameplayScreen} is currently active.
     */
    @Override
    public void update() {
        if (!wasInitCalled) {
            throw new IllegalStateException(String.format(
                    "%s: Call init() before attempting to update.",
                    this.getClass().getName()
            ));
        }

        if (player != null) {
            player.update();
        }
        updateEnemies();
        updateBullets();
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
     * Initialises the {@link Player} character.
     * <p>
     * Only works if the {@link GameplayScreen} is the current screen.
     */
    private void initialisePlayer() {
        if (!wasInitCalled || gamePanel == null) {
            throw new IllegalStateException("GameManager.init() must be called before initializing the player.");
        }

        HashSet<String> animations = Stream.of(
            "player"
                // "player-idle",
                // "player-move-up",
                // "player-move-down",
                // "player-move-left",
                // "player-move-right"
        ).collect(Collectors.toCollection(HashSet::new));

        player = new Player.PlayerBuilder(gamePanel)
                .animationKeys(animations)
                .build();
        // ,
        //         new Point(0, 0)
        // ,
        //         AnimationManager.getInstance().getAnimation("player")
        // ,
        //         20,
        //         5
        // );

        int x = (gamePanel.getWidth() - player.getSpriteWidth()) / 2;
        int y = (gamePanel.getHeight() - player.getSpriteHeight()) / 2;

        player.setPosition(new Point(x, y));
    }

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
     * Updates the list of managed enemies. Removes any enemy whose hitpoints
     * are fully depleted.
     */
    private void updateEnemies() {
        if (!wasInitCalled) {
            throw new IllegalStateException(String.format(
                    "%s: Call init() before attempting to update the enemies.",
                    this.getClass().getName()
            ));
        }

        if (enemies == null || enemies.isEmpty()) {
            return;
        }

        Iterator<Enemy> it = enemies.iterator();
        while (it.hasNext()) {
            Enemy e = it.next();
            if (e != null) {
                e.update();

                if (e.getCurrentHitPoints() <= 0) {
                    it.remove();
                }
            }
        }
    }

    /**
     * Updates the list of managed bullets. Removes any bullet that is fully
     * outside the panel boundaries.
     *
     * @param bullets The list of bullets.
     */
    private void updateBullets() {
        if (!wasInitCalled) {
            throw new IllegalStateException(String.format(
                    "%s: Call init() before attempting to update the bullets.",
                    this.getClass().getName()
            ));
        }

        if (bullets == null || bullets.isEmpty()) {
            return;
        }

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
