package edu.wm.cs.cs301.matthewcheng.gui;

import edu.wm.cs.cs301.matthewcheng.generation.Maze;

public class Container {
    Maze maze;
    static Container container = new Container();

    public void setMaze(Maze maze) {
        this.maze = maze;
    }

    public Maze getMaze() {
        return maze;
    }

    public static Container getInstance() {
        return container;
    }
}
