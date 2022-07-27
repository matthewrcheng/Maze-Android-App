package edu.wm.cs.cs301.matthewcheng.gui;

import edu.wm.cs.cs301.matthewcheng.generation.CardinalDirection;
import edu.wm.cs.cs301.matthewcheng.generation.Maze;

public interface Controller {


    /**
     * Method incorporates all reactions to keyboard input in original code.
     * The simple key listener calls this method to communicate input.
     * @param key is the user input
     * @param value is only used for the numerical input for the size of the maze
     */
    public boolean keyDown(Constants.UserInput key, int value);

    /**
     * Provides access to the maze configuration.
     * This is needed for a robot to be able to recognize walls
     * for the distance to walls calculation, to see if it
     * is in a room or at the exit.
     * Note that the current position is stored by the
     * controller. The maze itself is not changed during
     * the game.
     * This method should only be called in the playing state.
     * @return the MazeConfiguration
     */
    public Maze getMazeConfiguration();

    /**
     * Provides access to the current position.
     * The controller keeps track of the current position
     * while the maze holds information about walls.
     * This method should only be called in the playing state.
     * @return the current position as [x,y] coordinates,
     * {@code 0 <= x < width, 0 <= y < height}
     */
    public int[] getCurrentPosition();
    /**
     * Provides access to the current direction.
     * The controller keeps track of the current position
     * and direction while the maze holds information about walls.
     * This method should only be called in the playing state.
     * @return the current direction
     */
    public CardinalDirection getCurrentDirection();

    boolean hasReliableForward();

    boolean hasReliableLeft();

    boolean hasReliableRight();

    boolean hasReliableBackward();

    boolean isManual();

    void switchToTitle();

    void switchFromPlayingToWinning();

    void switchFromPlayingToLosing();
}
