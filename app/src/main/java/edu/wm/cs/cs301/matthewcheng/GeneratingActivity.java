package edu.wm.cs.cs301.matthewcheng;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

import edu.wm.cs.cs301.matthewcheng.generation.Maze;
import edu.wm.cs.cs301.matthewcheng.generation.MazeFactory;
import edu.wm.cs.cs301.matthewcheng.generation.Order;
import edu.wm.cs.cs301.matthewcheng.gui.Container;

public class GeneratingActivity extends AppCompatActivity implements Order {

    private static final String TAG = "GeneratingActivity";

    // initiate random
    private final Random rand = new Random();

    // declare bar and spinners
    private ProgressBar pb;
    private Spinner spinner2;
    private Spinner spinner3;

    // this determines whether the progress bar is done or not
    private final boolean[] loading = {true};

    // helpful attributes
    private int seed;
    private int skill;
    private boolean perfect;
    private Builder build;
    public Maze maze;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generating);

        // find progress bar
        pb = findViewById(R.id.progressBar);

        // get values from AMazeActivity
        Intent intent = getIntent();
        int skill = intent.getIntExtra("skill",0);
        String build = intent.getStringExtra("build");
        boolean rooms = intent.getBooleanExtra("rooms",true);
        String selection = intent.getStringExtra("selection");

        // get builder
        Order.Builder builder = chooseBuilder(build);

        // get string
        String isPerfect;
        if (rooms) {
            isPerfect = "no";
        } else {
            isPerfect = "yes";
        }

        // get shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPref", MODE_PRIVATE);

        // generate a new maze
        if (selection.equals("new")) {
            SharedPreferences.Editor editor = sharedPreferences.edit();

            this.seed = rand.nextInt();

            editor.putInt(skill + build + isPerfect, seed);
            editor.apply();
        }
        // regenerate an old maze
        else {
            this.seed = sharedPreferences.getInt(skill + build + isPerfect, 0);
        }
        this.skill = skill;
        this.perfect = !rooms;
        this.build = builder;

        // begin order
        MazeFactory mazefactory = new MazeFactory();
        mazefactory.order(this);
        mazefactory.waitTillDelivered();

        // find driver spinner
        spinner2 = findViewById(R.id.spinner2);

        // create a list of items for the spinner
        ArrayList<String> arrayList2 = new ArrayList<>();
        arrayList2.add(" ");
        arrayList2.add("Manual");
        arrayList2.add("Wall Follower");
        arrayList2.add("Wizard");
        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrayList2);
        arrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // give the spinner the list of items
        spinner2.setAdapter(arrayAdapter2);

        // for when spinner's selection is changed
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            /**
             * Uses toast to display a change in driver spinner selection.
             * @param parent the spinner object
             * @param view not used
             * @param position location of the current selection
             * @param id not used
             */
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String text = parent.getItemAtPosition(position).toString();
                Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // find quality spinner
        spinner3 = findViewById(R.id.spinner3);

        // create a list of items for the spinner
        ArrayList<String> arrayList3 = new ArrayList<>();
        arrayList3.add("Premium");
        arrayList3.add("Mediocre");
        arrayList3.add("Soso");
        arrayList3.add("Shaky");
        ArrayAdapter<String> arrayAdapter3 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrayList3);
        arrayAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // give the spinner the list of items
        spinner3.setAdapter(arrayAdapter3);

        // for when spinner's selection is changed
        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            /**
             * Uses toast to display a change in quality spinner selection.
             * @param parent the spinner object
             * @param view not used
             * @param position location of the current selection
             * @param id not used
             */
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String text = parent.getItemAtPosition(position).toString();
                Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // value that represents the current progress
        final int[] i = {0};

        // find the button for entering the maze
        Button btn3 = findViewById(R.id.button3);

        // for when the button is pressed
        btn3.setOnClickListener(new View.OnClickListener() {
            /**
             * Checks to see if the progress bar is done loading. If not,
             * toast displays a message telling the user to wait. If it is done
             * loading, checks to see if the user has selected both a driver
             * and a quality setting. If not, toast once again displays a message, but
             * this time tells the user to make a selection. If a selection has been made,
             * switches activity to the next activity. This is either PlayAnimationActivity
             * or PlayManuallyActivity, depending on the driver selection.
             * @param v current view
             */
            @Override
            public void onClick(View v) {
                if (!loading[0]) {
                    String selected2 = spinner2.getSelectedItem().toString();
                    String selected3 = spinner3.getSelectedItem().toString();

                    if (!selected2.equals(" ")) {
                        if (selected2.equals("Manual")) {
                            Log.v(TAG,"Play maze manually");
                            Intent intent = new Intent(GeneratingActivity.this, PlayManuallyActivity.class);
                            Container.getInstance().setMaze(maze);
                            startActivity(intent);
                            finish();
                        }
                        else {
                            Log.v(TAG,"Robot will play maze");
                            Intent intent = new Intent(GeneratingActivity.this, PlayAnimationActivity.class);
                            intent.putExtra("driver",spinner2.getSelectedItem().toString());
                            intent.putExtra("quality", spinner3.getSelectedItem().toString());
                            Container.getInstance().setMaze(maze);
                            startActivity(intent);
                            finish();
                        }
                    }
                    else {
                        Log.e(TAG, "Driver has not been selected");
                        Toast toast = Toast.makeText(getBaseContext(), "Please select driver and quality settings", Toast.LENGTH_LONG);
                        toast.show();
                    }
                }
                else {
                    Log.e(TAG, "Cannot go to next screen, as maze is still being created");
                    Toast toast = Toast.makeText(getBaseContext(), "Please wait for the maze to finish loading", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });
    }

    /**
     * Determine the builder based off of the string
     * @param build String representing builder
     * @return Builder
     */
    private Order.Builder chooseBuilder(String build) {
        Order.Builder builder;
        switch (build) {
            case "Prim":
                System.out.println("Using Prim");
                builder = Order.Builder.Prim;
                break;
            case "Boruvka":
                System.out.println("Using Boruvka");
                builder = Order.Builder.Boruvka;
                break;
            default:
                System.out.println("Using DFS");
                builder = Order.Builder.DFS;
                break;
        }
        return builder;
    }

    @Override
    public void onBackPressed() {
        Log.v(TAG, "Back button pressed");
        Intent setIntent = new Intent(this, AMazeActivity.class);
        startActivity(setIntent);
        finish();
    }

    @Override
    public int getSkillLevel() {
        return skill;
    }

    @Override
    public Builder getBuilder() {
        return build;
    }

    @Override
    public boolean isPerfect() {
        return perfect;
    }

    @Override
    public int getSeed() {
        return seed;
    }

    @Override
    public void deliver(Maze mazeConfig) {
        maze = mazeConfig;
    }

    @Override
    public void updateProgress(int percentage) {
        pb.setProgress(percentage, true);
        if (percentage == 100) {
            loading[0] = false;
        }
    }
}