package com.example.fenix.timecounter;

/**
 * Main app activity.
 *
 * @version 1.0 25.06.2015
 * @autor Felyk Volodymyr
 */

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.preference.PreferenceManager;
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

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;


public class TimeCounter extends AppCompatActivity {
    private boolean isStarted = true;
    private Handler mHandler = new Handler();
    private Timer timer = new Timer();
    private TimerTask timerTask;
    private Chronometer mChronometer;
    private SharedPreferences defaultPreferences,prefs;
    private DatabaseHandler database;
    private Bundle mBundle;
    private Button button_start;
    private Button button_lap;
    private Button button_reset;
    private ListView listView;
    private int msecond[] = {1000, 100, 10, 1};    //select start output timer
    private int timeoutput;
    private int index = 0;
    private long lapbase = 0;                      //for saving single lap
    private long base = 0;                         //for start time
    private long stoptime = 0;                         //for saving stop time
    private ArrayAdapter<String> arrayAdapter;

    private static final int DROP = 15;
    private static final int EXPORTDB = 16;
    private static final String PREFS_FILE = "myPrefsFile";
    public static final String TAG = "Time";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timer_layout);

        mChronometer = (Chronometer) findViewById(R.id.chronometer);
        button_start = (Button) findViewById(R.id.button_start);
        button_lap = (Button) findViewById(R.id.button_lap);
        button_reset = (Button) findViewById(R.id.button_reset);
        listView = (ListView) findViewById(R.id.listView);
        arrayAdapter = new ArrayAdapter<String>(this, R.layout.item1);


        Log.d(TAG, Long.toString(Thread.currentThread().getId()) + " = Theread id");
        database = new DatabaseHandler(this);

        defaultPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        timeoutput = Integer.parseInt(defaultPreferences.getString(getString(R.string.pr_timer_output), "2"));
        chronometerInit();

        for (TimeData data : database.getLaps()) {
            index++;
            arrayAdapter.add(data.getId() + ". " + format(data.getLap_time())
                    + " - " + format(data.getAll_time()) + " ");
        }

        listView.setAdapter(arrayAdapter);




    button_start.setOnClickListener(new

    OnClickListener() {
        @Override
        public void onClick (View v){

            if (isStarted) {

                long rt = SystemClock.elapsedRealtime();
                if (stoptime == 0) {
                    mChronometer.setBase(rt);
                    base = rt;
                    lapbase = rt;
                } else {
                    base = base + (rt - stoptime);
                    lapbase = lapbase + (rt - stoptime);
                    mChronometer.setBase(base);
                }
                button_start.setText(R.string.stop);
                stoptime = 0;
                isStarted = false;
                chronometerStart();
            } else {
                stoptime = SystemClock.elapsedRealtime();
                mChronometer.getOnChronometerTickListener().onChronometerTick(mChronometer);
                chronometerStop();
                button_start.setText(R.string.start);
                isStarted = true;
            }
        }
    }

    );

    button_lap.setOnClickListener(new

    OnClickListener() {
        @Override
        public void onClick (View v){
            index++;
            long realtime = SystemClock.elapsedRealtime();
            long laptime, alltime;
            if (base != 0) {
                alltime = realtime - base;          //collective time of several laps
                if (stoptime == 0) {
                    Log.d(TAG, Long.toString(lapbase) + " = lapbase stoptime==0");
                    Log.d(TAG, Long.toString(realtime) + " = realtime stoptime==0");
                    laptime = realtime - lapbase;       //time of single lap
                    lapbase = realtime;
                    Log.d(TAG, Long.toString(laptime) + " = laptime stoptime==0");
                } else {
                    Log.d(TAG, Long.toString(lapbase) + " = lapbase stoptime!=0");

                    Log.d(TAG, Long.toString(realtime) + " = realtime stoptime!=0");
                    laptime = stoptime - lapbase;                     //time of single lap
                    lapbase = stoptime;
                    Log.d(TAG, Long.toString(laptime) + " = laptime stoptime!=0");
                }
            } else {
                laptime = 0;
                alltime = 0;                  //collective time of several laps
            }

            database.addLap(new TimeData(index, laptime, alltime));
            arrayAdapter.insert(index + ". " + format(laptime) + " - " + format(alltime), 0);

        }
    }

    );

    button_reset.setOnClickListener(new

    OnClickListener() {
        @Override
        public void onClick (View v){
            stoptime = 0;
            lapbase = 0;
            base = 0;
            isStarted = true;
            chronometerStop();
            button_start.setText(R.string.start);
            mChronometer.setText(getResources().getStringArray(R.array.timer_output)[timeoutput]);
            Log.d(TAG, Long.toString(Thread.currentThread().getId()) + "Curent thread");
        }
    }

    );


    mChronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener()

    {
        @Override
        public void onChronometerTick (Chronometer cArg){
        long time = SystemClock.elapsedRealtime() - cArg.getBase();
            cArg.setText(format(time));

    }
    }

    );
}

    @Override
    protected void onStart(){
        super.onStart();
        Log.d(TAG, "onStart");

    }

    @Override
    protected void onResume() {
        Log.d(TAG, "On resume");
        super.onResume();
        prefs = getSharedPreferences(PREFS_FILE,0);
        timeoutput = Integer.parseInt(defaultPreferences.getString(getString(R.string.pr_timer_output), "2"));
        prefs = getSharedPreferences(PREFS_FILE,0);
        mChronometer.setText(getResources().getStringArray(R.array.timer_output)[timeoutput]);

        lapbase = prefs.getLong(getResources().getString(R.string.bundle_lapBaseTime),0);
        base = prefs.getLong(getResources().getString(R.string.bundle_baseTime),0);
        stoptime = prefs.getLong(getResources().getString(R.string.bundle_stopTime), 0);
        isStarted = prefs.getBoolean(getResources().getString(R.string.bundle_isStarted), true);
        if (base != 0) {

            if (stoptime == 0) {
                mChronometer.setBase(base);
            } else {
                base = base + (SystemClock.elapsedRealtime() - stoptime);
                mChronometer.setBase(base);
                lapbase = lapbase + (SystemClock.elapsedRealtime() - stoptime);
                mChronometer.getOnChronometerTickListener().onChronometerTick(mChronometer);
            }
            if (isStarted == false && (stoptime == 0)) {
                chronometerStart();
                button_start.setText(R.string.stop);
            }
        }


    }

    @Override
    protected void onPause() {
        Log.d(TAG, "oaPause");
        super.onPause();
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putLong(getResources().getString(R.string.bundle_lapBaseTime), lapbase);
        prefsEditor.putLong(getResources().getString(R.string.bundle_baseTime), base);
        prefsEditor.putLong(getResources().getString(R.string.bundle_stopTime), stoptime);
        prefsEditor.putBoolean(getResources().getString(R.string.bundle_isStarted), isStarted);
        prefsEditor.commit();

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "onSave...");
        super.onSaveInstanceState(outState);
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menu.add(0, EXPORTDB, 0, "Копіювати базу даних");
        menu.add(0, DROP, 0, "Видалити таблицю");

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
        switch (id) {
            case R.id.action_settings:
                Intent intent = new Intent();
                intent.setClass(this, PreferencesActivity.class);
                startActivity(intent);
                break;
            case EXPORTDB:
                database.exportDB();
                break;
            case DROP:
                database.drop();
                break;
            default:
                return false;
        }

        return super.onOptionsItemSelected(item);
    }

    /* Used for formatting chronometer output */
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
        switch (msecond[timeoutput]) {
            case 1:             // for time output 00:00:00.000
                ms = (int) (time - h * 3600000 - m * 60000 - s * 1000) / msecond[timeoutput];
                mS = ms < 10 ? "00" + ms : (ms < 100 ? "0" + ms : ms + "");
                cArg = (hh + ":" + mm + ":" + ss + "." + mS);
                break;
            case 10:            // for time output 00:00:00.00
                ms = (int) (time - h * 3600000 - m * 60000 - s * 1000) / msecond[timeoutput];
                mS = ms < 10 ? "0" + ms : ms + "";
                cArg = (hh + ":" + mm + ":" + ss + "." + mS);
                break;
            case 100:           // for time output 00:00:00.0
                ms = (int) (time - h * 3600000 - m * 60000 - s * 1000) / msecond[timeoutput];
                mS = ms + "";
                cArg = (hh + ":" + mm + ":" + ss + "." + mS);
                break;
            default:            // for time output 00:00:00
                cArg = (hh + ":" + mm + ":" + ss);
                break;
        }
        return cArg;
    }


    /** Method used for initialisation (TimerTask)timerTask
     *
     *  Without using this metod befor metods chronometerStart() or chronometerStop
     *  they throws java.lang.NullPointerException
     *  Methods chronometerStart(), chronometerStop, chronometerInit() made for
     *  readability and ease of use encapsulated methods
     */
    private void chronometerInit() {
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
    };

    /**
     * Method for start timer
     * used after chronometerInit()
     * */
    private void chronometerStart() {
        chronometerInit();
        mChronometer.start();
        timer.schedule(timerTask, 0L, 23L);
    };

    /**
     * Method for stop timer
     * used after chronometerInit()
     */
    private void chronometerStop() {
        timerTask.cancel();

               if(stoptime==0) mHandler.postDelayed((new Runnable() {
                    @Override
                    public void run() {
                        mChronometer.setText(getResources().getStringArray(R.array.timer_output)[timeoutput]);
                    }
                }),30L);

        mChronometer.stop();

    };


}

