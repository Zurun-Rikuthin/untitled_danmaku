package managers;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.lang.StackWalker.StackFrame;
import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.swing.Timer;

import com.rikuthin.entities.Player;
import com.rikuthin.entities.bullets.BulletSpawner;
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

        // Planned more for, currently unused.
        public abstract void handleState(GameManager gameManager);
    }

    // ----- STATIC VARIABLES -----
    /**
     * Singleton instance of the {@link GameManager}.
     * <p>
     * This static field holds the unique instance of the {@link GameManager}.
     * It is lazily initialized when {@link #getInstance()} is first called. The
     * singleton pattern ensures that only one instance of the
     * {@link GameManager} exists throughout the lifetime of the application.
     */
    private static GameManager instance;

    // ----- INSTANCE VARIABLES -----
    /**
     * Manages all enemy-related logic, including spawning, tracking, and updating enemies.
     * This instance is responsible for handling enemy creation cooldowns, updating enemy states,
     * and ensuring that the maximum enemy limit is enforced.
     */
    private final EnemyManager enemyManager;
    /**
     * Manages all bullet-related logic, including creation, tracking, and updating bullets.
     * This instance is responsible for handling bullet collisions, removing expired bullets,
     * and updating their movement over time.
     */
    private final BulletManager bulletManager;
    /**
     * Represents the current state of the game. This determines what actions  
     * can be performed at any given time and helps enforce state-based logic.  
     * Initialized to {@code GameState.NOT_INITIALIZED} by default.
     */
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
        currentState = GameState.NOT_INITIALIZED;
        enemyManager = new EnemyManager();
        bulletManager = new BulletManager();
    }

    // ----- GETTERS -----
    /**
     * Returns the singleton instance of the {@link GameManager}.
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

    /**
     * Returns the current active {@link Player}.
     *
     * @return The player.
     */
    public Player getPlayer() {
        ensureInitialized("getPlayer");
        return player;
    }

    public EnemyManager getEnemyManager() {
        ensureRunning("getEnemyManager");
        return enemyManager;
    }

    public BulletManager getBulletManager() {
        ensureRunning("getBulletManager");
        return bulletManager;
    }

    // ----- BUSINESS LOGIC METHODS -----
    /**
     * Returns whether the game is currently initializing.
     *
     * @return {@code true} if the game is initializing; {@code false}
     * otherwise.
     */
    public boolean isInitializing() {
        return currentState == GameState.INITIALIZING;
    }

    /**
     * Returns whether the game is currently paused.
     *
     * @return {@code true} if the game is paused; {@code false} otherwise.
     */
    public boolean isPaused() {
        return currentState == GameState.PAUSED;
    }

    /**
     * Returns whether the game is currently running.
     *
     * @return {@code true} if the game is running; {@code false} otherwise.
     */
    public boolean isRunning() {
        return currentState == GameState.RUNNING;
    }

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
        enemyManager.init();
        bulletManager.init();
        setGamePaused(false);

        // Initialization complete. Begin running.
        currentState = GameState.RUNNING;
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
            enemyManager.clear();
            bulletManager.clear();
        }
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
        enemyManager.update();
        bulletManager.update();
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

    private void ensureRunning(String methodName) {
        if (currentState != GameState.RUNNING) {
            StackWalker walker = StackWalker.getInstance();
            StackFrame caller = walker.walk(frames -> frames.skip(1).findFirst().orElse(null));

            throw new IllegalStateException(String.format(
                    "%s.%s: Cannot call %s() while not in the RUNNING state.",
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
        int x = (GameFrame.FRAME_HEIGHT / 2) - (player.getSpriteWidth() / 2);
        int y = GameFrame.FRAME_HEIGHT - (2 * player.getSpriteHeight());

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
