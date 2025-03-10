package com.rikuthin.graphics.screens.subpanels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import com.rikuthin.graphics.GameFrame;
import com.rikuthin.graphics.ImageManager;

/**
 * A component that displays all the game entities
 */
public class GamePanel extends JPanel {

    private final String backgroundImageURL;
    private BufferedImage backgroundImage;

    public GamePanel() {
        // Background colour used as a backup in case the image deosn't load.
        setBackground(new Color(200, 170, 170));
        setPreferredSize(new Dimension(GameFrame.FRAME_HEIGHT, GameFrame.FRAME_HEIGHT));

        backgroundImageURL = "/images/backgrounds/game_panel.png";
        backgroundImage = ImageManager.loadBufferedImage(backgroundImageURL);
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
     * Updates the screen logic every frame.
     */
    // public abstract void update();
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

//     private static int NUM_ALIENS = 3;
//     private SoundManager soundManager;
//     private Bat bat;
//     private Alien[] aliens;
//     private boolean alienDropped;
//     private boolean isRunning;
//     private boolean isPaused;
//     private Thread gameThread;
//     private BufferedImage image;
//     //private Image backgroundImage;
//     private OldBackground background;
//     private ImageFX imageFX;
//     private ImageFX imageFX2;
//     private FaceAnimation animation;
//     private CatAnimation animation2;
//     private StripAnimation animation3;
//     public GamePanel() {
//         bat = null;
//         aliens = null;
//         alienDropped = false;
//         isRunning = false;
//         isPaused = false;
//         soundManager = SoundManager.getInstance();
//         //backgroundImage = ImageManager.loadImage ("images/Background.jpg");
//         image = new BufferedImage(400, 400, BufferedImage.TYPE_INT_RGB);
//     }
//     public void createGameEntities() {
// //		background = new Background(this, "images/Scroll-Background.png", 96);
// //		background = new Background(this, "images/PurpleNebula.png", 96);
//         background = new OldBackground(this, "images/VerticalBackground.png", 96);
//         bat = new Bat(this, 175, 350);
//         aliens = new Alien[3];
//         aliens[0] = new Alien(this, 275, 10, bat);
//         aliens[1] = new Alien(this, 150, 10, bat);
//         aliens[2] = new Alien(this, 330, 10, bat);
//         imageFX = new TintFX(this);
//         imageFX2 = new GrayScaleFX2(this);
//         animation = new FaceAnimation();
//         animation2 = new CatAnimation();
//         animation3 = new StripAnimation();
//     }
//     public void run() {
//         try {
//             isRunning = true;
//             while (isRunning) {
//                 if (!isPaused) {
//                     gameUpdate();
//                 }
//                 gameRender();
//                 Thread.sleep(50);
//             }
//         } catch (InterruptedException e) {
//         }
//     }
//     public void gameUpdate() {
//         /*
// 		for (int i=0; i<NUM_ALIENS; i++) {
// 			aliens[i].move();
// 		}
// 		imageFX.update();
// 		imageFX2.update();
// 		animation.update();
// 		animation2.update();
// 		animation3.update();
//          */
//     }
//     public void updateBat(int direction) {
//         if (isPaused) {
//             return;
//         }
//         if (background != null) {
//             background.move(direction);
//         }
//         if (bat != null) {
//             bat.move(direction);
//         }
//     }
//     public void gameRender() {
//         // draw the game objects on the image
//         Graphics2D imageContext = (Graphics2D) image.getGraphics();
//         background.draw(imageContext);
//         //imageContext.drawImage(backgroundImage, 0, 0, null);	// draw the background image
//         if (bat != null) {
//             bat.draw(imageContext);
//         }
//         /*
// 		if (aliens != null) {
// 			for (int i=0; i<NUM_ALIENS; i++)
// 				aliens[i].draw(imageContext);
//        		}
// 		if (imageFX != null) {
// 			imageFX.draw (imageContext);
// 		}
// 		if (imageFX2 != null) {
// 			imageFX2.draw (imageContext);
// 		}
// 		if (animation != null) {
// 			animation.draw (imageContext);
// 		}
// 		if (animation2 != null) {
// 			animation2.draw (imageContext);
// 		}
// 		if (animation3 != null) {
// 			animation3.draw (imageContext);
// 		}
//          */
//         Graphics2D g2 = (Graphics2D) getGraphics();	// get the graphics context for the panel
//         g2.drawImage(image, 0, 0, 400, 400, null);
//         imageContext.dispose();
//         g2.dispose();
//     }
//     public void startGame() {				// initialise and start the game thread 
//         if (gameThread == null) {
//             //soundManager.playClip ("background", true);
//             createGameEntities();
//             gameThread = new Thread(this);
//             gameThread.start();
//             if (animation != null) {
//                 animation.start();
//             }
//             if (animation2 != null) {
//                 animation2.start();
//             }
//             if (animation3 != null) {
//                 animation3.start();
//             }
//         }
//     }
//     public void startNewGame() {				// initialise and start a new game thread 
//         isPaused = false;
//         if (gameThread == null || !isRunning) {
//             //soundManager.playClip ("background", true);
//             createGameEntities();
//             gameThread = new Thread(this);
//             gameThread.start();
//             if (animation != null) {
//                 animation.start();
//             }
//             if (animation2 != null) {
//                 animation2.start();
//             }
//             if (animation3 != null) {
//                 animation3.start();
//             }
//         }
//     }
//     public void pauseGame() {				// pause the game (don't update game entities)
//         if (isRunning) {
//             if (isPaused) {
//                 isPaused = false; 
//             }else {
//                 isPaused = true;
//             }
//         }
//     }
//     public void endGame() {					// end the game thread
//         isRunning = false;
//         //soundManager.stopClip ("background");
//     }
//     public void shootCat() {
//         animation3.start();
//     }
//     public boolean isOnBat(int x, int y) {
//         return bat.isOnBat(x, y);
//     }
}
