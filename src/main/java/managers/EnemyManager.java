package managers;

import java.awt.Point;
import java.lang.StackWalker.StackFrame;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import com.rikuthin.entities.Player;
import com.rikuthin.entities.enemies.BlueMage;
import com.rikuthin.entities.enemies.Enemy;
import com.rikuthin.entities.enemies.MagentaMage;
import com.rikuthin.entities.enemies.RedMage;
import com.rikuthin.graphics.GameFrame;
import com.rikuthin.graphics.screens.subpanels.GamePanel;
import com.rikuthin.interfaces.Updateable;

public class EnemyManager implements Updateable {

    /**
     * The maximum number of enemies that can exist simultaneously.
     */
    private static final int MAX_ENEMY_COUNT = 10;
    /**
     * The cooldown duration (in milliseconds) before another {@link Enemy} can
     * be created.
     */
    private static final long ENEMY_CREATION_COOLDOWN_MS = 5000; // 5 seconds

    // ----- INSTANCE VARIABLES -----
    /**
     * Stores references to all active enemies on screen.
     */
    private HashSet<Enemy> enemies;
    /**
     * Random generator used by various methods.
     */
    private Random random;
    /**
     * Tracks whether the cooldown timer is active.
     */
    private boolean isOnCreationCooldown;
    /**
     * The elapsed time (in milliseconds) since the enemy creation cooldown
     * began.
     */
    private long elapsedCreationCooldownMs;
    /**
     * The timestamp (in milliseconds) of the last update call.
     */
    private long lastUpdateTime;

    // ----- CONSTRUCTORS -----
    public EnemyManager() {
        init();
    }

    // ----- GETTERS -----
    /**
     * Returns all active {@link Enemy} instances.
     *
     * @return The enemies.
     */
    public Set<Enemy> getEnemies() {
        ensureRunning("getEnemies");
        return enemies;
    }

    /**
     * Checks if the enemy creation cooldown is currently active.
     *
     * @return {@code true} if the cooldown is active, otherwise {@code false}.
     */
    public boolean isOnCreationCooldown() {
        return isOnCreationCooldown;
    }

    /**
     * Gets the cooldown time in milliseconds before another enemy can be
     * created.
     *
     * @return The cooldown time.
     */
    public long getEnemyCreationCooldownMs() {
        return ENEMY_CREATION_COOLDOWN_MS;
    }

    /**
     * Returns how many milliseconds of the enemy creation cooldown have passed.
     *
     * @return The elasped time.
     */
    public long getElapsedCreationCooldownMs() {
        return elapsedCreationCooldownMs;
    }

    /**
     * Returns the last time the manager was updated in milliseconds.
     *
     * @return The last update time.
     */
    public long getLastUpdateTime() {
        return lastUpdateTime;
    }

    // ----- BUSINESS LOGIC METHODS -----
    /**
     * Initializes the EnemyManager for a new game. This method sets up all the
     * necessary objects to manage enemies and clears old enemy data.
     */
    public final void init() {
        random = new Random();
        clear();
        isOnCreationCooldown = false;
        elapsedCreationCooldownMs = 0;
        lastUpdateTime = 0;
    }

    /**
     * Clears old enemy data.
     */
    public void clear() {
        enemies = new HashSet<>();
    }

    /**
     * Checks if a new enemy can be created.
     *
     * @return {@code true} if a new {@link Enemy} can be created, otherwise
     * {@code false}.
     */
    public boolean canCreateEnemy() {
        ensureRunning("canCreateEnemy");

        System.out.println("Enemies: " + enemies.size()
                + " | Elapsed Cooldown Time: " + elapsedCreationCooldownMs
                + " | On Cooldown: " + isOnCreationCooldown());

        return enemies.size() < MAX_ENEMY_COUNT && !isOnCreationCooldown();
    }

    /**
     * Adds a new {@link Enemy} instance to the managed list.
     *
     * @param enemy The new enemy.
     */
    public void addEnemy(final Enemy enemy) {
        ensureRunning("addEnemy");
        updateEnemyCreationCooldownTimer();

        if (canCreateEnemy()) {
            enemies.add(enemy);
        }
    }

    /**
     * Creates a random {@link Enemy} (if allowed) and adds it to the managed
     * list.
     *
     * @param player The {@link Player} the {@link Enemy} will target.
     */
    public void createRandomEnemy(final Player player) {
        ensureRunning("createRandomEnemy");
        updateEnemyCreationCooldownTimer();

        if (canCreateEnemy()) {
            GamePanel gamePanel = GameManager.getInstance().getGamePanel();
            Enemy newEnemy;
            int enemyType = random.nextInt(3);

            switch (enemyType) {
                case 0 ->
                    newEnemy = new RedMage.RedMageBuilder(gamePanel).build();
                case 1 ->
                    newEnemy = new BlueMage.BlueMageBuilder(gamePanel).build();
                case 2 ->
                    newEnemy = new MagentaMage.MagentaMageBuilder(gamePanel).build();
                default ->
                    throw new IllegalStateException("Switch-case recieved unexpected value: " + enemyType);
            }

            newEnemy.setPosition(getRandomSpawnPoint());
            newEnemy.setTarget(player.getPosition());

            int xMoveSpeed = random.nextInt(5);
            boolean moveLeft = random.nextBoolean();

            newEnemy.setVelocityX(moveLeft ? -xMoveSpeed : xMoveSpeed);

            enemies.add(newEnemy);

            isOnCreationCooldown = true;
            elapsedCreationCooldownMs = 0;
        }
    }

    // ----- OVERRIDDEN METHODS -----
    /**
     * Updates all managed objects and the current game state.
     */
    @Override
    public void update() {
        ensureRunning("update");

        lastUpdateTime = System.currentTimeMillis();

        createRandomEnemy(GameManager.getInstance().getPlayer());
        updateEnemies();
    }

    // ----- HELPER METHODS -----
    private void ensureRunning(String methodName) {
        if (!GameManager.getInstance().isRunning()) {
            StackWalker walker = StackWalker.getInstance();
            StackFrame caller = walker.walk(frames -> frames.skip(1).findFirst().orElse(null));

            throw new IllegalStateException(String.format(
                    "%s.%s: Cannot call %s() when GameMaager is not in the RUNNING state.",
                    caller != null ? caller.getClassName() : "UnknownClass",
                    caller != null ? caller.getMethodName() : "UnknownMethod",
                    methodName
            ));
        }
    }

    /**
     * Updates the attack cooldown timer. This tracks how much time has passed
     * since the last enemy was created.
     */
    private void updateEnemyCreationCooldownTimer() {
        if (isOnCreationCooldown) {
            long currentTime = System.currentTimeMillis();
            long deltaTime = currentTime - lastUpdateTime;

            // Increment the cooldown timer
            elapsedCreationCooldownMs += deltaTime;

            // If the cooldown time is passed, reset the flag and elapsed time
            if (elapsedCreationCooldownMs >= ENEMY_CREATION_COOLDOWN_MS) {
                isOnCreationCooldown = false;
                elapsedCreationCooldownMs = 0;
            }
        }
    }

    /**
     * Updates the list of managed enemies and removes any defeated enemies.
     */
    private void updateEnemies() {
        ensureRunning("updateEnemies");

        if (enemies.isEmpty()) {
            return;
        }

        enemies.removeIf(enemy -> {
            enemy.update();
            return enemy.getCurrentHitPoints() <= 0;
        });
    }

    private Point getRandomSpawnPoint() {
        int x = random.nextInt(GameFrame.FRAME_HEIGHT);
        int y = random.nextInt(GameFrame.FRAME_HEIGHT * 1 / 5, GameFrame.FRAME_HEIGHT * 3 / 5);
        return new Point(x, y);
    }
}
