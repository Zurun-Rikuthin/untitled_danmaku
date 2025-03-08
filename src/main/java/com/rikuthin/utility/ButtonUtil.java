package com.rikuthin.utility;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JToggleButton;

/**
 * Utility class for creating various buttons in the UI.
 */
public class ButtonUtil {

    private ButtonUtil() {
        // Private constructor to prevent instantiation.
    }

    /**
     * Creates a regular JButton with the specified properties.
     *
     * @param text The text displayed on the button.
     * @param font The font to be used on the button.
     * @param buttonWidth The width of the button.
     * @param buttonHeight The height of the button.
     * @param enabled The initial enabled state of the button.
     * @param actionListener The ActionListener to be added to the button.
     * @return A configured JButton.
     */
    public static JButton createButton(final String text, final Font font, final int buttonWidth, final int buttonHeight, boolean enabled, final ActionListener actionListener) {
        return createGenericButton(new JButton(text), font, buttonWidth, buttonHeight, enabled, actionListener);
    }

    /**
     * Creates a JToggleButton with the specified properties.
     *
     * @param text The text displayed on the button.
     * @param font The font to be used on the button.
     * @param buttonWidth The width of the button.
     * @param buttonHeight The height of the button.
     * @param enabled The initial enabled state of the button.
     * @param actionListener The ActionListener to be added to the button.
     * @return A configured JToggleButton.
     */
    public static JToggleButton createToggleButton(final String text, final Font font, final int buttonWidth, final int buttonHeight, boolean enabled, final ActionListener actionListener) {
        return createGenericButton(new JToggleButton(text), font, buttonWidth, buttonHeight, enabled, actionListener);
    }

    /**
     * Creates a generic button with the specified properties. Used internally
     * for button creation.
     *
     * @param button The type of button to be created (JButton or
     * JToggleButton).
     * @param font The font to be used on the button.
     * @param buttonWidth The width of the button.
     * @param buttonHeight The height of the button.
     * @param enabled The initial enabled state of the button.
     * @param actionListener The ActionListener to be added to the button.
     * @return A configured button (JButton or JToggleButton).
     */
    private static <T extends AbstractButton> T createGenericButton(T button, Font font, int buttonWidth, int buttonHeight, boolean enabled, ActionListener actionListener) {
        if (font == null || actionListener == null) {
            throw new IllegalArgumentException("Font and actionListener must not be null");
        }

        // Validate dimensions
        if (buttonWidth <= 0 || buttonHeight <= 0) {
            throw new IllegalArgumentException("Button width and height must be positive values.");
        }

        Dimension buttonSize = new Dimension(buttonWidth, buttonHeight);

        button.setFont(font);
        button.setPreferredSize(buttonSize);
        button.setMinimumSize(buttonSize);
        button.setMaximumSize(buttonSize);
        button.setEnabled(enabled);
        button.addActionListener(actionListener);

        return button;
    }
}
