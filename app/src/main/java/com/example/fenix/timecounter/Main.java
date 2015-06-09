package com.example.fenix.timecounter;


import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Chronometer;
import java.util.Timer;
import java.util.TimerTask;


public class Main extends AppCompatActivity {
    private  boolean isStarted = true;
    private Handler mHandler = new Handler();
    private Timer timer = new Timer();
    private TimerTask timerTask;
    private Chronometer mChronometer;
    private static final String TAG="Time";
    private int second = 0; //select start output timer


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timer_layout);

        mChronometer = (Chronometer) findViewById(R.id.chronometer);
        final Button button_start = (Button) findViewById(R.id.button_start);
        final Button button_lap = (Button) findViewById(R.id.button_lap);
        final Button button_reset = (Button) findViewById(R.id.button_reset);
        mChronometer.setText(getResources().getStringArray(R.array.timer_output)[0]);




         // output seconds possibility{(1000,10),(100,100),(10,1000),(1,10000)}


        //mChronometer.


        long Base;





        button_start.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isStarted) {
                    mChronometer.setBase(SystemClock.elapsedRealtime());
                    mChronometer.start();
                    button_start.setText(R.string.stop);

                    timerTask=new TimerTask() {
                        @Override
                        public void run() {
                            mHandler.postDelayed((new Runnable() {
                                @Override
                                public void run() {
                                    mChronometer.getOnChronometerTickListener().onChronometerTick(mChronometer);
                                }
                            }), 1L);

                        }};
                        timer.schedule(timerTask, 1L, 1L);
                    isStarted=false;
                }else{
                    mChronometer.stop();
                    timerTask.cancel();
                    button_start.setText(R.string.start);
                    isStarted=true;
                }
            }
        });

        button_lap.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mChronometer.stop();
            }
        });

        button_reset.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mChronometer.setBase(SystemClock.elapsedRealtime());
                mChronometer.setText(getResources().getStringArray(R.array.timer_output)[second]);
                }
        });


        mChronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer cArg) {
                long time = SystemClock.elapsedRealtime() - cArg.getBase();
                int h = (int) (time / 3600000);
                int m = (int) (time - h * 3600000) / 60000;
                int s = (int) (time - h * 3600000 - m * 60000) / 1000;
                int ms = (int)(time - h * 3600000 - m * 60000 - s * 1000);
                String hh = h < 10 ? "0" + h : h + "";
                String mm = m < 10 ? "0" + m : m + "";
                String ss = s < 10 ? "0" + s : s + "";
                String mS = ms < 10 ? "00"+ ms: (ms < 100 ? "0" + ms:ms + "" );
                //cArg.setText(hh + ":" + mm + ":" + ss);
                cArg.setText(hh + ":" + mm + ":" + ss + "." + mS);
            }
        });
    }


        @Override
        public boolean onCreateOptionsMenu (Menu menu){
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.menu_main, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected (MenuItem item){
            // Handle action bar item clicks here. The action bar will
            // automatically handle clicks on the Home/Up button, so long
            // as you specify a parent activity in AndroidManifest.xml.
            int id = item.getItemId();

            //noinspection SimplifiableIfStatement
            if (id == R.id.action_settings) {
                return true;
            }

            return super.onOptionsItemSelected(item);
        }
    }

