package edu.wm.cs.cs301.wenxindang.gui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import edu.wm.cs.cs301.wenxindang.R;
import edu.wm.cs.cs301.wenxindang.generation.SingleRandom;

/**
 * This Activity collects parameters for setup,
 * and will send parameters to GeneratingActivity.
 *
 * @author Wenxin Dang
 */
public class AMazeActivity extends AppCompatActivity {

    private String activity = "AMazeActivity";
    private String driver;
    private String builder;
    private boolean isPerfect;
    private int skillLevel;
    private int seed;
//    private TextView txt_cur;
    private Context mContext;
    private int builderCode;
    private int RoomCode;

    /**
     * Initialize the screen displayed.
     * @param savedInstanceState provides a bundle that is most recently saved.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amaze);
        mContext = AMazeActivity.this;
        driver = "Manually";
        builder = "DFS";
        isPerfect = false;
        skillLevel = 0;
        seed = 13;
        builderCode = 1000;
        RoomCode = 0;
        setSkillLevel();
        setBuilder();
        setDriver();
        setRoom();
        Explore();
        Revisit();
        /**
         * Use this line to clear the SharedPreferences:
         */
//        clearSharedPreferences();
    }

    /**
     * set up the SkillLevel with user's input.
     */
    private void setSkillLevel(){
        SeekBar skillLevel_SeekBar = (SeekBar) findViewById(R.id.skillLevel_seekBar);
//        toast("Skill Level is: " + skillLevel_SeekBar.getProgress());
        skillLevel_SeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress = 0;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                this.progress = progress;
                skillLevel = this.progress;
//                toast("Skill Level Selected is: " + skillLevel);
//                txt_cur.setText("Skill Level Selected is: " + skillLevel);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                /*toast("Selecting a skill level.");*/
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                toast("Skill level of "+progress+" is selected.");
            }
        });
    }

    /**
     * set up the Builder with user's input.
     */
    private void setBuilder(){
        Spinner builder_spinner = (Spinner) findViewById(R.id.builder_spinner);
        ArrayAdapter<CharSequence> builder_adapter = ArrayAdapter.createFromResource(this, R.array.builderchoice, android.R.layout.simple_spinner_item);
        builder_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        builder_spinner.setAdapter(builder_adapter);
        builder_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                builder = parent.getItemAtPosition(position).toString();
                builderCode = (position+1)*1000;
                ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
//                toast("The selected builder is: "+builder);
                Log.v(activity,"the builderCode is: "+Integer.toString(builderCode));
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        }

    /**
     * set up the Driver with user's input.
     */
    private void setDriver(){
        Spinner driver_spinner = (Spinner) findViewById(R.id.playing_spinner);
        ArrayAdapter<CharSequence> driver_adapter = ArrayAdapter.createFromResource(this, R.array.mode, android.R.layout.simple_spinner_item);
        driver_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        driver_spinner.setAdapter(driver_adapter);
        driver_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                driver = parent.getItemAtPosition(position).toString();
                ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
//                toast("The selected playing mode is: "+driver);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    /**
     * set up the isPerfect or not with user's input.
     */
    private void setRoom(){
        Spinner room_spinner = (Spinner) findViewById(R.id.rooms_spinner);
        ArrayAdapter<CharSequence> room_adapter = ArrayAdapter.createFromResource(this, R.array.room, android.R.layout.simple_spinner_item);
        room_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        room_spinner.setAdapter(room_adapter);
        room_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (parent.getItemAtPosition(position).toString()){
                    case "Yes": {
                        isPerfect = false;
                        RoomCode = 0;
//                        toast("Yes, I want rooms in the maze.");
                        Log.v(activity,"the RoomCode is: "+Integer.toString(RoomCode));
                        break;
                    }
                    case "No": {
                        isPerfect = true;
                        RoomCode = 1;
//                        toast("No, I want a perfect maze without rooms");
                        Log.v(activity,"the RoomCode is: "+Integer.toString(RoomCode));
                        break;
                    }
                }
                ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    /**
     * Start the game with a new seed.
     */
    private void Explore(){
        Button exploreButton = (Button) findViewById(R.id.explore_button);
        exploreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                toast("Start a NEW Game!");
                Intent exploreIntent = new Intent(AMazeActivity.this, GeneratingActivity.class);
                exploreIntent.putExtra("skillLevel", skillLevel);
                exploreIntent.putExtra("builder", builder);
                exploreIntent.putExtra("driver", driver);
                exploreIntent.putExtra("isPerfect", isPerfect);
                // set seed:
                SingleRandom sr = SingleRandom.getRandom();
                seed = sr.nextInt();
                Log.v(activity,"the seed is: " + seed);
                // put into SharedPreferences:
                Log.v(activity,"try to put the seed into SharedPreferences: ");
                SharedPreferences sp = getSharedPreferences("file", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                String code = getSharedPreferencesCode(skillLevel, builder, isPerfect);
                Log.v(activity,"the SharedPreferences code is: " + code);
                editor.putInt(code, seed);
                editor.commit();
                Log.v(activity,"successfully put the seed into SharedPreferences.");
                //continue to putExtra:
                exploreIntent.putExtra("seed", seed);
                /*exploreIntent.putExtra("RevisitORExplore","Explore");*/
                startActivity(exploreIntent);
            }
        });
    }

    /**
     * Get a key to store information in the SharedPreferences.
     * @param sk the skillLevel
     * @param bui the builderCode
     * @param p the RoomCode
     * @return a key
     */
    private String getSharedPreferencesCode(int sk, String bui, boolean p){
        int sum = skillLevel*10 + builderCode + RoomCode;
        return Integer.toString(sum);
    }
    /**
     * Read parameters (including the seed) from the file.
     */
    private void Revisit(){
        Button exploreButton = (Button) findViewById(R.id.revisit_button);
        exploreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sp = getSharedPreferences("file", Context.MODE_PRIVATE);
                String code = getSharedPreferencesCode(skillLevel, builder, isPerfect);
                if (sp.contains(code)){
                    // Revisit:
                    toast("Revisit an OLD Game!");
                    seed = sp.getInt(code, 13);
                    Log.v(activity,"Retrieved seed is: "+ seed);
                    Intent revisitIntent = new Intent(AMazeActivity.this, GeneratingActivity.class);
                    revisitIntent.putExtra("skillLevel", skillLevel);
                    revisitIntent.putExtra("builder", builder);
                    revisitIntent.putExtra("driver", driver);
                    revisitIntent.putExtra("isPerfect", isPerfect);
                    revisitIntent.putExtra("seed", seed);
                    revisitIntent.putExtra("RevisitORExplore","Revisit");
                    startActivity(revisitIntent);
                }
//                else{
//                    // Announce: Cannot revisit, please explore.
//                    toast("This maze is not played before, CANNOT revisit. Please use Explore to start a new game!");
//                }
            }
        });
    }

    public void clearSharedPreferences(){
        SharedPreferences sp = getSharedPreferences("file", Context.MODE_PRIVATE);
        sp.edit().clear().commit();
        Log.v(activity,"Cleared the SharedPreferences");
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
     * move the app to the back.
     */
    @Override
    public void onBackPressed() {
        toast("Back is Pressed.");
        moveTaskToBack(true);
    }

}