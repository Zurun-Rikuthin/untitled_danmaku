package com.rikuthin.graphics.animations;

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
     * @param frameDurationMs Duration of each frame in milliseconds. (Minimum value: 1)
     * @param numRows The number of rows in the sprite sheet. (Minimum value: 1)
     * @param numColumns The number of columns in the sprite sheet. (Minimum value: 1)
     * @return List of AnimationFrame objects extracted from the sprite sheet.
     * @throws IOException If the image file cannot be loaded.
     */
    public static List<AnimationFrame> loadFromSpriteSheet(final String filePath, long frameDurationMs, int numRows, int numColumns) throws IllegalArgumentException, IOException {
        if (filePath == null || filePath.isEmpty()) {
            throw new IllegalArgumentException("AnimationLoader: Must provide the file path of the sprite sheet to be loaded.");
        }
        
        frameDurationMs = Math.max(frameDurationMs, 1);        
        numRows = Math.max(numRows, 1);
        numColumns = Math.max(numColumns, 1);

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
        SpriteSheetInfo[] spriteSheets = {
            new SpriteSheetInfo("enemy-bullet.png", 1, 8, true),
            new SpriteSheetInfo("mage-guardian-blue.png", 1, 14, true),
            new SpriteSheetInfo("mage-guardian-magenta.png", 1, 14, true),
            new SpriteSheetInfo("mage-guardian-red.png", 1, 14, true),
            new SpriteSheetInfo("player-bullet.png", 1, 8, true),
            new SpriteSheetInfo("player-death.png", 1, 8, false),
            new SpriteSheetInfo("player-idle.png", 1, 8, true),
            new SpriteSheetInfo("player-walk-up-left.png", 1, 8, true),
            new SpriteSheetInfo("player-walk-up-right.png", 1, 8, true),
            new SpriteSheetInfo("player-walk-up.png", 1, 8, true)
        };

        // Load each animation and add it to the AnimationManager
        for (SpriteSheetInfo sheet : spriteSheets) {
            try {
                // Load the frames from the sprite sheet
                List<AnimationFrame> frames = AnimationLoader.loadFromSpriteSheet(
                        ANIMATION_FOLDER + sheet.fileName, App.FRAME_RATE_MS, sheet.numGridRows, sheet.numGridColumns
                );

                // Create the animation template
                AnimationTemplate animationTemplate = new AnimationTemplate(frames, sheet.isLooping);

                // Generate a unique key for the animation from the sprite sheet filename (minus the extension, i.e., "bullet-1" instead of "bullet-1.png").
                String animationKey = sheet.fileName.substring(sheet.fileName.lastIndexOf('/') + 1, sheet.fileName.lastIndexOf('.'));

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

    // ----- PRIVATE INNER CLASSES -----
    /**
     * Represents metadata for a sprite sheet used in animations.
     */
    private static class SpriteSheetInfo {

        /**
         * File name of the sprite sheet.
         */
        String fileName;
        /**
         * Number of rows in the sprite sheet. (Minimum value of 1.)
         */
        int numGridRows;
        /**
         * Number of columns in the sprite sheet. (Minimum value of 1.)
         */
        int numGridColumns;
        /**
         * Whether the animation should loop.
         */
        boolean isLooping;

        /**
         * Creates a new SpriteSheetInfo object.
         *
         * @param fileName Name of the sprite sheet file.
         * @param numGridRows Number of rows in the sprite sheet's grid.
         * (Minimum value of 1.)
         * @param numGridColumns Number of columns in the sprite sheet's grid.
         * (Minimum value of 1.)
         * @param isLooping Whether the animation should loop.
         */
        public SpriteSheetInfo(final String fileName, final int numGridRows, final int numGridColumns, final boolean isLooping) throws IllegalArgumentException {
            if (fileName == null || fileName.isBlank()) {
                throw new IllegalArgumentException(String.format(
                        "%s: File namme cannot be null nor blank.",
                        this.getClass().getName()
                ));
            }
            this.fileName = fileName;
            this.numGridRows = Math.max(numGridRows, 1);
            this.numGridColumns = Math.max(numGridColumns, 1);
            this.isLooping = isLooping;
        }
    }

}
