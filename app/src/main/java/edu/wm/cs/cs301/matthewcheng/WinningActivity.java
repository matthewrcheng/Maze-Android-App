package edu.wm.cs.cs301.matthewcheng;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class WinningActivity extends AppCompatActivity {

    private static final String TAG = "WinningActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_winning);

        /*
        MediaPlayer mp = MediaPlayer.create(this, R.raw.win);
        mp.setVolume(100, 100);
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.reset();
                mp.release();
            }
        });
        mp.start();
         */

        // get values from PlayManuallyActivity or PlayAnimationActivity
        Intent intent = getIntent();
        int distance = intent.getIntExtra("distance",0);
        int optimal = intent.getIntExtra("optimal",0);
        int energy = intent.getIntExtra("energy",0);

        // find text views for distance, optimal distance, and energy consumed
        TextView text14 = (TextView)findViewById(R.id.textView14);
        TextView text15 = (TextView)findViewById(R.id.textView15);
        TextView text16 = (TextView)findViewById(R.id.textView16);

        // append the values passed through intent to the end of display strings
        text14.append(String.valueOf(distance));
        text15.append(String.valueOf(optimal));
        // if source was PlayManually, make energy text view invisible
        if (energy == -1) {
            text16.setVisibility(View.GONE);
        }
        // otherwise, append this value as well
        else {
            text16.append(String.valueOf(energy));
        }

        // find relaunch button
        Button btn13 = (Button)findViewById(R.id.button13);

        // for when the button is pressed
        btn13.setOnClickListener(new View.OnClickListener() {
            /**
             * Takes user back to title screen.
             * @param v current view
             */
            @Override
            public void onClick(View v) {
                Log.v(TAG,"Back to title");
                startActivity(new Intent(WinningActivity.this, AMazeActivity.class));
                finish();
            }
        });
    }

    /**
     * Instead of returning to the previous screen,
     * the back button is overridden so that it takes
     * the user back to the title screen.
     */
    @Override
    public void onBackPressed() {
        Log.v(TAG, "Back button pressed");
        Intent setIntent = new Intent(WinningActivity.this, AMazeActivity.class);
        startActivity(setIntent);
        finish();
    }
}