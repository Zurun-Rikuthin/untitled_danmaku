
package com.rikuthin.graphics.screens.subpanels;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import static javax.swing.Box.createHorizontalStrut;
import static javax.swing.Box.createVerticalStrut;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import com.rikuthin.core.GameManager;
import com.rikuthin.graphics.GameFrame;
import com.rikuthin.graphics.ImageManager;
import com.rikuthin.graphics.UIConstants;
import com.rikuthin.utility.ButtonUtil;

/**
 * The InfoPanel displays game information (e.g., player health, elapsed time,
 * and score) pause button for the game.
 * <p>
 * This panel is displayed at the right of the gameplay screen.
 * </p>
 */

public final class InfoPanel extends JPanel {

    private final String backgroundImageURL;
    private final JButton pauseButton;
    private final JLabel waveCounterLabel;
    private final JLabel gameplayTimerLabel;
    private final JLabel highscoreLabel;
    private final JLabel scoreLabel;
    private final JPanel hpCounterPanel;
    private final JPanel bombCounterPanel;
    private final JTextArea infoTextArea;
    private BufferedImage backgroundImage;

    /**
     * Constructs the InfoPanel.
     */
    public InfoPanel() {
        // Background colour used as a backup in case the image deosn't load.
        setBackground(new Color(87, 73, 100));
        setPreferredSize(new Dimension(GameFrame.FRAME_WIDTH - GameFrame.FRAME_HEIGHT, GameFrame.FRAME_HEIGHT));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setAlignmentX(Component.CENTER_ALIGNMENT);
        setAlignmentY(CENTER_ALIGNMENT);

        backgroundImageURL = "/images/backgrounds/info_panel.png";
        backgroundImage = ImageManager.loadBufferedImage(backgroundImageURL);

        // ----- Initialise elements -----
        JPanel topRow = new JPanel();
        topRow.setLayout(new BoxLayout(topRow, BoxLayout.X_AXIS));

        JPanel topRowLabels = new JPanel();
        topRowLabels.setLayout(new BoxLayout(topRowLabels, BoxLayout.Y_AXIS));
        
        waveCounterLabel = new JLabel("Wave <lorem ipsum>");
        waveCounterLabel.setFont(UIConstants.BODY_FONT);

        gameplayTimerLabel = new JLabel("HH:MM.SS");
        gameplayTimerLabel.setFont(UIConstants.BODY_FONT);

        pauseButton = ButtonUtil.createButtonWithIcon("/images/icons/pause-button.png", 64, 64, false, GameManager.getInstance()::onPause);        

        Dimension scoreLabelSize = new Dimension(284, 80);
        highscoreLabel = new JLabel("Highscore: <lorem ipsum>");
        highscoreLabel.setFont(UIConstants.BODY_FONT);
        highscoreLabel.setPreferredSize(scoreLabelSize);
        highscoreLabel.setMinimumSize(scoreLabelSize);
        highscoreLabel.setMaximumSize(scoreLabelSize);
        highscoreLabel.setBackground(Color.WHITE);

        scoreLabel = new JLabel("Score: <lorem ipsum>");
        scoreLabel.setFont(UIConstants.BODY_FONT);
        scoreLabel.setPreferredSize(scoreLabelSize);
        scoreLabel.setMinimumSize(scoreLabelSize);
        scoreLabel.setMaximumSize(scoreLabelSize);
        scoreLabel.setBackground(Color.WHITE);

        Dimension counterPanelSize = new Dimension(284, 80);
        hpCounterPanel = new JPanel(true);
        hpCounterPanel.setPreferredSize(counterPanelSize);
        hpCounterPanel.setMinimumSize(counterPanelSize);
        hpCounterPanel.setMaximumSize(counterPanelSize);
        hpCounterPanel.setBackground(Color.WHITE);

        bombCounterPanel = new JPanel(true);
        bombCounterPanel.setPreferredSize(counterPanelSize);
        bombCounterPanel.setMinimumSize(counterPanelSize);
        bombCounterPanel.setMaximumSize(counterPanelSize);
        bombCounterPanel.setBackground(Color.WHITE);

        StringBuilder sb = new StringBuilder("Controls:");
        sb.append(String.format("%n    - WSAD or arrow keys to move"));
        sb.append(String.format("%n    - SPACE to use a bomb (destroys all enemy bullets on screen."));
        sb.append(String.format("%n%nGoal:"));
        sb.append(String.format("%n    - Shoot enemies to gain points."));
        sb.append(String.format("%n    - Don't get hit by enemy bullets."));
        infoTextArea = new JTextArea(sb.toString());
        infoTextArea.setPreferredSize(new Dimension(getWidth() - 20, 300));
        infoTextArea.setBackground(Color.WHITE);
        infoTextArea.setForeground(Color.BLACK);
        infoTextArea.setEditable(false);
        infoTextArea.setFont(UIConstants.BODY_FONT);

        // ----- Add to topRow -----
        topRowLabels.add(waveCounterLabel);
        topRowLabels.add(createVerticalStrut(40));
        topRowLabels.add(gameplayTimerLabel);
        topRow.add(topRowLabels);
        topRow.add(createHorizontalStrut(40));
        topRow.add(pauseButton);

        // ----- Add to InfoPanel -----
        add(createVerticalStrut(20));
        add(topRow);
        add(createVerticalStrut(200));
        add(highscoreLabel);
        add(createVerticalStrut(20));
        add(scoreLabel);
        add(createVerticalStrut(20));
        validate();add(hpCounterPanel);
        add(createVerticalStrut(20));
        add(bombCounterPanel);
        add(createVerticalStrut(100));
        add(infoTextArea);
    }

    /**
     * Override the paintComponent method to render the game on the screen. This
     * is where custom rendering will occur.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (g instanceof Graphics2D g2d) {
            render(g2d);
        }
    }

    /**
     * Renders the screen's graphical components.
     */
    public void render(Graphics2D g2d) {
        if (backgroundImage != null && g2d != null) {
            g2d.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
        } else {
            System.err.println(String.format("%s: Could not load background image <'%s'>.", this.getClass().getName(), backgroundImageURL));
        }
    }

    //     score = 0;
    //     elapsedSeconds = 0;
    //     // Create the font used for the button and labels.
    //     Font buttonFont = new Font(GameFrame.BODY_TYPEFACE, Font.PLAIN, 16);
    //     // Create the pause button.
    //     pauseMenuButton = createButton(
    //             "PAUSE", buttonFont, 100, 40, true,
    //             GameManager.getInstance()::onPause
    //     );
    //     // Create the score and timer labels.
    //     scoreLabel = createStatusLabel();
    //     timerLabel = createStatusLabel();
    //     // Arrange components with horizontal spacing.
    //     add(Box.createHorizontalStrut(20));
    //     add(pauseMenuButton);
    //     add(Box.createHorizontalGlue());
    //     add(scoreLabel);
    //     add(Box.createHorizontalGlue());
    //     add(timerLabel);
    //     add(Box.createHorizontalStrut(20));
    //     // Initialise the score display.
    //     updateScoreDisplay(0);
    // }
    // /**
    //  * Updates the displayed score
    //  *
    //  * @param score The new score.
    //  */
    // public final void updateScoreDisplay(final int score) {
    //     scoreLabel.setText(String.format("Score: %d", score));
    // }
    // /**
    //  * Updates the timer label with a formatted elapsed time string.
    //  *
    //  * @param elapsedSeconds The elapsed time in seconds.
    //  */
    // public void updateTimerDisplay(final int elapsedSeconds) {
    //     int minutes = elapsedSeconds / 60;
    //     int seconds = elapsedSeconds % 60;
    //     timerLabel.setText(String.format("Elapsed Time: %d:%02d", minutes, seconds));
    // }
    // /**
    //  * Creates a JLabel for displaying status information (score or timer).
    //  *
    //  * Buggy at the moment
    //  *
    //  * @return A configured JLabel.
    //  */
    // private JLabel createStatusLabel() {
    //     JLabel label = new JLabel();
    //     label.setFont(new Font(GameFrame.BODY_TYPEFACE, Font.BOLD, 16));
    //     label.setForeground(Color.WHITE);
    //     // Will try to fix for the next assignment
    //     // label.setForeground(new Color(70, 0, 50));
    //     // label.setBorder(new RoundedBorder(Color.BLACK, Color.WHITE, 10));
    //     return label;
    // }
}
