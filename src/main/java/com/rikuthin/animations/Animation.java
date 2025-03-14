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
    public void setPosition (final int x, final int y) {
        this.x = x; 
        this.y = y;
    }

    // ----- OVERRIDDEN METHODS -----
    /**
     * Updates the animation's frame based on elapsed time.
     */
    @Override
    public void update() {
        if (!isPlaying || !frames.isEmpty()) {
            return;
        }

        if (elapsedFrameDisplayTime >= frames.get(currentFrameIndex).frameDurationMs) {
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
     * Adds an image to the animation with the specified duration (time to
     * display the image).
     *
     * @param imageUrl The URL of the new frame's image.
     * @param durationMs How long to display the new frame (in milliseconds).
     */
    public void addFrame(final String imageUrl, long durationMs) {
        frames.add(new AnimationFrame(imageUrl, durationMs));
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
     * This method increments the current frame index and resets the elapsed
     * frame display time. If the current frame index reaches the last frame and
     * looping is enabled, the animation restarts from the first frame. If
     * looping is not enabled and the last frame is reached, the animation is
     * stopped.
     */
    private void nextFrame() {
        long currentTime = System.currentTimeMillis();
        elapsedFrameDisplayTime += currentTime - lastUpdateTime;
        lastUpdateTime = currentTime;

        // Check if the frame index points to/beyond the final frame
        if (currentFrameIndex >= frames.size() - 1) {
            if (isLooping) { // If looping is enabled, reset to the first frame
                currentFrameIndex = 0;

            } else { // Otherwise, stop the animation
                stop();
            }

        } else { // Otherwise proceed to the next frame and reset the display time counter
            currentFrameIndex++;
            elapsedFrameDisplayTime = 0;
        }
    }

    // ----- PRIVATE INNER CLASSES -----
    /**
     * Represents a single frame in the animation
     */
    private static class AnimationFrame {

        /**
         * The image displayed by the frame.
         */
        final BufferedImage image;
        /**
         * How many milliseconds to display the frame for.
         */
        final long frameDurationMs;

        /**
         * Constructs a new animation frame.
         *
         * @param imageUrl The URL of the displayed image.
         * @param frameDurationMs How many milliseconds to display the frame
         * for.
         */
        public AnimationFrame(final String imageUrl, long frameDurationMs) {
            this.image = ImageManager.loadBufferedImage(imageUrl);
            this.frameDurationMs = frameDurationMs;
        }
    }

}
