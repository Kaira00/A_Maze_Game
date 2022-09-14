package edu.wm.cs.cs301.wenxindang.gui;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import edu.wm.cs.cs301.wenxindang.R;
import edu.wm.cs.cs301.wenxindang.generation.Constants;
import edu.wm.cs.cs301.wenxindang.generation.DataHolder;
import edu.wm.cs.cs301.wenxindang.generation.MazePanel;

/**
 * This Activity generates a maze based on received parameters.
 *
 * @author Wenxin Dang
 */
public class GeneratingActivity extends AppCompatActivity {

    private String activity = "GeneratingActivity";
    private String driver;
    private String builder;
    private boolean isPerfect;
    private int skillLevel;
    private int seed;
    private DataHolder cotroller;
    //    private TextView txt_cur;
    private Context mContext;
    private boolean backPressed = false;
    private ProgressBar progressBar;
    private TextView progress;
    private int recordprogress;
    private DataHolder controller;

    /**
     * Initialize the screen displayed.
     * @param savedInstanceState provides a bundle that is most recently saved.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generating);
        mContext = GeneratingActivity.this;
        backPressed = false;
        Intent intentFromTitle = getIntent();
        skillLevel = intentFromTitle.getIntExtra("skillLevel", 0);
        Log.v(activity,"received skillLevel: "+skillLevel);
        seed = intentFromTitle.getIntExtra("seed", 13);
        Log.v(activity,"received seed: "+seed);
        builder = intentFromTitle.getStringExtra("builder");
        Log.v(activity,"received builder: "+builder);
        driver = intentFromTitle.getStringExtra("driver");
        Log.v(activity,"received driver: "+driver);
        isPerfect = intentFromTitle.getBooleanExtra("isPerfect",false);
        Log.v(activity,"received isPerfect: "+isPerfect);
        startThread(driver);
    }

    /**
     * Generate a maze on a background thread.
     * and display the progress on UI.
     * @param driver gives the driver selected in AMazeActivity.
     */
    private void startThread(String driver){
        Handler handlethread = new Handler(Looper.myLooper());
        progressBar = (ProgressBar) findViewById(R.id.generating_progressBar);
        progress = (TextView) findViewById(R.id.progress_textView);
        recordprogress = 0;
        progressBar.setMax(100);
        progressBar.setProgress(0);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.v(activity,"Start thread:");
                controller = controller.getInstance();
                Log.v(activity,"Got the controller");
                MazePanel panel = new MazePanel(getApplicationContext());
                controller.getController().setMazePanel(panel);
                controller.setRobotAndDriver(driver);
                controller.setBuilder(builder);
                controller.setskillLevel(skillLevel);
                controller.setPerfect(isPerfect);
                controller.setSeed(seed);
                controller.setState(Constants.State.Generating);
                controller.getController().startGenerating();
                Log.v(activity,"Set the controller");
                while (recordprogress<100){
                    if (backPressed){
                        /*Thread.interrupted();*/
                        break;
                    }
                    recordprogress = controller.getController().getPercentDone();
                    handlethread.post(new Runnable() {
                        @Override
                        public void run() {
                            progress.setText(Integer.toString(recordprogress));
                            progressBar.setProgress(recordprogress);
                        }
                    });
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (!backPressed) {
                    moveToPlay(driver);
                }
                Log.v(activity,"end thread.");
            }
        }).start();
    }

    /**
     * Decide whether jump to PlayManuallyActivity or PlayAnimationActivity
     * based on the driver selected in AMazeActivity.
     * @param driver gives the driver selected in AMazeActivity.
     */
    private void moveToPlay(String driver){
        if (driver.equals("animation: Wizard")){
            Log.v(activity,"move to Animation with Wizard");
            Intent Wintent = new Intent(GeneratingActivity.this, PlayAnimationActivity.class);
            Wintent.putExtra("driver", "Wizard");
            startActivity(Wintent);
        }
        else if (driver.equals("animation: WallFollower")){
            Log.v(activity,"move to Animation with WallFollower");
            Intent WFintent = new Intent(GeneratingActivity.this, PlayAnimationActivity.class);
            WFintent.putExtra("driver", "WallFollower");
            startActivity(WFintent);
        }
        else{
            Log.v(activity,"move to Manual.");
            Intent manualintent = new Intent(GeneratingActivity.this, PlayManuallyActivity.class);
            startActivity(manualintent);
        }
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
        Intent backToTitle = new Intent(GeneratingActivity.this, AMazeActivity.class);
        startActivity(backToTitle);
    }

}
