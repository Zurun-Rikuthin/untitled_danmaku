// package com.rikuthin.screen_panels.gameplay_subpanels;

// import java.awt.BorderLayout;
// import java.awt.Color;
// import java.awt.Dimension;
// import java.awt.Font;
// import java.awt.Graphics;
// import java.awt.Graphics2D;

// import javax.swing.BoxLayout;
// import javax.swing.JLabel;
// import javax.swing.JPanel;

// import com.rikuthin.GameFrame;
// import com.rikuthin.game_objects.Blaster;

// /**
//  * The BlasterPanel is responsible for displaying the blaster and updating the
//  * remaining bubbles counter on the gameplay screen.
//  * <p>
//  * Note: Additional panels (e.g. {@code remainingBubblesPanel} and
//  * {@code heldBubblesPanel}) are initialised for potential future use.
//  * </p>
//  */
// public class BlasterPanel extends JPanel {

//     private final JLabel remainingBubblesCounterLabel;
//     private final JPanel remainingBubblesPanel;
//     private final Blaster blaster;

//     /**
//      * Constructs a BlasterPanel
//      */
//     public BlasterPanel() {
//         setBackground(new Color(159, 131, 131));
//         setPreferredSize(new Dimension(GameFrame.FRAME_WIDTH, 110));
//         setLayout(new BorderLayout());

//         // Initialise placeholder panels for future enhancements.
//         remainingBubblesPanel = new JPanel();
//         remainingBubblesPanel.setLayout(new BoxLayout(remainingBubblesPanel, BoxLayout.Y_AXIS));

//         // Initialise and add the remaining bubbles counter label.
//         remainingBubblesCounterLabel = new JLabel();
//         remainingBubblesCounterLabel.setFont(new Font(GameFrame.BODY_TYPEFACE, Font.BOLD, 16));
//         remainingBubblesCounterLabel.setForeground(Color.WHITE);
//         add(remainingBubblesCounterLabel, BorderLayout.WEST);

//         // Configure the blaster.
//         final int shotSize = 30;
//         final double shotSpeed = 15;
//         final int blasterX = (GameFrame.FRAME_WIDTH / 2) - (shotSize / 2);
//         final int blasterY = 0;
//         blaster = new Blaster(blasterX, blasterY, shotSize, shotSpeed, new Color(2, 52, 54));
//     }

//     /**
//      * Returns the Blaster associated with this panel.
//      *
//      * @return The Blaster object.
//      */
//     public Blaster getBlaster() {
//         return blaster;
//     }

//     /**
//      * Updates the remaining bubbles counter displayed on this panel.
//      *
//      * @param value The new remaining bubbles count.
//      */
//     public void updateRemainingBubblesCounter(final int value) {
//         remainingBubblesCounterLabel.setText(String.format("Remaining Bubbles: %d", value));
//     }

//     /**
//      * Paints the blaster onto the panel.
//      *
//      * @param g The Graphics context.
//      */
//     @Override
//     protected void paintComponent(Graphics g) {
//         super.paintComponent(g);
//         Graphics2D g2 = (Graphics2D) g;
//         blaster.draw(g2);
//     }
// }
