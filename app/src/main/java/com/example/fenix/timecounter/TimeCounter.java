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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ListView;
import android.content.Context;

import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;


public class TimeCounter extends AppCompatActivity {
    private boolean isStarted = true;
    private Handler mHandler = new Handler();
    private Timer timer = new Timer();
    private TimerTask timerTask;
    private static final String TAG = "Time";
    private int msecond[] = {1000, 100, 10, 1};    //select start output timer
    private int timeindex = 2;
    private int index=1;
    private long Base = 0;
    //private LinkedList<String> linkedList = new LinkedList<>();
    private ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timer_layout);

        final Chronometer mChronometer = (Chronometer) findViewById(R.id.chronometer);
        final Button button_start = (Button) findViewById(R.id.button_start);
        final Button button_lap = (Button) findViewById(R.id.button_lap);
        final Button button_reset = (Button) findViewById(R.id.button_reset);
        final ListView listView = (ListView) findViewById(R.id.listView);
        arrayAdapter = new ArrayAdapter<String>(this, R.layout.item1);




        Log.d(TAG, Long.toString(Thread.currentThread().getId()));


        listView.setAdapter(arrayAdapter);
        //setContentView(listView);
        mChronometer.setText(getResources().getStringArray(R.array.timer_output)[timeindex]);


        button_start.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isStarted) {
                    if (Base == 0)
                        mChronometer.setBase(SystemClock.elapsedRealtime());
                    else mChronometer.setBase(Base);  // DEBUG comment
                    mChronometer.start();
                    button_start.setText(R.string.stop);

                    timerTask = new TimerTask() {
                        @Override
                        public void run() {
                            mHandler.post((new Runnable() {
                                @Override
                                public void run() {
                                    mChronometer.getOnChronometerTickListener().
                                            onChronometerTick(mChronometer);
                                }
                            }));

                        }
                    };
                    timer.schedule(timerTask, 10L, 30L);
                    isStarted = false;
                } else {
                    Base = mChronometer.getBase();
                    mChronometer.getOnChronometerTickListener().onChronometerTick(mChronometer);
                    mChronometer.stop();
                    timerTask.cancel();
                    button_start.setText(R.string.start);
                    isStarted = true;
                }
            }
        });

        button_lap.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                arrayAdapter.insert(index+".  "+mChronometer.getText().toString(),0);
                index++;

                Log.d(TAG, Long.toString(Thread.currentThread().getId()));
                }
        });

        button_reset.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Base = 0;
                mChronometer.setBase(SystemClock.elapsedRealtime());
                mChronometer.setText(getResources().getStringArray(R.array.timer_output)[timeindex]);
                Log.d(TAG, Long.toString(Thread.currentThread().getId()));
            }
        });


        mChronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer cArg) {
                long time = SystemClock.elapsedRealtime() - cArg.getBase();
                cArg.setText(format(time));
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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

    private String format(long time) {
        String cArg;
        int h = (int) (time / 3600000);
        int m = (int) (time - h * 3600000) / 60000;
        int s = (int) (time - h * 3600000 - m * 60000) / 1000;
        int ms;
        String hh = h < 10 ? "0" + h : h + "";
        String mm = m < 10 ? "0" + m : m + "";
        String ss = s < 10 ? "0" + s : s + "";
        String mS;
        switch (msecond[timeindex]) {
            case 1:
                ms = (int) (time - h * 3600000 - m * 60000 - s * 1000) / msecond[timeindex];
                mS = ms < 10 ? "00" + ms : (ms < 100 ? "0" + ms : ms + "");  // for time output 00:00:00.000
                cArg = (hh + ":" + mm + ":" + ss + "." + mS);
                break;
            case 10:
                ms = (int) (time - h * 3600000 - m * 60000 - s * 1000) / msecond[timeindex];
                mS = ms < 10 ? "0" + ms : ms + "";  // for time output 00:00:00.00
                cArg = (hh + ":" + mm + ":" + ss + "." + mS);
                break;
            case 100:
                ms = (int) (time - h * 3600000 - m * 60000 - s * 1000) / msecond[timeindex];
                mS = ms < 10 ? "00" + ms : (ms < 100 ? "0" + ms : ms + "");  // for time output 00:00:00.0
                cArg = (hh + ":" + mm + ":" + ss + "." + mS);
                break;
            default:
                cArg = (hh + ":" + mm + ":" + ss);
                break;
        }
        return cArg;
    }
}

