package com.rikuthin.interfaces;

import java.awt.Graphics2D;


/* An interface representing objects that can be rendered on the screen.
 * Any class implementing this interface must define a method to render 
 * itself using a {@link Graphics2D} object.
 */
public interface Renderable {

    /**
     * Renders the object onto the provided graphics context.
     *
     * @param g2d The {@link Graphics2D} object used for rendering.
     */
    void render(Graphics2D g2d);
}
