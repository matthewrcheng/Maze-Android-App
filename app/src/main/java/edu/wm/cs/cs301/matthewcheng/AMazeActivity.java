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
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;

public class AMazeActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final String TAG = "AMazeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PlayBackgroundSound();

        // find build spinner
        Spinner spinner = findViewById(R.id.spinner);

        // create a list of items for the spinner
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("DFS");
        arrayList.add("Prim's");
        arrayList.add("Boruvka");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // give the spinner the list of items
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(this);

        // find load maze button
        Button btn = (Button)findViewById(R.id.button);
        // find generate maze button
        Button btn2 = (Button)findViewById(R.id.button2);
        // find room switch
        Switch switch1 = (Switch)findViewById(R.id.switch1);
        // find skill seekbar
        SeekBar sb = (SeekBar)findViewById(R.id.seekBar);

        // for when load maze button is selected
        btn.setOnClickListener(new View.OnClickListener() {

            /**
             * Switches activity to GeneratingActivity for loading an old maze,
             * sending the selected skill, build, and room values.
             */
            @Override
            public void onClick(View v) {
                // get shared preferences
                SharedPreferences sharedPreferences = getSharedPreferences("sharedPref", MODE_PRIVATE);
                int skill = sb.getProgress();
                String build = spinner.getSelectedItem().toString();
                boolean rooms = switch1.isChecked();

                // determine string for representing perfect
                String isPerfect;
                if (rooms) {
                    isPerfect = "no";
                } else {
                    isPerfect = "yes";
                }

                // load a saved maze using above criteria
                if (sharedPreferences.contains(skill + build + isPerfect)) {
                    Log.v(TAG,"Load saved maze");
                    Intent intent = new Intent(AMazeActivity.this, GeneratingActivity.class);
                    intent.putExtra("skill", skill);
                    intent.putExtra("build", build);
                    intent.putExtra("rooms", rooms);
                    intent.putExtra("selection","load");
                    startActivity(intent);
                    // not finishing the activity allows the background music to play
                    // finish();
                }
                else {
                    Log.e(TAG, "Current settings could not be loaded as they have not been used before");
                    Toast.makeText(getBaseContext(), "Error: These settings have not been used before", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // for when generate maze button is selected
        btn2.setOnClickListener(new View.OnClickListener() {

            /**
             * Switches activity to GeneratingActivity for generating a new maze,
             * sending the selected skill, build, and room values.
             * @param v view of current click
             */
            @Override
            public void onClick(View v) {
                // determine parameters
                int skill = sb.getProgress();
                String build = spinner.getSelectedItem().toString();
                boolean rooms = switch1.isChecked();

                // add parameters to intent
                Log.v(TAG,"Generate a new maze");
                Intent intent = new Intent(AMazeActivity.this, GeneratingActivity.class);
                intent.putExtra("skill", skill);
                intent.putExtra("build", build);
                intent.putExtra("rooms", rooms);
                intent.putExtra("selection","new");
                startActivity(intent);
                // not finishing the activity allows the background music to play
                // finish();
            }
        });

        // for when switch condition is changed
        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            /**
             * Uses toast to display a change in switch selection.
             * @param compoundButton the switch object
             * @param b not used
             */
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()) {
                    Toast.makeText(getBaseContext(), "Rooms", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getBaseContext(), "No Rooms", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // for when seekbar selection is changed
        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            /**
             * Uses toast to display a change in the seekbar selection.
             * @param seekBar the seekbar object
             * @param i new progress
             * @param b not used
             */
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                Toast.makeText(getBaseContext(), "Skill Level: "+String.valueOf(i), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    /**
     * Uses toast to display a change in spinner selection.
     * @param adapterView current adapter view
     * @param view not used
     * @param i location of new slection
     * @param l not used
     */
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String text = adapterView.getItemAtPosition(i).toString();
        Toast.makeText(adapterView.getContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
    public void PlayBackgroundSound() {
        Intent intent = new Intent(this, BackgroundSoundService.class);
        startService(intent);
    }
}