package edu.wm.cs.cs301.matthewcheng;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import edu.wm.cs.cs301.matthewcheng.generation.CardinalDirection;
import edu.wm.cs.cs301.matthewcheng.generation.Maze;
import edu.wm.cs.cs301.matthewcheng.gui.Constants;
import edu.wm.cs.cs301.matthewcheng.gui.Container;
import edu.wm.cs.cs301.matthewcheng.gui.Controller;
import edu.wm.cs.cs301.matthewcheng.gui.MazePanel;
import edu.wm.cs.cs301.matthewcheng.gui.Robot;
import edu.wm.cs.cs301.matthewcheng.gui.RobotDriver;
import edu.wm.cs.cs301.matthewcheng.gui.StatePlaying;
import edu.wm.cs.cs301.matthewcheng.gui.UnreliableRobot;
import edu.wm.cs.cs301.matthewcheng.gui.WallFollower;
import edu.wm.cs.cs301.matthewcheng.gui.Wizard;

public class PlayAnimationActivity extends AppCompatActivity implements Controller {

    private static final String TAG = "PlayAnimationActivity";
    private static final String lose = "Ran out of energy";

    // get maze from GeneratingActivity and declare attributes
    Maze maze = Container.getInstance().getMaze();
    MazePanel mp;
    StatePlaying sp;
    UnreliableRobot robot;
    RobotDriver driver;
    ProgressBar pb;
    Button forward;
    Button left;
    Button right;
    Button backward;
    AsyncTask<UnreliableRobot, Integer, Integer> gs;

    // calculate shortest distance, initialize speed and paused status
    private final int shortest = maze.getDistanceToExit(maze.getStartingPosition()[0],maze.getStartingPosition()[1]);
    private int speed = 2;
    private boolean paused = true;

    // keep track of sensor quality
    private boolean reliableForward = false;
    private boolean reliableLeft = false;
    private boolean reliableRight = false;
    private boolean reliableBackward = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_animation);

        Log.v(TAG, "Starting maze");

        // find the mazepanel
        mp = findViewById(R.id.view);

        // get values from GeneratingActivity
        Intent intent = getIntent();
        String driver = intent.getStringExtra("driver");
        String quality = intent.getStringExtra("quality");

        // set booleans according to robot quality
        switch(quality) {
            case "Premium":
                reliableForward = true;
                reliableLeft = true;
                reliableRight = true;
                reliableBackward = true;
                break;
            case "Mediocre":
                reliableForward = true;
                reliableBackward = true;
                break;
            case "Soso":
                reliableLeft = true;
                reliableRight = true;
                break;
            default:
                break;
        }

        // set robot driver
        if (driver.equals("Wizard")) {
            Log.v(TAG, "Maze will use Wizard Driver");
            this.driver = new Wizard();
            robot = new UnreliableRobot(this);
            this.driver.setMaze(maze);
            this.driver.setRobot(robot);
        }
        else {
            Log.v(TAG, "Maze will use WallFollower Driver");
            this.driver = new WallFollower();
            robot = new UnreliableRobot(this);
            this.driver.setRobot(robot);
            this.driver.setMaze(maze);
        }

        // start/resume/pause
        Button start = findViewById(R.id.button11);

        // solution
        Button map = findViewById(R.id.button8);

        // zoom out
        ImageButton zoomOut = findViewById(R.id.imageButton7);

        // zoom in
        ImageButton zoomIn = findViewById(R.id.imageButton8);

        // speed up
        Button speedUp = findViewById(R.id.button9);

        // slow down
        Button slowDown = findViewById(R.id.button10);

        // show solution
        Button full = findViewById(R.id.button7);

        // find the progress bar that represents the robot's battery
        pb = findViewById(R.id.progressBar2);

        // forward sensor status
        forward = findViewById(R.id.button12);

        // right sensor status
        right = findViewById(R.id.button14);

        // left sensor status
        left = findViewById(R.id.button15);

        // backward sensor status
        backward = findViewById(R.id.button16);

        /**
         * Resumes or pauses the robot depending on the current status.
         */
        start.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (paused) {
                    paused = false;
                    start.setText("Pause");
                    Log.v(TAG, "Switched robot to play mode.");
                }
                else {
                    paused = true;
                    start.setText("Resume");
                    Log.v(TAG, "Switched robot to pause mode");
                }
                Toast.makeText(PlayAnimationActivity.this, "Toggled Robot Activity", Toast.LENGTH_SHORT).show();
            }
        });

        /**
         * Toggles the visibility of the map of the maze.
         */
        map.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.v(TAG, "Map visibility toggled");
                sp.keyDown(Constants.UserInput.TOGGLELOCALMAP,0);
                sp.keyDown(Constants.UserInput.TOGGLEFULLMAP,0);
                sp.keyDown(Constants.UserInput.TOGGLESOLUTION,0);
                Toast.makeText(PlayAnimationActivity.this, "Toggled Map Visibility", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(PlayAnimationActivity.this, "Zoomed Out", Toast.LENGTH_SHORT).show();
            }
        });

        /**
         * Zooms in on the map to make it larger.
         */
        zoomIn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.v(TAG, "Zoomed in map");
                sp.keyDown(Constants.UserInput.ZOOMIN,0);
                Toast.makeText(PlayAnimationActivity.this, "Zoomed In", Toast.LENGTH_SHORT).show();
            }
        });

        /**
         * Speeds up the robot by decreasing its time in between movements.
         */
        speedUp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (speed < 3) {
                    Log.v(TAG, "Sped up robot");
                    speed++;
                    sp.setSpeed(calculateSpeed(speed));
                    Toast.makeText(PlayAnimationActivity.this, "Speed set to: " + speed, Toast.LENGTH_SHORT).show();
                } else {
                    Log.e(TAG, "Robot cannot speed up further");
                }
            }
        });

        /**
         * Slows down the robot by increasing its time in between movements.
         */
        slowDown.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (speed > 0) {
                    Log.v(TAG, "Slowed down robot");
                    speed--;
                    sp.setSpeed(calculateSpeed(speed));
                    Toast.makeText(PlayAnimationActivity.this, "Speed set to: " + speed, Toast.LENGTH_SHORT).show();
                } else {
                    Log.e(TAG, "Robot cannot slow down further");
                }
            }
        });

        /**
         * Toggles the full maze visibility. This was added so that players who are playing with high
         * skill level mazes have the option to only view the nearby walls
         * so that performance is not too slow.
         */
        full.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.v(TAG, "Full maze visibility toggled");
                sp.keyDown(Constants.UserInput.TOGGLEFULLMAP,0);
                Toast.makeText(PlayAnimationActivity.this, "Solution Toggled", Toast.LENGTH_SHORT).show();

            }
        });

        // set max battery to 3500, starting at full
        pb.setMax(3500);
        pb.setProgress(3500);

        // initialize and start stateplaying
        sp = new StatePlaying();
        sp.setSpeed(calculateSpeed(speed));
        sp.setManual(false);
        sp.setMazeConfiguration(maze);
        sp.start(this, mp);

        // initialize asynchronous task that handles robot's timing and sensor status
        gs = new GameSpeed();
        gs.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,robot);
        sp.keyDown(Constants.UserInput.ZOOMIN,0);
    }

    /**
     * Calculates the time between movements based off of the speed.
     * @param speed robot's current speed
     * @return timing
     */
    private int calculateSpeed(int speed) {
        switch (speed)
        {
            case 3:
                return 50;
            case 1:
                return 200;
            case 0:
                return 400;
            default:
                return 100;
        }
    }

    /**
     * Instead of returning to the previous screen,
     * the back button is overridden so that it takes
     * the user back to the title screen.
     */
    @Override
    public void onBackPressed() {
        Log.v(TAG, "Back button pressed");
        Intent setIntent = new Intent(PlayAnimationActivity.this, AMazeActivity.class);
        startActivity(setIntent);
        finish();
        gs.cancel(true);
    }

    @Override
    public boolean keyDown(Constants.UserInput key, int value) {
        return sp.keyDown(key, value);
    }

    @Override
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
        return reliableForward;
    }

    @Override
    public boolean hasReliableLeft() {
        return reliableLeft;
    }

    @Override
    public boolean hasReliableRight() {
        return reliableRight;
    }

    @Override
    public boolean hasReliableBackward() {
        return reliableBackward;
    }

    @Override
    public boolean isManual() {
        return false;
    }

    @Override
    public void switchToTitle() {
        onBackPressed();
    }

    @Override
    public void switchFromPlayingToWinning() {
        gs.cancel(true);
        Log.v(TAG,"Robot completed maze");
        Intent intent = new Intent(PlayAnimationActivity.this, WinningActivity.class);
        intent.putExtra("distance",robot.getOdometerReading());//+1);
        intent.putExtra("optimal",shortest);
        intent.putExtra("energy",3500-(int) robot.getBatteryLevel());
        startActivity(intent);
        //while (mp.isPlaying());
        finish();
    }

    @Override
    public void switchFromPlayingToLosing() {
        gs.cancel(true);
        Log.v(TAG,lose);
        Intent intent = new Intent(PlayAnimationActivity.this, LosingActivity.class);
        intent.putExtra("distance",robot.getOdometerReading());
        intent.putExtra("optimal",shortest);
        intent.putExtra("energy",0);
        startActivity(intent);
        //while (mp.isPlaying());
        finish();
    }

    class GameSpeed extends AsyncTask<UnreliableRobot, Integer, Integer> {

        @Override
        protected Integer doInBackground(UnreliableRobot... unreliableRobots) {
            Log.v(TAG, "Start new async task to handle the game's speed");

            try {
                while (robot.getBatteryLevel() > 0 & !robot.canSeeThroughTheExitIntoEternity(Robot.Direction.FORWARD)) {
                    // handles when the robot is paused
                    while (paused) {
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    // handles sensor status
                    if (robot.getSensor(Robot.Direction.FORWARD).isOperational()) {
                        forward.setBackgroundColor(Color.GREEN);
                    } else {
                        forward.setBackgroundColor(Color.RED);
                    }
                    if (robot.getSensor(Robot.Direction.RIGHT).isOperational()) {
                        right.setBackgroundColor(Color.GREEN);
                    } else {
                        right.setBackgroundColor(Color.RED);
                    }
                    if (robot.getSensor(Robot.Direction.LEFT).isOperational()) {
                        left.setBackgroundColor(Color.GREEN);
                    } else {
                        left.setBackgroundColor(Color.RED);
                    }
                    if (robot.getSensor(Robot.Direction.BACKWARD).isOperational()) {
                        backward.setBackgroundColor(Color.GREEN);
                    } else {
                        backward.setBackgroundColor(Color.RED);
                    }
                    // when not paused, try to drive the robot to the exit one step at a time
                    try {
                        driver.drive1Step2Exit();
                        pb.setProgress((int) robot.getBatteryLevel());
                    } catch (Exception e) {
                        switchFromPlayingToLosing();
                    }
                    // sleep in between movements
                    try {
                        Thread.sleep(calculateSpeed(speed));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            } catch (UnsupportedOperationException e) {
                return null;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            try {
                if (robot.canSeeThroughTheExitIntoEternity(Robot.Direction.FORWARD) & robot.getBatteryLevel() >= 6) {
                    //Log.v(TAG, "Robot has completed the maze.");
                    robot.move(1);
                    //switchFromPlayingToWinning();
                } else {
                    //Log.v(TAG, "Robot did not have enough energy to complete the maze.");
                    switchFromPlayingToLosing();
                }
            } catch (UnsupportedOperationException e) {
                switchFromPlayingToLosing();
            }
        }
    }
}