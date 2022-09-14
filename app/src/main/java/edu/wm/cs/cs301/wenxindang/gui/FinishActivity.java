package edu.wm.cs.cs301.wenxindang.gui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import edu.wm.cs.cs301.wenxindang.R;

/**
 * This Activity decides the text to show on the screen based on received message.
 * and restores the seed and other parameters’ value related to the maze in a file.
 *
 * @author Wenxin Dang
 */
public class FinishActivity extends AppCompatActivity {

    private String result;
    private TextView line1;
    private TextView line2;
    private TextView pl;
    private TextView plnum;
    private TextView re;
    private TextView renum;
    public String activity = "FinishActivity";
    private Context mContext;
    private Vibrator myVibrator;
    private boolean backPressed = false;
    private int pathLength;
    private int energyConsumption;

    /**
     * Initialize the screen displayed.
     * @param savedInstanceState provides a bundle that is most recently saved.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);
        myVibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
        mContext = FinishActivity.this;
        backPressed = false;
        Intent intentFromPlay = getIntent();
        result = intentFromPlay.getStringExtra("SorF");
        pathLength = intentFromPlay.getIntExtra("pathLength",0);
        energyConsumption = intentFromPlay.getIntExtra("energyConsumption",0);
        if (result.equals("succeed")){
           line1 = (TextView) findViewById(R.id.Won_text);
           line1.setText("You Won!");
           line2 = (TextView) findViewById(R.id.Cong_text);
           line2.setText("Congratulations!");
           /*pl = (TextView) findViewById(R.id.pl_textView);
           pl.setText("");*/
           plnum = (TextView) findViewById(R.id.plnum_textView);
           plnum.setText(Integer.toString(pathLength));
           re = (TextView) findViewById(R.id.re_textView);
           re.setText("");
           renum = (TextView) findViewById(R.id.renum_textView);
           renum.setText("");
        }
        else if (result.equals("fail")){
            line1 = (TextView) findViewById(R.id.Won_text);
            line1.setText("Sorry, you lost");
            line2 = (TextView) findViewById(R.id.Cong_text);
            line2.setText("");
            pl = (TextView) findViewById(R.id.pl_textView);
            pl.setText("");
            plnum = (TextView) findViewById(R.id.plnum_textView);
            plnum.setText("");
            re = (TextView) findViewById(R.id.re_textView);
            re.setText("");
            renum = (TextView) findViewById(R.id.renum_textView);
            renum.setText("");
        }
        else if (result.equals("Robotsucceed")){
            plnum = (TextView) findViewById(R.id.plnum_textView);
            plnum.setText(Integer.toString(pathLength));
            renum = (TextView) findViewById(R.id.renum_textView);
            renum.setText(Integer.toString(energyConsumption));
        }
        setRestartButton();
    }

    /**
     * Return to AMazeActivity if the Button is pressed.
     */
    private void setRestartButton(){
        Button restart_button = (Button) findViewById(R.id.restart_button);
        restart_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                toast("restart");
                Intent intentToTitle = new Intent(FinishActivity.this, AMazeActivity.class);
//                send relative info (seed, builder, ...) back to AMAZE;
//                intentToTitle.putExtra("seed",seed);
                startActivity(intentToTitle);
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
        Intent backToTitle = new Intent(FinishActivity.this, AMazeActivity.class);
        startActivity(backToTitle);
    }

}