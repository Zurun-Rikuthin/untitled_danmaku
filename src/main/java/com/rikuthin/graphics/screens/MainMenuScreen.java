package com.rikuthin.graphics.screens;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.rikuthin.graphics.GameFrame;
import com.rikuthin.graphics.ImageManager;
import com.rikuthin.graphics.UIConstants;
import static com.rikuthin.utility.ButtonUtil.createButton;

/**
 * Main menu screen of the game. Provides options to start a new game, view
 * instructions, adjust settings, see high scores, and quit the game.
 */
public class MainMenuScreen extends Screen {

    private final JLabel titleLabel;
    private final JPanel buttonPanel;
    private final JPanel centreWrapper;
    private final JPanel titlePanel;
    private final String backgroundImageURL;
    private BufferedImage backgroundImage;

    // TODO: Implement mute functionality
    // private final JToggleButton muteMusicButton;
    /**
     * Constructs the main menu screen panel with buttons for starting the game,
     * viewing how to play, settings, high scores, and quitting.
     *
     * @param gameFrame The parent {@link GameFrame} to which this screen
     * belongs.
     */
    public MainMenuScreen(GameFrame gameFrame) {
        super(gameFrame);
        setBackground(new Color(87, 73, 100));
        setLayout(new BorderLayout());

        System.out.println("before loading image");
        backgroundImageURL = "/images/backgrounds/main_menu.png";
        backgroundImage = ImageManager.loadBufferedImage(backgroundImageURL);
        System.out.println("after loading image" + backgroundImage);

        // ----- Title Section (Centered at the top) -----
        titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setOpaque(false);

        titleLabel = new JLabel(gameFrame.getTitle(), SwingConstants.CENTER);
        titleLabel.setFont(UIConstants.TITLE_FONT);
        titleLabel.setForeground(Color.WHITE);

        titlePanel.add(Box.createVerticalStrut(100));  // Add space above the title
        titlePanel.add(titleLabel);
        titlePanel.add(Box.createVerticalStrut(100));  // Add space below the title

        add(titlePanel, BorderLayout.NORTH);

        // ----- Button Section -----
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setOpaque(false);

        buttonPanel.add(Box.createVerticalStrut(100));  // Add space above buttons

        // Button labels and their corresponding actions
        final String[] labels = {"START GAME", "HOW TO PLAY", "SETTINGS", "HIGHSCORES", "QUIT GAME"};
        final ActionListener[] actions = {this::onStartGame, this::onHowToPlay, this::onSettings, this::onHighscores, this::onQuitGame};

        // Create and add buttons
        for (int i = 0; i < labels.length; i++) {
            final boolean enabled = i == 0 || i == 4; // Only enable "START GAME" and "QUIT GAME" for now
            JButton button = createButton(labels[i], UIConstants.BUTTON_FONT, 180, 40, enabled, actions[i]);
            buttonPanel.add(button);
            buttonPanel.add(Box.createVerticalStrut(10));  // Space between buttons
        }

        // Center the button panel
        centreWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        centreWrapper.setOpaque(false);
        centreWrapper.add(buttonPanel);

        add(centreWrapper, BorderLayout.CENTER);

        // TODO: Implement Mute Music Button
        // muteButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        // muteButtonPanel.setOpaque(false);
        // muteMusicButton = createToggleButton("MUTE", buttonFont, 90, 40, false, this::onMuteMusic);
        // muteButtonPanel.add(muteMusicButton);
        // add(muteButtonPanel, BorderLayout.NORTH);  // Add to the left side
    }

    @Override
    public void update() {
        // Not needed right now (no animations, button effects, etc.)
    }

    @Override
    public void render(Graphics2D g2d) {
        if (backgroundImage != null && g2d != null) {
            g2d.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
        } else {
            System.err.println(String.format("%s: Could not load background image <'%s'>.", this.getClass().getName(), backgroundImageURL));
        }
    }

    // TODO: Implement mute functionality
    // private void onMuteMusic(ActionEvent e) {
    //     System.out.println("Mute functionality is not yet implemented.");
    // }
    /**
     * Starts the game by switching to the gameplay panel and initializing the
     * game.
     *
     * @param e The action event triggered by the button.
     */
    private void onStartGame(ActionEvent e) {
        gameFrame.setScreen(new GameplayScreen(gameFrame));
    }

    // TODO: Implement the functionality for viewing how to play
    private void onHowToPlay(ActionEvent e) {
        System.out.println("How to Play screen is not implemented yet.");
    }

    // TODO: Implement the functionality for the settings menu
    private void onSettings(ActionEvent e) {
        System.out.println("Settings menu is not implemented yet.");
    }

    // TODO: Implement the functionality for high scores
    private void onHighscores(ActionEvent e) {
        System.out.println("High scores screen is not implemented yet.");
    }

    /**
     * Exits the game when the quit button is pressed.
     *
     * @param e The action event triggered by the button.
     */
    private void onQuitGame(ActionEvent e) {
        System.exit(0);
    }

}
