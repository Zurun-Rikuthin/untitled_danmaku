package com.rikuthin.animations;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.rikuthin.graphics.ImageManager;
import com.rikuthin.interfaces.Renderable;
import com.rikuthin.interfaces.Updateable;

/**
 * The Animation class manages a series of images (frames) and the duration for
 * which each frame should be displayed. It supports looping and can be started,
 * stopped, and updated dynamically.
 */
public class Animation implements Updateable, Renderable {

    // ----- INSTANCE VARIABLES -----
    /**
     * collection of frames in the animation.
     */
    private final ArrayList<AnimationFrame> frames;

    /**
     * The x-coordinate the animation is rendered at.
     */
    private int x;
    /**
     * The y-coordinate the animation is rendered at.
     */
    private int y;
    /**
     * Indicates if the animation should loop.
     */
    private final boolean isLooping;

    /**
     * Index of the current frame being displayed.
     */
    private int currentFrameIndex;
    /**
     * Time elapsed since the current frame was first displayed.
     */
    private long elapsedFrameDisplayTime;
    /**
     * Last recorded update time.
     */
    private long lastUpdateTime;
    /**
     * Indicates if the animation is currently playing.
     */
    private boolean isPlaying;

    // ------ CONSTRUCTORS -----
    /**
     * Creates a new, empty Animation.
     *
     * @param x The x-coordinate to render the animation at.
     * @param y The y-coordinate to render the animation at.
     * @param isLooping Whether the animation should loop.
     */
    public Animation(final int x, final int y, final boolean isLooping) {
        this.frames = new ArrayList<>();
        this.currentFrameIndex = 0;
        this.elapsedFrameDisplayTime = 0;
        this.lastUpdateTime = 0;
        this.isLooping = isLooping;
        this.isPlaying = false;
    }

    // ------ GETTERS ------
    /**
     * The x-coordinate the animation is rendered at.
     *
     * @return The x-coordinate.
     */
    public int getX() {
        return x;
    }

    /**
     * The y-coordinate the animation is rendered at.
     *
     * @return The y-coordinate.
     */
    public int getY() {
        return y;
    }

    /**
     * Returns the current frame's image.
     *
     * @return The image of the current frame, or null if there are no frames.
     */
    public BufferedImage getCurrentFrameImage() {
        return frames.isEmpty() ? null : getFrame(currentFrameIndex).image;
    }

    /**
     * Returns the total number of frames in the animation.
     *
     * @return The number of frames.
     */
    public int getFrameCount() {
        return frames.size();
    }

    private AnimationFrame getFrame(int i) {				// returns ith frame in the collection
        return frames.get(i);
    }

    /**
     * Checks whether the animation is currently playing.
     *
     * @return true if the animation is active, false otherwise.
     */
    public boolean isPlaying() {
        return isPlaying;
    }

    // ----- SETTERS -----
    public void setPosition(final int x, final int y) {
        this.x = x;
        this.y = y;
    }

    // ----- OVERRIDDEN METHODS -----
    /**
     * Updates the animation's frame based on elapsed time.
     */
    @Override
    public void update() {
        if (!isPlaying || frames.isEmpty()) {
            return;
        }

        long currentTime = System.currentTimeMillis();
        elapsedFrameDisplayTime += currentTime - lastUpdateTime;
        lastUpdateTime = currentTime;

        // If the elapsed time exceeds the duration of the current frame, move to the next frame
        while (elapsedFrameDisplayTime >= frames.get(currentFrameIndex).frameDurationMs) {
            elapsedFrameDisplayTime = 0;
            nextFrame();
        }
    }

    @Override
    public void render(Graphics2D g2d) {
        BufferedImage currentImage = frames.get(currentFrameIndex).image;
        if (currentImage != null) {
            g2d.drawImage(currentImage, x, y, currentImage.getWidth(), currentImage.getHeight(), null);
        }
    }

    // ----- BUSINESS LOGIC METHODS ------
    /**
     * Loads animation frames from a given strip file.
     * <p>
     * Note: A valid strip file has no margins (i.e., no space between the
     * frames themselves and the outer borders of the image itself).
     *
     * @param fileUrl The URL of the strip file.
     * @param frameDurationMs How many milliseconds to display each frame.
     * @param numRows The number of rows in the strip file.
     * @param numColumns The number of columns in the strip file.
     * @param horizontalSpace The number of pixels between frame columns in the
     * strip file.
     * @param verticalSpace The number of pixels between frame rows in the strip
     * file.
     */
    public void loadStripFile(final String fileUrl, final long frameDurationMs, final int numRows, final int numColumns, final int horizontalSpace, final int verticalSpace) {
        BufferedImage stripImage = ImageManager.loadBufferedImage(fileUrl);
        int frameWidth = (stripImage.getWidth() - (numColumns - 1) * horizontalSpace) / numColumns;
        int frameHeight = (stripImage.getHeight() - (numRows - 1) * verticalSpace) / numRows;

        for (int row = 0; row < numRows; row++) {
            for (int column = 0; column < numColumns; column++) {
                int leftX = column * (frameWidth + horizontalSpace);
                int upperY = row * (frameHeight + verticalSpace);

                BufferedImage croppedImage = stripImage.getSubimage(leftX, upperY, frameWidth, frameHeight);

                // Create a new copy of the cropped image to preserve the original's memory integrity
                BufferedImage frameImage = new BufferedImage(frameWidth, frameHeight, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g = frameImage.createGraphics();
                g.drawImage(croppedImage, 0, 0, null);
                g.dispose();

                addFrame(frameImage, frameDurationMs);
            }
        }
    }

    /**
     * Adds a new frame to the animation using the provided URL.
     *
     * @param imageUrl The URL of the new frame's image.
     * @param durationMs How long to display the new frame (in milliseconds).
     */
    public void addFrame(final String imageUrl, long durationMs) {
        frames.add(new AnimationFrame(imageUrl, durationMs));
    }

    /**
     * Adds an image to the animation using a pre-loaded {@link BufferedImage}.
     *
     * @param image The pre-loaded image.
     * @param durationMs How long to display the new frame (in milliseconds).
     */
    public void addFrame(final BufferedImage image, long durationMs) {
        frames.add(new AnimationFrame(image, durationMs));
    }

    /**
     * Starts this animation over from the beginning.
     */
    public void start() {
        isPlaying = true;
        currentFrameIndex = 0;
        elapsedFrameDisplayTime = 0;
        lastUpdateTime = System.currentTimeMillis();
    }

    /**
     * Terminates this animation.
     */
    public void stop() {
        isPlaying = false;
    }

    // ----- HELPER METHODS -----
    /**
     * Advances to the next frame of the animation.
     *
     * If the animation reaches the last frame, it either loops back to the
     * first frame (if looping is enabled) or stops. The frame index is updated
     * accordingly.
     */
    private void nextFrame() {
        if (currentFrameIndex >= frames.size() - 1) {
            if (isLooping) {
                currentFrameIndex = 0;
            } else {
                stop();
            }
        } else {
            currentFrameIndex++;
        }
    }

    // ----- PRIVATE INNER CLASSES -----
    /**
     * Represents a single frame in the animation
     */
    private static class AnimationFrame {

        // ----- INSTANCE VARIABLES -----
        /**
         * The image displayed by the frame.
         */
        final BufferedImage image;
        /**
         * How many milliseconds to display the frame for.
         */
        final long frameDurationMs;

        // ----- CONSTRUCTORS -----
        /**
         * Constructs a new animation frame using the given URL.
         *
         * @param imageUrl The URL of the displayed image.
         * @param frameDurationMs How many milliseconds to display the frame
         * for.
         */
        public AnimationFrame(final String imageUrl, long frameDurationMs) {
            this.image = ImageManager.loadBufferedImage(imageUrl);
            this.frameDurationMs = frameDurationMs;
        }

        /**
         * Constructs a new animation frame using a pre-loaded
         * {@link BufferedImage}.
         *
         * @param imageUrl The URL of the displayed image.
         * @param frameDurationMs How many milliseconds to display the frame
         * for.
         */
        public AnimationFrame(final BufferedImage image, long frameDurationMs) {
            this.image = image;
            this.frameDurationMs = frameDurationMs;
        }
    }
}
