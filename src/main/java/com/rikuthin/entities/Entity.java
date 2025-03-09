package com.rikuthin.entities;

import java.awt.Graphics2D;

/**
 * The base class for all entities in the game. This class represents any object
 * that has behavior (e.g., movement, animation, or interaction with other entities) in the game world.
 */
public abstract class Entity {

    protected int x, y; // Position of the entity
    protected int width, height; // Dimensions of the entity

    /**
     * Constructor to initialize the entity's position and size.
     * 
     * @param x      The x-coordinate of the entity.
     * @param y      The y-coordinate of the entity.
     * @param width  The width of the entity.
     * @param height The height of the entity.
     */
    public Entity(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /**
     * Abstract method to update the entity's behavior. This method will be 
     * overridden by subclasses to define how each entity behaves in the game world.
     */
    public abstract void update();

    /**
     * Abstract method to render the entity. This method will be overridden 
     * by subclasses to define how each entity is drawn in the game.
     * 
     * @param g2 The Graphics2D object used for rendering the entity.
     */
    public abstract void render(Graphics2D g2);
}
