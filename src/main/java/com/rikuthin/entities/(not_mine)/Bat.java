// package com.rikuthin.entities;

// import java.awt.Graphics2D;
// import java.awt.Image;
// import java.awt.geom.Rectangle2D;

// import javax.swing.JPanel;

// import com.rikuthin.graphics.ImageManager;

// public class Bat {

//     private JPanel panel;
//     private int x;
//     private int y;
//     private int width;
//     private int height;

//     private int dx;
//     private int dy;

//     private Image batImage;
//     private Image batLeftImage;
//     private Image batRightImage;

//     public Bat(JPanel p, int xPos, int yPos) {
//         panel = p;

//         /*
// 		x = xPos;
// 		y = yPos;
//          */
//         x = 188;
//         y = 178;

//         dx = 0;				// set to zero since background moves instead
//         dy = 0;				// size of vertical movement

//         width = 24;
//         height = 44;

//         batLeftImage = ImageManager.loadImage("images/BatLeft.gif");
//         batRightImage = ImageManager.loadImage("images/BatRight.gif");

//         batImage = batRightImage;
//     }

//     public void draw(Graphics2D g2) {

//         g2.drawImage(batImage, x, y, width, height, null);

//     }

//     public void move(int direction) {

//         if (!panel.isVisible()) {
//             return;
//         }

//         if (direction == 1) {
//             x = x - dx;			// move left
//             batImage = batLeftImage;
//         } else if (direction == 2) {
//             x = x + dx;			// move right
//             batImage = batRightImage;
//         } else if (direction == 3) {
//             y = y - dy;			// move up
//             if (y < 0) {
//                 y = 0;
//             }
//         } else if (direction == 4) {
//             y = y + dy;			// move down
//             if (y > 350) {
//                 y = 350;
//             }
//         }
//     }


//     /*
// 	public void move (int direction) {

// 		if (!panel.isVisible ()) return;
      
// 		if (direction == 1) {
// 			x = x - dx;
// 	                batImage = batLeftImage;          
// 			if (x < -30)			// move to right of GamePanel
// 				x = 380;
// 		}
// 		else 
// 		if (direction == 2) {
// 			x = x + dx;
//           	  	batImage = batRightImage;
// 			if (x > 380)			// move to left of GamePanel
// 				x = -30;
// 		}
// 	}
//      */
//     public boolean isOnBat(int x, int y) {
//         Rectangle2D.Double myRectangle = getBoundingRectangle();
//         return myRectangle.contains(x, y);
//     }

//     public Rectangle2D.Double getBoundingRectangle() {
//         return new Rectangle2D.Double(x, y, width, height);
//     }

// }
