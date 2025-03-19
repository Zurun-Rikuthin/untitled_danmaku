package managers;

import java.awt.Point;
import java.lang.StackWalker.StackFrame;
import java.util.HashSet;
import java.util.Iterator;
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
    private static final int ENEMY_LIMIT = 20;

    // ----- INSTANCE VARIABLES -----
    /**
     * Stores references to all active enemies on screen.
     */
    private HashSet<Enemy> enemies;
    /**
     * Random number generator.
     */
    private Random random;

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

    // ----- BUSINESS LOGIC METHODS -----
    /**
     * Initializes the EnemyManager for a new game. This method sets up all the
     * necessary objects to manage enemies and clears old enemy data.
     */
    public final void init() {
        random = new Random();
        clear();
    }

    /**
     * Clears old enemy data.
     */
    public void clear() {
        enemies = new HashSet<>();
    }

    /**
     * Adds a new {@link Enemy} instance to the managed list.
     *
     * @param enemy The new enemy.
     */
    public void addEnemy(final Enemy enemy) {
        ensureRunning("addEnemy");

        if (enemy != null) {
            enemies.add(enemy);
        }
    }

    /**
     * Creates a random {@link Enemy} and adds it to the managed list.
     *
     * @param player The {@link Player} the {@link Enemy} will target.
     */
    public void createRandomEnemy(final Player player) {
        ensureRunning("createRandomEnemy");

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
        addEnemy(newEnemy);
    }

    // ----- OVERRIDDEN METHODS -----
    /**
     * Updates all managed objects and the current game state.
     */
    @Override
    public void update() {
        ensureRunning("update");
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
     * Updates the list of managed enemies and removes any defeated enemies.
     */
    private void updateEnemies() {
        ensureRunning("updateEnemies");

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

    private Point getRandomSpawnPoint() {
        int x = random.nextInt(GameFrame.FRAME_WIDTH - GameFrame.FRAME_HEIGHT);
        int y = random.nextInt(500);
        return new Point(x, y);
    }
}
