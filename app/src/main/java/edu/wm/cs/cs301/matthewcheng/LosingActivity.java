package edu.wm.cs.cs301.matthewcheng;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class LosingActivity extends AppCompatActivity {

    private static final String TAG = "LosingActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_losing);

        /*
        MediaPlayer mp = MediaPlayer.create(this, R.raw.lose);
        mp.setVolume(100, 100);
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                // TODO Auto-generated method stub
                mp.reset();
                mp.release();
            }
        });
        mp.start();
         */

        // get values from PlayAnimationActivity
        Intent intent = getIntent();
        int distance = intent.getIntExtra("distance",0);
        int optimal = intent.getIntExtra("optimal",0);
        int energy = intent.getIntExtra("energy",0);

        // find text views for distance, optimal distance, and energy consumed
        TextView text14 = (TextView)findViewById(R.id.textView14);
        TextView text15 = (TextView)findViewById(R.id.textView15);
        TextView text16 = (TextView)findViewById(R.id.textView16);

        // add the value that was passed through intent to the current display string
        text14.append(String.valueOf(distance));
        text15.append(String.valueOf(optimal));
        text16.append(String.valueOf(energy));

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
                startActivity(new Intent(LosingActivity.this, AMazeActivity.class));
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
        Log.v(TAG, "Bac k button pressed");
        Intent setIntent = new Intent(LosingActivity.this, AMazeActivity.class);
        startActivity(setIntent);
        finish();
    }
}