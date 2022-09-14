package edu.wm.cs.cs301.wenxindang.gui;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import edu.wm.cs.cs301.wenxindang.R;
import edu.wm.cs.cs301.wenxindang.generation.Constants;
import edu.wm.cs.cs301.wenxindang.generation.Controller;
import edu.wm.cs.cs301.wenxindang.generation.DataHolder;
import edu.wm.cs.cs301.wenxindang.generation.MazePanel;

/**
 * This Activity displays the maze generated based on the received controller.
 * and users can use buttons on the screen to operate the controller.
 *
 * @author Wenxin Dang
 */
public class PlayManuallyActivity extends AppCompatActivity {

    public String activity = "PlayManuallyActivity";
    private Context mContext;
    private boolean backPressed = false;
    private DataHolder holder;
    private Controller controller;

    private int pathLength;


    /**
     * Initialize the screen displayed.
     * @param savedInstanceState provides a bundle that is most recently saved.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_manually);
        mContext = PlayManuallyActivity.this;
        backPressed = false;
        pathLength = 0;
        Intent intentFromTitle = getIntent();
        holder = holder.getInstance();
        controller = holder.getController();
        Log.v(activity,"Got the Controller");
        controller.setMazePanel((MazePanel) findViewById(R.id.MazePanel));
        Log.v(activity,"Set the Panel");
        controller.startPlayManually();
        Log.v(activity,"Ready to play");
        setJumpButton();
        setDOWNButton();
        setUpButton();
        setRIGHTButton();
        setLEFTButton();
/*        setFailButton();*/
/*        setSuceedButton();*/
        HideWall_toggleButton();
        ShowSolution_toggleButton();
        setShowMapToggleButton();
    }

    /**
     * Jump if the Button is pressed.
     */
    private void setJumpButton(){
        Button jumpButton = (Button) findViewById(R.id.JUMPButton);
        jumpButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                toast("Jump");
              controller.keyDown(Constants.UserInput.JUMP, 1);
                pathLength++;
            }
        });
    }

    /**
     * Move UP if the Button is pressed.
     */
    private void setUpButton(){
        Button upButton = (Button) findViewById(R.id.UP_button);
        upButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
//                toast("move Up");
                controller.keyDown(Constants.UserInput.UP, 1);
                pathLength++;
                if (holder.getController().getState()==Constants.State.WinManually){
                    toast("Suceed!");
                    Intent successIntent = new Intent(PlayManuallyActivity.this, FinishActivity.class);
                    successIntent.putExtra("SorF", "succeed");
                    successIntent.putExtra("pathLength", pathLength);
                    startActivity(successIntent);
                }
            }
        });
    }

    /**
     * Move DOWN if the Button is pressed.
     */
    private void setDOWNButton(){
        Button downButton = (Button) findViewById(R.id.DOWN_button);
        downButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
//                toast("move down");
              controller.keyDown(Constants.UserInput.DOWN, 1);
                pathLength++;
            }
        });
    }

    /**
     * Turn RIGHT if the Button is pressed.
     */
    private void setRIGHTButton(){
        Button rightButton = (Button) findViewById(R.id.RIGHT_button);
        rightButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
//                toast("turn right");
              controller.keyDown(Constants.UserInput.RIGHT, 1);
            }
        });
    }

    /**
     * Turn LEFT if the Button is pressed.
     */
    private void setLEFTButton(){
        Button leftButton = (Button) findViewById(R.id.LEFT_button);
        leftButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
//                toast("turn left");
              controller.keyDown(Constants.UserInput.LEFT, 1);
            }
        });
    }

    /**
     * This is a shortcut to the final screen.
     */
/*    private void setSuceedButton(){
        Button winButton = (Button) findViewById(R.id.shortcut_success);
        winButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toast("Suceed!");
                Intent successIntent = new Intent(PlayManuallyActivity.this, FinishActivity.class);
                successIntent.putExtra("SorF", "succeed");
                startActivity(successIntent);
            }
        });
    }*/

/*    private void setFailButton(){
        Button failButton = (Button) findViewById(R.id.shortcut_failure);
        failButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toast("Fail!");
                Intent failIntent = new Intent(PlayManuallyActivity.this, FinishActivity.class);
                failIntent.putExtra("SorF", "fail");
                startActivity(failIntent);
            }
        });
    }*/

    /**
     * Hide the map if the ToggleButton is pressed.
     */
    private void setShowMapToggleButton(){
        ToggleButton ShowMap_toggleButton = (ToggleButton) findViewById(R.id.ShowMap_toggleButton);
        ShowMap_toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //in eclipse, it is changed by: boolean showmap = !showmap
                    controller.keyDown(Constants.UserInput.TOGGLEFULLMAP, 1);
//                    toast("Show the Map");
                } else {
                    controller.keyDown(Constants.UserInput.TOGGLEFULLMAP, 1);
//                    toast("Hide the Map");
                }
            }
        });
    }

    /**
     * Hide the solution if the ToggleButton is pressed.
     */
    private void ShowSolution_toggleButton(){
        ToggleButton ShowSolution_toggleButton = (ToggleButton) findViewById(R.id.ShowSolution_toggleButton);
        ShowSolution_toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //in eclipse, it is changed by: boolean showmap = !showmap
                    controller.keyDown(Constants.UserInput.TOGGLESOLUTION, 1);
//                    toast("Show the Solution");
                } else {
                    controller.keyDown(Constants.UserInput.TOGGLESOLUTION, 1);
//                    toast("Hide the Solution");
                }
            }
        });
    }

    /**
     * Hide the wall if the ToggleButton is pressed.
     */
    private void HideWall_toggleButton(){
        ToggleButton HideWall_toggleButton = (ToggleButton) findViewById(R.id.HideWall_toggleButton);
        HideWall_toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //in eclipse, it is changed by: boolean showmap = !showmap
                    controller.keyDown(Constants.UserInput.TOGGLELOCALMAP, 1);
//                    toast("Show the Wall");
                } else {
                    controller.keyDown(Constants.UserInput.TOGGLELOCALMAP, 1);
//                    toast("Hide the Wall");
                }
            }
        });
    }

    /**
     * show some messages with toast and Log.v() in the Logcat window;
     * @param text gives the string wanted to show.
     */
    private void toast(String text){
        Toast.makeText(mContext, text,Toast.LENGTH_SHORT).show();
        Log.v(activity, text);
    }

    /**
     * Return to the AMazeActivity.
     */
    @Override
    public void onBackPressed() {
        backPressed = true;
        toast("Back to Title.");
        Intent backToTitle = new Intent(PlayManuallyActivity.this, AMazeActivity.class);
        startActivity(backToTitle);
    }
}