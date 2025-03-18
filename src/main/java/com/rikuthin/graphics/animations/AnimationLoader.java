package com.rikuthin.graphics.animations;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.rikuthin.core.App;
import com.rikuthin.graphics.ImageManager;

/**
 * Utility class for loading animations from sprite sheets (strip files).
 */
public class AnimationLoader {

    // ----- STATIC VARIABLES -----
    /**
     * Directory where animations are stored
     */
    private static final String ANIMATION_FOLDER = "/images/animations/";

    // ----- CONSTRUCTORS -----
    /**
     * Private constructor to prevent instantiation.
     */
    private AnimationLoader() {
    }

    // ----- BUSINESS LOGIC METHODS -----
    /**
     * Loads an animation from a sprite strip file.
     * <p>
     * Valid sprite sheets expect each frame to be the same size and have no
     * space in-between them or along the sheet's margins.
     *
     * @param filePath Path to the sprite sheet image.
     * @param numRows The number of rows in the sprite sheet.
     * @param numColumns The number of columns in the sprite sheet.
     * @param frameDurationMs Duration of each frame in milliseconds.
     * @return List of AnimationFrame objects extracted from the sprite sheet.
     * @throws IOException If the image file cannot be loaded.
     */
    public static List<AnimationFrame> loadFromSpriteSheet(String filePath, long frameDurationMs, int numRows, int numColumns) throws IllegalArgumentException, IOException {
        if (filePath == null || filePath.isEmpty()) {
            throw new IllegalArgumentException("AnimationLoader: Must provide the file path of the sprite sheet to be loaded.");
        }
        if (frameDurationMs <= 0) {
            throw new IllegalArgumentException("AnimationLoader: Frame duration must be greater than zero (0) milliseconds.");
        }
        if (numRows <= 0 || numColumns <= 0) {
            throw new IllegalArgumentException("AnimationLoader: Row and column counts must both be greater than zero (0).");
        }

        BufferedImage spriteSheet = ImageManager.loadBufferedImage(filePath);
        if (spriteSheet == null) {
            throw new IOException("AnimationLoader: Failed to load sprite sheet: " + filePath);
        }

        ArrayList<AnimationFrame> frames = new ArrayList<>();
        int frameWidth = spriteSheet.getWidth() / numColumns;
        int frameHeight = spriteSheet.getHeight() / numRows;

        for (int row = 0; row < numRows; row++) {
            for (int column = 0; column < numColumns; column++) {
                int x = column * frameWidth;
                int y = row * frameHeight;

                BufferedImage frame = extractFrame(spriteSheet, x, y, frameWidth, frameHeight);
                frames.add(new AnimationFrame(frame, frameDurationMs));
            }
        }
        return frames;
    }

    /**
     * Loads multiple animations at startup.
     * <p>
     * Hard-coding this for now. Might try to make it more dynamic later.
     */
    public static void loadAllAnimations() {
        String[] spriteSheetPaths = {
            ANIMATION_FOLDER + "player-bullet-1.png",
            ANIMATION_FOLDER + "enemy-bullet-1.png",
            ANIMATION_FOLDER + "player.png",
            ANIMATION_FOLDER + "Agis.png"
        };

        // "width" = column count, "height" = row count
        Dimension[] spriteSheetDimensions = {
            new Dimension(8, 1),
            new Dimension(8, 1),
            new Dimension(1, 1),
            new Dimension(15, 1)
        };

        boolean[] looping = {true, true, true, true, true}; // Looping flag for each animation

        // Load each animation and add it to the AnimationManager
        for (int i = 0; i < spriteSheetPaths.length; i++) {
            String spriteSheetPath = spriteSheetPaths[i];

            try {
                // Load the frames from the sprite sheet
                List<AnimationFrame> frames = AnimationLoader.loadFromSpriteSheet(
                        spriteSheetPath, App.FRAME_RATE_MS, spriteSheetDimensions[i].height, spriteSheetDimensions[i].width
                );

                // Create the animation template
                AnimationTemplate animationTemplate = new AnimationTemplate(frames, looping[i]);

                // Generate a unique key for the animation from the sprite sheet filename (minus the extension, i.e., "bullet-1" instead of "bullet-1.png").
                String animationKey = spriteSheetPath.substring(spriteSheetPath.lastIndexOf('/') + 1, spriteSheetPath.lastIndexOf('.'));

                // Add the animation template to the AnimationManager
                AnimationManager.getInstance().addAnimation(animationKey, animationTemplate);

                System.out.println(String.format(
                        "AnimationLoader: Loaded animation <'%s> with <%d> frames.",
                        animationKey,
                        animationTemplate.getFrames().size()
                ));
            } catch (IOException | IllegalArgumentException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    // ----- HELPER METHODS -----
    /**
     * Extracts a frame from a sprite sheet.
     *
     * @param source The source sprite sheet.
     * @param x X coordinate of the frame.
     * @param y Y coordinate of the frame.
     * @param width Width of the frame.
     * @param height Height of the frame.
     * @return A new BufferedImage containing the extracted frame.
     */
    private static BufferedImage extractFrame(BufferedImage source, int x, int y, int width, int height) {
        BufferedImage frame = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = frame.createGraphics();
        g.drawImage(source.getSubimage(x, y, width, height), 0, 0, null);
        g.dispose();
        return frame;
    }
}
