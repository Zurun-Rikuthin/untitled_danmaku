package com.rikuthin.graphics.dialogue;

import java.awt.Component;
import java.awt.Dialog;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;

import com.rikuthin.graphics.GameFrame;
import static com.rikuthin.utility.ButtonUtil.createButton;

public class PauseMenuDialogue extends Dialog {

    private final JLabel menuLabel;
    private final JButton resumeButton;
    private final JButton settingsButton;
    private final JButton mainMenuButton;
    private final JButton quitGameButton;

    /**
     * Constructs a new PauseMenuDialogue.
     *
     * @param gameFrame The game frame, used for positioning the dialog relative
     * to it.
     * @param onResumeCallback A callback to resume the game when the "Resume"
     * button is clicked.
     */
    public PauseMenuDialogue(GameFrame gameFrame, Runnable onResumeCallback) {
        super(gameFrame, "Pause Menu", Dialog.ModalityType.APPLICATION_MODAL);
        setSize(250, 300); // Adjusted size for better readability
        setLocationRelativeTo(gameFrame);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        Font menuFont = new Font(GameFrame.BODY_TYPEFACE, Font.PLAIN, 16); // Adjusted font size for better readability

        menuLabel = new JLabel("PAUSE");
        menuLabel.setFont(menuFont);
        menuLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Initialize buttons with a callback for their respective actions
        resumeButton = createButton("RESUME", menuFont, 200, 40, true, e -> onResume(onResumeCallback));
        resumeButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        settingsButton = createButton("SETTINGS", menuFont, 200, 40, false, this::onSettings);
        settingsButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        mainMenuButton = createButton("MAIN MENU", menuFont, 200, 40, false, this::onMainMenu);
        mainMenuButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        quitGameButton = createButton("QUIT GAME", menuFont, 200, 40, true, this::onQuitGame);
        quitGameButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Add spacing between components
        add(Box.createVerticalStrut(20));
        add(menuLabel);
        add(Box.createVerticalStrut(20));
        add(resumeButton);
        add(Box.createVerticalStrut(10));
        add(settingsButton);
        add(Box.createVerticalStrut(10));
        add(mainMenuButton);
        add(Box.createVerticalStrut(10));
        add(quitGameButton);
        add(Box.createVerticalStrut(20));

        // Close dialog and resume the game if the window is closed directly
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                onResume(onResumeCallback);  // Ensure callback is called even if dialog is closed
                dispose();
            }
        });
    }

    /**
     * Invoked when the "Resume" button is clicked. It executes the callback and
     * closes the dialog.
     *
     * @param onResumeCallback The callback to resume the game.
     */
    private void onResume(Runnable onResumeCallback) {
        onResumeCallback.run();
        dispose(); // Close the dialog after resuming the game
    }

    /**
     * Invoked when the "Settings" button is clicked. For now, it prints a
     * message to the console.
     */
    private void onSettings(ActionEvent e) {
        System.out.println("Settings button clicked.");
        // Future functionality can be implemented here.
    }

    /**
     * Invoked when the "Main Menu" button is clicked. For now, it prints a
     * message to the console.
     */
    private void onMainMenu(ActionEvent e) {
        System.out.println("Main Menu button clicked.");
        // Future functionality can be implemented here.
    }

    /**
     * Invoked when the "Quit Game" button is clicked. Exits the application.
     */
    private void onQuitGame(ActionEvent e) {
        System.exit(0);
    }
}
