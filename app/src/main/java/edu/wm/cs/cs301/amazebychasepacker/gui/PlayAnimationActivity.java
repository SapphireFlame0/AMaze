package edu.wm.cs.cs301.amazebychasepacker.gui;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import edu.wm.cs.cs301.amazebychasepacker.databinding.ActivityPlayAnimationBinding;

import edu.wm.cs.cs301.amazebychasepacker.R;
import edu.wm.cs.cs301.amazebychasepacker.generation.Maze;
import edu.wm.cs.cs301.amazebychasepacker.generation.MazeContainer;

public class PlayAnimationActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityPlayAnimationBinding binding;

    //Driving Information//////////////////////////////////
    private int Driver = 0;
    private String RobotConfig = "1111";
    /*
    private Driver theDriver;
    private Robot theRobot;
     */

    ///////////////////////////////////////////////////////

    private boolean showMapV = true;
    private double mapScale = 1.0;
    private boolean active = false;
    private int animationSpeed = 2;
    private int failure_cause = 0;


    //Hardcoded P6 values///
    private float consumedEnergy = 2400;
    private int pathLength = 100;
    ////////////////////////////


    Maze theMaze;

    //GUI Elements//
    ProgressBar consumption;
    CheckBox ShowMap;
    Button decreaseScale;
    Button increaseScale;
    Button winning;
    Button losing;
    Button start;
    SeekBar animation;
    ImageView sensorStatus;

    ///////////////////////////


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Driver = getIntent().getIntExtra("Driver", 0);
        RobotConfig = getIntent().getStringExtra("RobotConfig");

        setContentView(R.layout.activity_play_animation);

        consumption = (ProgressBar) findViewById(R.id.EnergyLeft);
        consumption.setProgress(100);

        String msg = "Driver:  " + Driver + " RobotConfig:  " + RobotConfig;
        Log.v("PlayAnimationActivity", msg);
        Snackbar.make(consumption, msg, Snackbar.LENGTH_SHORT).show();

        ShowMap = (CheckBox) findViewById(R.id.showMap);
        ShowMap.setChecked(true);

        decreaseScale = (Button) findViewById(R.id.Decrease_Scale);
        increaseScale = (Button) findViewById(R.id.Increase_Scale);

        winning = (Button) findViewById(R.id.SkipAnimation);
        losing = (Button) findViewById(R.id.Go2Lose);

        theMaze = GeneratingActivity.finishedMaze;

        start = (Button) findViewById(R.id.ActivateButton);
        animation = (SeekBar) findViewById(R.id.SpeedBar);
        animation.setProgress(2);

        //Depending on the sensor status of robot, the image will change on screen.  Will be
        //implemented further in project 7
        sensorStatus = (ImageView) findViewById(R.id.SensorStatus);
        Drawable sensorImage = getResources().getDrawable(R.drawable.s1100, getTheme());
        sensorStatus.setImageDrawable(sensorImage);

        //change showMap setting if pressed.
        ShowMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(showMapV)
                {
                    showMapV = false;
                }
                else
                {
                    showMapV = true;
                }

                String msg = "Show Map:  " + showMapV;
                Log.v("PlayAnimationActivity", msg);
                Snackbar.make(ShowMap, msg, Snackbar.LENGTH_SHORT).show();
            }
        });
        //decrease map scale if pressed.
        decreaseScale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mapScale > .10)
                {
                    mapScale -= .10;
                }

                String msg = "New Scale:  " + mapScale;
                Log.v("PlayAnimationActivity", msg);
                Snackbar.make(decreaseScale, msg, Snackbar.LENGTH_SHORT).show();

            }
        });
        //increase map scale if pressed
        increaseScale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mapScale < 5)
                {
                    mapScale += .10;
                }

                String msg = "New Scale:  " + mapScale;
                Log.v("PlayAnimationActivity", msg);
                Snackbar.make(increaseScale, msg, Snackbar.LENGTH_SHORT).show();
            }
        });
        //switch to winning
        winning.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String msg = "Going to Winning";
                Log.v("PlayAnimationActivity", msg);
                Snackbar.make(winning, msg, Snackbar.LENGTH_SHORT).show();
                switchToWinning();
            }
        });
        //switch to losing
        losing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = "Going to Losing";
                Log.v("PlayAnimationActivity", msg);
                Snackbar.make(winning, msg, Snackbar.LENGTH_SHORT).show();
                switchToLosing();

            }
        });
        //if start/stop button is pressed, either activate or deactivate driver depending on state
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(active)
                {
                    stopDriver();
                }
                else
                {
                    startDriver();
                }

                String msg = "Start/Stop button Selected";
                Log.v("PlayAnimationActivity", msg);
                Snackbar.make(increaseScale, msg, Snackbar.LENGTH_SHORT).show();
            }
        });
        //if animation speed bar is changed, change the animation speed.
        animation.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                animationSpeed = progress;

                String msg = "Animation speed set to " + progress + " .";

                Log.v("PlayAnimationActivity", msg);
                Snackbar.make(animation, msg, Snackbar.LENGTH_SHORT).show();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


    }

    @Override
    public void onBackPressed()
    {
        switchToTitle();
    }

    /**
     * switches to AMazeActivity
     */
    private void switchToTitle() {
        Intent toTitle = new Intent(this, AMazeActivity.class);
        startActivity(toTitle);
    }

    private void stopDriver() {
        //project 7
    }

    private void startDriver() {
        //project 7
    }

    /**
     * Switches to WinningActivity with path and consumption
     */
    private void switchToWinning() {
        Intent toWinning = new Intent(this, WinningActivity.class);
        toWinning.putExtra("Consumption", consumedEnergy);
        toWinning.putExtra("path", pathLength);
        startActivity(toWinning);
    }

    /**
     * Switches ot LosingActivity and sends path and consumption
     */
    private void switchToLosing() {
        Intent toLosing = new Intent(this, LosingActivity.class);
        toLosing.putExtra("Consumption", consumedEnergy);
        toLosing.putExtra("path", pathLength);
        toLosing.putExtra("Failure", failure_cause);
        startActivity(toLosing);
    }

}