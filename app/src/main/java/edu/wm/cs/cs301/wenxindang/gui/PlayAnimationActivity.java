package edu.wm.cs.cs301.wenxindang.gui;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import edu.wm.cs.cs301.wenxindang.generation.Robot;
import edu.wm.cs.cs301.wenxindang.generation.RobotDriver;

/**
 * This Activity displays the maze generated and an Amination to solve the maze
 * based on the received controller.
 * and users can use buttons on the screen to operate the controller.
 *
 * @author Wenxin Dang
 */
public class PlayAnimationActivity extends AppCompatActivity {

    private String activity = "PlayAnimationActivity";
    private Context mContext;
    private boolean backPressed = false;
    private ProgressBar energy_progressBar;
    private TextView energy_progress;
    private int recordenergyprogress;
    private String driver;
    private boolean pause;
    private Handler handler;
    private DataHolder holder;
    private Controller controller;

    private Thread thread;
    private boolean win;
    private Robot robot;
    private RobotDriver robotdriver;

    /**
     * Initialize the screen displayed.
     * @param savedInstanceState provides a bundle that is most recently saved.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_animation);
        mContext = PlayAnimationActivity.this;
        backPressed = false;
        pause = false;
        handler = new Handler(Looper.myLooper());

        Intent intentFromTitle = getIntent();
        driver = intentFromTitle.getStringExtra("driver");
        toast("Received driver is: " + driver);
/*        setFailButton();
        setSuceedButton();*/
        HideWall_toggleButton();
        ShowSolution_toggleButton();
        setShowMapToggleButton();
        pause_toggleButton();

        holder = holder.getInstance();
        controller = holder.getController();
        controller.setMazePanel((MazePanel) findViewById(R.id.MazePanelAnimation));
        controller.startPlayManually();

        energy_progressBar = (ProgressBar) findViewById(R.id.energy_progressBar);
        energy_progress = (TextView) findViewById(R.id.remainingEnergy_TextView);
        energy_progressBar.setMax(2000);
        energy_progressBar.setProgress(2000);
        Log.v(activity,"set energy_progressBar");

        robot = controller.getRobot();
        robotdriver = controller.getDriver();
        robot.setController(controller);
        robotdriver.setRobot(robot);
        robotdriver.setMaze(controller.getMazeConfiguration());
        Log.v(activity,"set robot and driver");
        win = false;
        /**
         * Start to drive the robot to exit.
         */
        new Thread(new Runnable() {
                @Override
                public void run() {
                    Log.v(activity,"Start thread:");
                    controller.keyDown(Constants.UserInput.TOGGLELOCALMAP, 1);
                    controller.keyDown(Constants.UserInput.TOGGLEFULLMAP, 1);
                    controller.keyDown(Constants.UserInput.TOGGLESOLUTION, 1);
                    Log.v(activity,"Show maps etc.");
                    while (!robot.isAtExit()) {
                        if (backPressed){
                            /*Thread.interrupted();*/
                            break;
                        }
                        if (pause){
                            Log.v(activity,"Pause");
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        else {
                            try {
                                /* robotdriver.drive2Exit();*/
                                robotdriver.UpdateInfo();
                                robotdriver.drive1Step2Exit();
                                Log.v(activity,"Drove 1 step closer to exit.");
                            } catch (Exception e) {
                                Log.v(activity,"Ops, exception in while loop");
                                break;
                            }
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    energy_progress.setText(Integer.toString((int) robot.getBatteryLevel()));
                                    energy_progressBar.setProgress((int) robot.getBatteryLevel());
                                }
                            });
                        }
                    }
                    Log.v(activity,"out of the while loop");
                    if (!backPressed){
                        if (robot.getBatteryLevel()==0.0f) {
                            moveToFailScreen();
                        }
                        else if (!robot.isAtExit()){
                            Log.v(activity,"the robot is not at exit");
                            moveToFailScreen();
                        }
                        else if (!robot.canSeeThroughTheExitIntoEternity(Robot.Direction.FORWARD)) {
                            Log.v(activity,"the robot is at exit");
                            if (robot.getBatteryLevel()==0.0f) {
                                Log.v(activity,"the robot does not have enough battery");
                                moveToFailScreen();
                            }
                            else if (robot.canSeeThroughTheExitIntoEternity(Robot.Direction.LEFT)) {
                                Log.v(activity,"the robot can win if rotate left");
                                if (robot.getBatteryLevel()>=3.0f) {
                                    robot.rotate(Robot.Turn.LEFT);
                                    try {
                                        Thread.sleep(100);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    moveToRobotSucceedScreen(robotdriver.getPathLength(),(int) robotdriver.getEnergyConsumption());
                                }
                                else {
                                    moveToFailScreen();
                                }
                            }
                            else {
                                Log.v(activity,"the robot can win if rotate right");
                                if (robot.getBatteryLevel()>=3.0f) {
                                    robot.rotate(Robot.Turn.RIGHT);
                                    try {
                                        Thread.sleep(100);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    moveToRobotSucceedScreen(robotdriver.getPathLength(),(int) robotdriver.getEnergyConsumption());
                                }
                                else {
                                    moveToFailScreen();
                                }
                            }
                        }
                        else {
                            Log.v(activity,"the robot is at exit position and forward direction is correct.");
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            moveToRobotSucceedScreen(robotdriver.getPathLength(),(int) robotdriver.getEnergyConsumption());
                        }
                    }
                    Log.v(activity,"end thread.");
                }
            }).start();
    }

    /**
     * The robot fails to get to exit, move to the final screen.
     */
    private void moveToFailScreen(){
        Log.v(activity,"Robot fails.");
        Intent failIntent = new Intent(PlayAnimationActivity.this, FinishActivity.class);
        failIntent.putExtra("SorF", "fail");
        startActivity(failIntent);
    }

    /**
     * The robot successfully got to exit, move to the final screen.
     */
    private void moveToRobotSucceedScreen(int pl, int ec){
        Log.v(activity,"Robot succeeds.");
        Intent successIntent = new Intent(PlayAnimationActivity.this, FinishActivity.class);
        successIntent.putExtra("SorF", "Robotsucceed");
        successIntent.putExtra("pathLength", pl);
        successIntent.putExtra("energyConsumption", ec);
        startActivity(successIntent);
    }

    /**
     * This is a shortcut to the final screen with success message.
     */
/*    private void setSuceedButton(){
        Button winButton = (Button) findViewById(R.id.shortcut_success);
        winButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toast("Suceed!");
                Intent successIntent = new Intent(PlayAnimationActivity.this, FinishActivity.class);
                successIntent.putExtra("SorF", "Robotsucceed");
                startActivity(successIntent);
            }
        });
    }*/

    /**
     * This is a shortcut to the final screen with fail message.
     */
/*    private void setFailButton(){
        Button failButton = (Button) findViewById(R.id.shortcut_failure);
        failButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toast("Fail!");
                Intent failIntent = new Intent(PlayAnimationActivity.this, FinishActivity.class);
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
//                    toast("Hide the Map");
                } else {
                    controller.keyDown(Constants.UserInput.TOGGLEFULLMAP, 1);
//                    toast("Show the Map");
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
/*                    toast("Hide the Solution");*/
                } else {
                    controller.keyDown(Constants.UserInput.TOGGLESOLUTION, 1);
//                    toast("Show the Solution");
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
//                    toast("Hide the Wall");
                } else {
                    controller.keyDown(Constants.UserInput.TOGGLELOCALMAP, 1);
//                    toast("Show the Wall");
                }
            }
        });
    }

    /**
     * pause the robotDriver if the ToggleButton is pressed.
     */
    private void pause_toggleButton(){
        ToggleButton pause_toggleButton = (ToggleButton) findViewById(R.id.pause_toggleButton);
        pause_toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    toast("Pause Animation");
                    pause = true;
                } else {
//                    toast("Start Animation");
                    pause = false;
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
        Intent backToTitle = new Intent(PlayAnimationActivity.this, AMazeActivity.class);
        startActivity(backToTitle);
    }
}