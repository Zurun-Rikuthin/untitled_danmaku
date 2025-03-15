package com.rikuthin.graphics;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

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
     * The coordinates the animation is rendered at.
     */
    private Point position;
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
     * Indicates if the animation is currently playing. Defaults to
     * {@code false}.
     */
    private boolean isPlaying;

    // ------ CONSTRUCTORS -----
    /**
     * Creates a new, empty Animation.
     *
     * @param position The coordinates to render the animation at.
     * @param isLooping Whether the animation should loop.
     */
    public Animation(final Point position, final boolean isLooping) {
        this.position = position;
        this.isLooping = isLooping;
        this.frames = new ArrayList<>();
        this.currentFrameIndex = 0;
        this.elapsedFrameDisplayTime = 0;
        this.lastUpdateTime = System.currentTimeMillis();
        this.isPlaying = false;
    }

    // ------ GETTERS ------
    /**
     * The x-coordinate the animation is rendered at.
     *
     * @return The x-coordinate.
     */
    public int getX() {
        return position.x;
    }

    /**
     * The y-coordinate the animation is rendered at.
     *
     * @return The y-coordinate.
     */
    public int getY() {
        return position.y;
    }

    /**
     * The coordinates the animation is rendered at.
     *
     * @return The coordinates.
     */
    public Point getPosition() {
        return position;
    }

    /**
     * Returns whether the animation has any frames.
     *
     * @return {@code true} if there are frames; {@code false} otherwise.
     */
    public boolean isEmpty() {
        return frames.isEmpty();
    }

    /**
     * Returns the current frame's image.
     *
     * @return The image of the current frame, or null if there are no frames.
     */
    public BufferedImage getCurrentFrameImage() {
        return frames.isEmpty() ? null : frames.get(currentFrameIndex).image;
    }

    /**
     * Checks whether the animation is currently playing.
     *
     * @return true if the animation is playing, false otherwise.
     */
    public boolean isPlaying() {
        return !frames.isEmpty() && isPlaying;
    }

    // ----- SETTERS -----
    /**
     * Sets the x-coordinate of the animation's top-left corner.
     *
     * @param x The new x-coordinate.
     */
    public final void setX(final int x) {
        position.x = x;
    }

    /**
     * Sets the y-coordinate of the animation's top-left corner.
     *
     * @param x The new y-coordinate.
     */
    public final void setY(final int y) {
        position.y = y;
    }

    /**
     * Sets the position at which the animation will be rendered.
     *
     * @param x The new x-coordinate.
     * @param y The new y-coordinate.
     */
    public void setPosition(final int x, final int y) {
        position.x = x;
        position.y = y;
    }

    /**
     * Sets the position at which the animation will be rendered.
     *
     * @param position The new position.
     */
    public void setPosition(final Point position) {
        this.position = position;
    }

    // ----- OVERRIDDEN METHODS -----
    /**
     * Updates the animation's frame based on elapsed time.
     */
    @Override
    public void update() {
        if (!isPlaying || isEmpty()) {
            return;
        }

        long currentTime = System.currentTimeMillis();
        elapsedFrameDisplayTime += currentTime - lastUpdateTime;
        lastUpdateTime = currentTime;

        // If the elapsed time exceeds the duration of the current frame, move to the next frame
        if (elapsedFrameDisplayTime >= frames.get(currentFrameIndex).frameDurationMs) {
            elapsedFrameDisplayTime = 0;
            nextFrame();
        }
    }

    /**
     * Renders the current frame at the animations's x and y position.
     *
     * @param g2 The Graphics2D object used for rendering the entity.
     */
    @Override
    public void render(Graphics2D g2d) {
        BufferedImage currentImage = frames.get(currentFrameIndex).image;
        if (currentImage != null) {
            g2d.drawImage(currentImage, position.x, position.y, currentImage.getWidth(), currentImage.getHeight(), null);
        }
    }

    // ----- BUSINESS LOGIC METHODS ------
    /**
     * Loads animation frames from a given strip file.
     * <p>
     * Note: A valid strip file has no margins (i.e., no space between the
     * frames themselves and the outer borders of the image itself). It can
     * however have space *in-between* each frame.
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
        if (fileUrl == null || fileUrl.isEmpty()) {
            throw new IllegalArgumentException(String.format(
                    "%s: Must provide a strip animation file URL to load from.",
                    this.getClass().getName()
            ));
        }

        if (frameDurationMs <= 0) {
            throw new IllegalArgumentException(String.format(
                    "%s: Frame duration must be a positive value.",
                    this.getClass().getName()
            ));
        }

        if (numRows <= 0) {
            throw new IllegalArgumentException(String.format(
                    "%s: Number of rows must be a positive value.",
                    this.getClass().getName()
            ));
        }

        if (numColumns <= 0) {
            throw new IllegalArgumentException(String.format(
                    "%s: Number of colums must be a positive value.",
                    this.getClass().getName()
            ));
        }
        safeLoadStripAnimation(fileUrl, frameDurationMs, numRows, numColumns, Math.abs(horizontalSpace), Math.abs(verticalSpace));
    }

    /**
     * Loads animation frames from a given strip file that has with no
     * horizontal/vertical space between frames.
     * <p>
     * Note: A valid strip file has no margins (i.e., no space between the
     * frames themselves and the outer borders of the image itself).
     *
     * @param fileUrl The URL of the strip file.
     * @param frameDurationMs How many milliseconds to display each frame.
     * @param numRows The number of rows in the strip file.
     * @param numColumns The number of columns in the strip file.
     */
    public void loadStripFile(final String fileUrl, final long frameDurationMs, final int numRows, final int numColumns) {
        loadStripFile(fileUrl, frameDurationMs, numRows, numColumns, 0, 0);
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
        if (isEmpty()) {
            throw new IllegalStateException(String.format(
                    "%s: Cannot start an empty animation",
                    this.getClass().getName()
            ));

        }
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
        if (isEmpty()) {
            return;
        }

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

    private void safeLoadStripAnimation(final String fileUrl, final long frameDurationMs, final int numRows, final int numColumns, final int horizontalSpace, final int verticalSpace) {
        BufferedImage stripImage = ImageManager.loadBufferedImage(fileUrl);

        if (stripImage == null) {
            throw new NullPointerException(String.format(
                    "%s: Could not load strip animation file <'%s'>",
                    this.getClass().getName(),
                    fileUrl
            ));
        }

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
        public AnimationFrame(final String imageUrl, final long frameDurationMs) {
            if (imageUrl == null || imageUrl.isEmpty()) {
                throw new IllegalArgumentException(String.format(
                        "%s: Must provide a URL.",
                        this.getClass().getName()
                ));
            }

            if (frameDurationMs <= 0) {
                throw new IllegalArgumentException(String.format(
                        "%s: Must provide a positive duration.",
                        this.getClass().getName()
                ));
            }

            this.image = ImageManager.loadBufferedImage(imageUrl);
            this.frameDurationMs = frameDurationMs;
        }

        /**
         * Constructs a new animation frame using a pre-loaded
         * {@link BufferedImage}.
         *
         * @param imageUrl The displayed image.
         * @param frameDurationMs How many milliseconds to display the frame
         * for.
         */
        public AnimationFrame(final BufferedImage image, final long frameDurationMs) {
            if (image == null) {
                throw new IllegalArgumentException(String.format(
                        "%s: Must provide an image.",
                        this.getClass().getName()
                ));
            }

            if (frameDurationMs <= 0) {
                throw new IllegalArgumentException(String.format(
                        "%s: Must provide a positive duration.",
                        this.getClass().getName()
                ));
            }

            this.image = image;
            this.frameDurationMs = frameDurationMs;
        }
    }
}
