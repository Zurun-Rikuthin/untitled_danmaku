// package com.rikuthin.entities;

// import java.awt.Dimension;
// import java.awt.Graphics2D;
// import java.awt.Image;
// import java.awt.geom.Rectangle2D;
// import java.util.Random;

// import javax.swing.JPanel;

// import com.rikuthin.audio.SoundManager;
// import com.rikuthin.graphics.ImageManager;

// public class Alien {

//     private final JPanel panel;

//     private int x;
//     private int y;

//     private final int width;
//     private final int height;


//     private final int dx;		// increment to move along x-axis
//     private int dy;		// increment to move along y-axis
//     private final Dimension dimension;

//     private final Random random;

//     private final Bat bat;
//     private final SoundManager soundManager;
//     private final Image alienImage;

//     public Alien(JPanel p, int xPos, int yPos, Bat bat) {
//         panel = p;
//         dimension = panel.getSize();
//         panel.getBackground();

//         width = 50;
//         height = 45;

//         random = new Random();

//         x = xPos;
//         y = yPos;

//         setLocation();

//         dx = 0;			// no movement along x-axis
//         dy = 5;			// would like the alien to drop down

//         this.bat = bat;
//         alienImage = ImageManager.loadImage("images/Alien2.png");
//         soundManager = SoundManager.getInstance();
//     }

//     public void setLocation() {
//         int panelWidth = panel.getWidth();
//         x = random.nextInt(panelWidth - width);
//         y = 10;
//     }

//     public void draw(Graphics2D g2) {

//         g2.drawImage(alienImage, x, y, width, height, null);

//     }

//     public void move() {

//         if (!panel.isVisible()) {
//             return;
//         }

//         x = x + dx;
//         y = y + dy;

//         int height = panel.getHeight();
//         boolean collision = collidesWithBat();

//         if (collision) {
//             soundManager.playClip("hit", false);
//         }

//         if (collision) {
//             setLocation();
//         }

//         if (y > height) {
//             setLocation();
//             soundManager.playClip("appear", false);
//             dy = dy + 1;			// speed up alien when it is re-generated at top
//         }

//     }


//     /*
//    public boolean isOnAlien (int x, int y) {
//       if (head == null)
//       	  return false;

//       return head.contains(x, y);
//    }
//      */
//     public Rectangle2D.Double getBoundingRectangle() {
//         return new Rectangle2D.Double(x, y, width, height);
//     }

//     public boolean collidesWithBat() {
//         Rectangle2D.Double myRect = getBoundingRectangle();
//         Rectangle2D.Double batRect = bat.getBoundingRectangle();

//         return myRect.intersects(batRect);
//     }

// }
