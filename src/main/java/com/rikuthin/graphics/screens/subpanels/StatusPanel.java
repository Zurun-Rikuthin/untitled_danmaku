package com.rikuthin.screen_panels.gameplay_subpanels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.rikuthin.GameFrame;
import com.rikuthin.GameManager;
import static com.rikuthin.utility.ButtonUtil.createButton;

/**
 * The StatusPanel displays the current score and elapsed time, and provides a
 * pause button for the game.
 * <p>
 * This panel is displayed at the top of the gameplay screen.
 * </p>
 */
public final class StatusPanel extends JPanel {

    private final JButton pauseMenuButton;
    private final JLabel scoreLabel;
    private final JLabel timerLabel;

    private int score;
    private int elapsedSeconds;

    /**
     * Constructs the StatusPanel, initialising the score and timer displays and
     * starting the game timer.
     */
    public StatusPanel() {
        // Set background colour and panel size.
        setBackground(new Color(87, 73, 100));
        setPreferredSize(new Dimension(GameFrame.FRAME_WIDTH, 60));
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        score = 0;
        elapsedSeconds = 0;

        // Create the font used for the button and labels.
        Font buttonFont = new Font(GameFrame.BODY_TYPEFACE, Font.PLAIN, 16);

        // Create the pause button.
        pauseMenuButton = createButton(
                "PAUSE", buttonFont, 100, 40, true,
                GameManager.getInstance()::onPause
        );

        // Create the score and timer labels.
        scoreLabel = createStatusLabel();
        timerLabel = createStatusLabel();

        // Arrange components with horizontal spacing.
        add(Box.createHorizontalStrut(20));
        add(pauseMenuButton);
        add(Box.createHorizontalGlue());
        add(scoreLabel);
        add(Box.createHorizontalGlue());
        add(timerLabel);
        add(Box.createHorizontalStrut(20));

        // Initialise the score display.
        updateScoreDisplay(0);
    }

    /**
     * Updates the displayed score
     *
     * @param score The new score.
     */
    public final void updateScoreDisplay(final int score) {
        scoreLabel.setText(String.format("Score: %d", score));
    }

    /**
     * Updates the timer label with a formatted elapsed time string.
     *
     * @param elapsedSeconds The elapsed time in seconds.
     */
    public void updateTimerDisplay(final int elapsedSeconds) {
        int minutes = elapsedSeconds / 60;
        int seconds = elapsedSeconds % 60;
        timerLabel.setText(String.format("Elapsed Time: %d:%02d", minutes, seconds));
    }

    /**
     * Creates a JLabel for displaying status information (score or timer).
     *
     * Buggy at the moment
     *
     * @return A configured JLabel.
     */
    private JLabel createStatusLabel() {
        JLabel label = new JLabel();
        label.setFont(new Font(GameFrame.BODY_TYPEFACE, Font.BOLD, 16));
        label.setForeground(Color.WHITE);

        // Will try to fix for the next assignment
        // label.setForeground(new Color(70, 0, 50));
        // label.setBorder(new RoundedBorder(Color.BLACK, Color.WHITE, 10));
        return label;
    }
}
