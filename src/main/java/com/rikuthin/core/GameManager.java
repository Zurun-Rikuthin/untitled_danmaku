package com.rikuthin.core;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.lang.StackWalker.StackFrame;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.swing.Timer;

import com.rikuthin.entities.Bullet;
import com.rikuthin.entities.BulletSpawner;
import com.rikuthin.entities.Player;
import com.rikuthin.entities.enemies.Enemy;
import com.rikuthin.graphics.GameFrame;
import com.rikuthin.graphics.dialogue.PauseMenuDialogue;
import com.rikuthin.graphics.screens.subpanels.GamePanel;
import com.rikuthin.graphics.screens.subpanels.InfoPanel;
import com.rikuthin.interfaces.Updateable;

public class GameManager implements Updateable {

    // ----- ENUMERATORS -----
    /**
     * Enum representing the possible game states.
     */
    private enum GameState {
        NOT_INITIALIZED {
            @Override
            public void handleState(GameManager gameManager) {
                // Cannot run game, must initialize first.
                throw new IllegalStateException("Game is not initialized.");
            }
        },
        INITIALIZING {
            @Override
            public void handleState(GameManager gameManager) {
                // Game is in the process of initializing, don't allow any operations.
                throw new IllegalStateException("Game is currently initializing.");
            }
        },
        PAUSED {
            @Override
            public void handleState(GameManager gameManager) {
                // Implement pause behavior
                gameManager.showPauseMenu();
            }
        },
        RUNNING {
            @Override
            public void handleState(GameManager gameManager) {
                // Handle game running logic (timer, game updates)
                gameManager.update();
            }
        },
        STOPPED {
            @Override
            public void handleState(GameManager gameManager) {
                gameManager.clear();
            }
        };

        public abstract void handleState(GameManager gameManager);
    }

    // ----- STATIC VARIABLES -----
    private static final int ENEMY_LIMIT = 20;
    private static GameManager instance;

    // ----- INSTANCE VARIABLES -----
    private GameState currentState = GameState.NOT_INITIALIZED;
    /**
     * Measures how long the current game has been active.
     */
    private Timer gameplayTimer;
    /**
     * The player character.
     */
    private Player player;
    /**
     * Stores references to all active enemies on screen.
     */
    private HashSet<Enemy> enemies;
    /**
     * Stores references to all active bullets on screen.
     */
    private HashSet<Bullet> bullets;
    /**
     * Reference to the where all game entities are displayed.
     */
    private GamePanel gamePanel;
    /**
     * Reference to the panel where game information is displayed.
     */
    private InfoPanel infoPanel;

    // ----- CONSTRUCTORS -----
    /**
     * Private constructor to prevent direct instantiation. Singleton pattern.
     */
    private GameManager() {
        // Start in a non-initialized state.
        currentState = GameState.NOT_INITIALIZED;
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
        ensureInitialized("getPlayer");
        return player;
    }

    /**
     * Returns all active {@link Enemy} instances.
     *
     * @return The enemies.
     */
    public Set<Enemy> getEnemies() {
        ensureInitialized("getEnemies");
        return enemies;
    }

    /**
     * Returns all active {@link Bullet} instances.
     *
     * @return The bullets.
     */
    public Set<Bullet> getBullets() {
        ensureInitialized("getBullets");
        return bullets;
    }

    /**
     * Returns whether the game is currently paused.
     *
     * @return {@code true} if the game is paused; {@code false} otherwise.
     */
    public boolean isPaused() {
        return currentState == GameState.PAUSED;
    }

    public boolean isRunning() {
        return currentState == GameState.RUNNING;
    }

    // ----- BUSINESS LOGIC METHODS -----
    /**
     * Initializes the GameManager for a new game. This method must be called
     * before the game can run. It sets up all the necessary objects to start
     * the game.
     *
     * @param gamePanel The panel where the game is displayed.
     * @param infoPanel The panel where the game information is displayed.
     */
    public final void init(final GamePanel gamePanel, final InfoPanel infoPanel) {
        if (currentState != GameState.NOT_INITIALIZED && currentState != GameState.INITIALIZING) {
            System.err.println(String.format(
                    "%s: Cannot initialize unless the game is in the NOT_INITIALIZED or INITIALIZING state.",
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

        // Transition to initializing state during setup
        currentState = GameState.INITIALIZING;

        initialisePlayer();
        enemies = new HashSet<>();
        bullets = new HashSet<>();
        setGamePaused(false);

        // Initialization complete. Begin running.
        currentState = GameState.RUNNING;
        createRandomEnemy();
    }

    /**
     * Clears game data (used when transitioning back to the main menu).
     */
    public final void clear() {
        if (currentState != GameState.NOT_INITIALIZED) {
            stopGameplayTimer();
            gamePanel = null;
            infoPanel = null;
            player = null;
            enemies.clear();
            bullets.clear();
        }
    }

    /**
     * Adds a new {@link Enemy} instance to the managed list.
     *
     * @param enemy The new enemy.
     */
    public void addEnemy(final Enemy enemy) {
        ensureInitialized("addEnemy");

        if (enemy != null){
            enemies.add(enemy);
        }
    }

    /**
     * Adds a new bullet to the managed list.
     *
     * @param bullet The new bullet
     */
    public void addBullet(final Bullet bullet) {
        ensureInitialized("addBullet");

        if (bullet != null) {
            bullets.add(bullet);
        }
    }

    /**
     * Removes the first occurance of a specific bullet instance from the
     * managed list.
     *
     * @param bullet
     */
    public void removeBullet(final Bullet bullet) {
        ensureInitialized("removeBullet");
        bullets.remove(bullet);
    }

    /**
     * Pauses the game when the pause button is clicked. Stops the timer and
     * displays the pause menu dialogue.
     *
     * @param e The action event triggered by clicking the pause button.
     */
    public void onPause(ActionEvent e) {
        setGamePaused(true);
    }

    // ----- OVERRIDDEN METHODS -----
    /**
     * Updates all managed objects and the current game state.
     */
    @Override
    public void update() {
        ensureInitialized("update");

        if (player != null) {
            player.update();
        }
        updateEnemies();
        updateBullets();
    }

    // ----- HELPER METHODS -----
    private void ensureInitialized(String methodName) {
        if (currentState == GameState.NOT_INITIALIZED || currentState == GameState.INITIALIZING) {
            StackWalker walker = StackWalker.getInstance();
            StackFrame caller = walker.walk(frames -> frames.skip(1).findFirst().orElse(null));

            throw new IllegalStateException(String.format(
                    "%s.%s: Cannot call %s() before init().",
                    caller != null ? caller.getClassName() : "UnknownClass",
                    caller != null ? caller.getMethodName() : "UnknownMethod",
                    methodName
            ));
        }
    }

    /**
     * Initialises the {@link Player} character.
     */
    private void initialisePlayer() {
        if (currentState != GameState.INITIALIZING) {
            throw new IllegalStateException(
                    "Cannot initialise player without being in the INITIALIZING state."
            );
        }

        HashSet<String> playerAnimationKeys = Stream.of(
                "player-death",
                "player-idle",
                "player-walk-up-left",
                "player-walk-up-right",
                "player-walk-up"
        ).collect(Collectors.toCollection(HashSet::new));

        player = new Player.PlayerBuilder(gamePanel)
                .invisibility(false)
                .collidability(true)
                .animationKeys(playerAnimationKeys)
                .currentAnimationKey("player-idle")
                .maxHitPoints(20)
                .currentHitPoints(20)
                .build();

        // Trying to do this dynamically wasn't working, so hard-coding for now
        int x = GameFrame.FRAME_WIDTH - GameFrame.FRAME_HEIGHT - (player.getSpriteWidth() / 2);
        int y = GameFrame.FRAME_HEIGHT - (player.getSpriteHeight() / 2);

        player.setPosition(new Point(x, y));

        HashSet<String> playerBulletAnimationKeys = Stream.of("player-bullet").collect(Collectors.toCollection(HashSet::new));

        BulletSpawner spawner = new BulletSpawner.BulletSpawnerBuilder(gamePanel, player)
                .bulletDamage(1)
                .bulletVelocityY(20)
                .bulletAnimationKeys(playerBulletAnimationKeys)
                .currentBulletAnimationKey("player-bullet")
                .build();

        player.setBulletSpawner(spawner);
    }

    /**
     * Creates a random {@link Enemy} and adds it to the managed list.
     */
    private void createRandomEnemy() {
        if (currentState != GameState.RUNNING) {
            throw new IllegalStateException(
                    "Cannot create enemies without being in the RUNNING state."
            );
        }

        Random random = new Random();

        HashSet<String> enemyAnimationKeys = Stream.of(
                "mage-guardian-magenta"
        ).collect(Collectors.toCollection(HashSet::new));

        Enemy newEnemy = new Enemy.EnemyBuilder(gamePanel)
                .invisibility(false)
                .collidability(true)
                .animationKeys(enemyAnimationKeys)
                .currentAnimationKey("mage-guardian-magenta")
                .maxHitPoints(20)
                .currentHitPoints(20)
                .build();

        
        // Trying to do this dynamically wasn't working, so hard-coding for now
        int x = 300;
        int y = 200;

        newEnemy.setPosition(new Point(x, y));

        HashSet<String> playerBulletAnimationKeys = Stream.of("enemy-bullet").collect(Collectors.toCollection(HashSet::new));

        BulletSpawner spawner = new BulletSpawner.BulletSpawnerBuilder(gamePanel, player)
                .bulletDamage(1)
                .bulletVelocityY(20)
                .bulletAnimationKeys(playerBulletAnimationKeys)
                .currentBulletAnimationKey("enemy-bullet")
                .build();

        newEnemy.setBulletSpawner(spawner);
        addEnemy(newEnemy);
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
     * Resumes the game.
     */
    private void onResume() {
        setGamePaused(false);
    }

    /**
     * Updates the list of managed enemies and removes any defeated enemies.
     */
    private void updateEnemies() {
        ensureInitialized("updateEnemies");

        if (enemies.isEmpty()) {
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
     * Updates the list of managed bullets and removes any bullets that are
     * outside the panel boundaries.
     *
     * @param bullets The list of bullets.
     */
    private void updateBullets() {
        ensureInitialized("updateBullets");

        if (bullets.isEmpty()) {
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

    /**
     * Pauses or resumes the game based on the given parameter.
     *
     * @param paused Whether the game should be paused.
     */
    private void setGamePaused(boolean paused) {
        if (paused) {
            currentState = GameState.PAUSED;
            stopGameplayTimer();
            showPauseMenu();
        } else {
            currentState = GameState.RUNNING;
            startGameplayTimer();
        }
    }

    /**
     * Starts the current gameplay timer.
     */
    private void startGameplayTimer() {
        if (gameplayTimer == null) {
            gameplayTimer = new Timer(1000 / 60, e -> update());
        }
        gameplayTimer.start();
    }

    /**
     * Stops the gameplay timer.
     */
    private void stopGameplayTimer() {
        if (gameplayTimer != null) {
            gameplayTimer.stop();
        }
    }
}

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
