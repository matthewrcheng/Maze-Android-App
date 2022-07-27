package edu.wm.cs.cs301.matthewcheng;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import edu.wm.cs.cs301.matthewcheng.generation.CardinalDirection;
import edu.wm.cs.cs301.matthewcheng.generation.Maze;
import edu.wm.cs.cs301.matthewcheng.gui.Constants;
import edu.wm.cs.cs301.matthewcheng.gui.Container;
import edu.wm.cs.cs301.matthewcheng.gui.Controller;
import edu.wm.cs.cs301.matthewcheng.gui.MazePanel;
import edu.wm.cs.cs301.matthewcheng.gui.StatePlaying;

public class PlayManuallyActivity extends AppCompatActivity implements Controller {

    private static final String TAG = "PlayManuallyActivity";

    // get maze from GeneratingActivity
    Maze maze = Container.getInstance().getMaze();

    // calculate shortest
    final int shortest = maze.getDistanceToExit(maze.getStartingPosition()[0],maze.getStartingPosition()[1]);
    // stores the distance moved by the player
    private int distance = 0;

    // declare mazepanel and stateplaying
    MazePanel mp;
    StatePlaying sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_manually);

        Log.v(TAG, "Starting maze");

        // find the maze panel
        mp = findViewById(R.id.view);

        // forward
        ImageButton forward = findViewById(R.id.imageButton);

        // left
        ImageButton left = findViewById(R.id.imageButton2);

        // right
        ImageButton right = findViewById(R.id.imageButton3);

        // jump
        Button jump = findViewById(R.id.button19);

        // walls
        Button walls = findViewById(R.id.button4);

        // full
        Button full = findViewById(R.id.button5);

        // solution
        Button solution = findViewById(R.id.button6);

        // zoom out
        ImageButton zoomOut = findViewById(R.id.imageButton7);

        // zoom in
        ImageButton zoomIn = findViewById(R.id.imageButton8);

        /**
         * The forward button has been pressed so distance is incremented
         * whenever the player can successfully move forward and then
         * performs the move.
         */
        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sp.checkMove(1)){
                    distance++;
                }
                keyDown(Constants.UserInput.UP,0);
            }
        });

        /**
         * Makes the player turn to the left when the left button is pressed.
         */
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //turn_mp.start();
                keyDown(Constants.UserInput.LEFT,0);
            }
        });

        /**
         * Makes the player turn to the right when the right button is pressed.
         */
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //turn_mp.start();
                keyDown(Constants.UserInput.RIGHT,0);
            }
        });

        /**
         * Makes the player attempt to jump and increments distance if the
         * position has changed.
         */
        jump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //jump_mp.start();
                int x = getCurrentPosition()[0];
                int y = getCurrentPosition()[1];
                keyDown(Constants.UserInput.JUMP,0);
                if (getCurrentPosition()[0]!=x | getCurrentPosition()[1]!=y) {
                    distance++;
                }
            }
        });

        /**
         * Toggles the visibility of the map.
         */
        walls.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.v(TAG, "Map visibility toggled");
                //sp.setMapMode(!sp.isInMapMode());
                sp.keyDown(Constants.UserInput.TOGGLELOCALMAP,0);
                Toast.makeText(PlayManuallyActivity.this, "Toggle Wall Visibility", Toast.LENGTH_SHORT).show();
            }
        });

        /**
         * Toggles the visibility of the full map.
         */
        full.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.v(TAG, "Full maze visibility toggled");
                sp.keyDown(Constants.UserInput.TOGGLEFULLMAP,0);
                Toast.makeText(PlayManuallyActivity.this, "Toggle Full Maze Visibility", Toast.LENGTH_SHORT).show();
            }
        });

        /**
         * Toggles the visibility of the solution.
         */
        solution.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.v(TAG, "Solution visibility toggled");
                sp.keyDown(Constants.UserInput.TOGGLESOLUTION,0);
                Toast.makeText(PlayManuallyActivity.this, "Toggle Solution Visibility", Toast.LENGTH_SHORT).show();
            }
        });

        /**
         * Zooms out from the map to make it smaller.
         */
        zoomOut.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.v(TAG, "Zoomed out map");
                sp.keyDown(Constants.UserInput.ZOOMOUT,0);
                Toast.makeText(PlayManuallyActivity.this, "Zoomed Out", Toast.LENGTH_SHORT).show();
            }
        });

        /**
         * Zooms in on the map to make it larger.
         */
        zoomIn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //sp.setShowSolution(!sp.isInShowSolutionMode());
                sp.keyDown(Constants.UserInput.ZOOMIN,0);
                Toast.makeText(PlayManuallyActivity.this, "Zoomed In", Toast.LENGTH_SHORT).show();
            }
        });

        // initializes and starts stateplaying
        sp = new StatePlaying();
        sp.setMazeConfiguration(maze);
        sp.start(this, mp);
    }

    /**
     * Instead of returning to the previous screen,
     * the back button is overridden so that it takes
     * the user back to the title screen.
     */
    @Override
    public void onBackPressed() {
        Log.v(TAG, "Back button pressed");
        Intent setIntent = new Intent(PlayManuallyActivity.this, AMazeActivity.class);
        startActivity(setIntent);
        finish();
    }

    @Override
    public boolean keyDown(Constants.UserInput key, int value) {
        return sp.keyDown(key, value);
    }

    /**
     * Retrieves the current maze.
     * @return maze
     */
    public Maze getMazeConfiguration() {
        return maze;
    }

    @Override
    public int[] getCurrentPosition() {
        return sp.getCurrentPosition();
    }

    @Override
    public CardinalDirection getCurrentDirection() {
        return sp.getCurrentDirection();
    }

    @Override
    public boolean hasReliableForward() {
        return false;
    }

    @Override
    public boolean hasReliableLeft() {
        return false;
    }

    @Override
    public boolean hasReliableRight() {
        return false;
    }

    @Override
    public boolean hasReliableBackward() {
        return false;
    }

    @Override
    public boolean isManual() {
        return true;
    }

    @Override
    public void switchToTitle() {
        onBackPressed();
    }

    @Override
    public void switchFromPlayingToWinning() {
        Log.v(TAG,"Player completed maze");
        Intent intent = new Intent(PlayManuallyActivity.this, WinningActivity.class);
        intent.putExtra("distance",distance);
        intent.putExtra("optimal",shortest);
        int energy = -1;
        intent.putExtra("energy", energy);
        startActivity(intent);
        finish();
    }

    @Override
    public void switchFromPlayingToLosing() {
        // this will never be called
    }
}