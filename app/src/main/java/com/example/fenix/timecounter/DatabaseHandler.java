package com.example.fenix.timecounter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.LinkedList;

/**
 * File used for handle SQLite database
 *
 * Created by fenix on 23.06.2015.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "timeManager.db";
    private static final String TABLE_LAPS = "timelaps";
    private static final String KEY_ID = "id";                  //primary key
    private static final String KEY_NUMBER = "lap_number";      //not null, unique
    private static final String KEY_LAPTIME = "lap_time";
    private static final String KEY_ALLTIME = "all_time";
    private Context context;


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            String CREATE_TIME_TABLE = "CREATE TABLE " + TABLE_LAPS + "("
                    + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NUMBER + " INTEGER,"
                    + KEY_LAPTIME + " INTEGER," + KEY_ALLTIME + " INTEGER" + ")";
            db.execSQL(CREATE_TIME_TABLE);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.execSQL("DROP TABLE IF EXISTS" + TABLE_LAPS);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void drop() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE " + TABLE_LAPS);
        onCreate(db);
        db.close();
    }

    public void addLap(TimeData data) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_LAPTIME, data.getLap_time());
        values.put(KEY_ALLTIME, data.getAll_time());
        values.put(KEY_NUMBER, data.getNumber());
        db.insert(TABLE_LAPS, null, values);
        Log.d(TimeCounter.TAG, db.getMaximumSize() + " = DatabaseHandler");
        db.close();
    }

    public TimeData getLap(int id) {
        TimeData data = new TimeData();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_LAPS, new String[]{KEY_NUMBER, KEY_LAPTIME, KEY_ALLTIME},
                KEY_ID + "=?", new String[]{String.valueOf(id)}, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            data = new TimeData(cursor.getInt(0), cursor.getLong(1), cursor.getLong(2));
        }
        return data;
    }

    /**
     * used for gettingt all data from database
     */
    public LinkedList<TimeData> getLaps() {
        LinkedList<TimeData> linkedList = new LinkedList<TimeData>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_LAPS, new String[]{KEY_ID, KEY_NUMBER, KEY_LAPTIME, KEY_ALLTIME},
                null, null, null, null, KEY_ID + " DESC");
        if (cursor.moveToFirst()) {
            do{
                linkedList.add(new TimeData(cursor.getInt(0), cursor.getInt(1), cursor.getLong(2), cursor.getLong(3)));
            }while (cursor.moveToNext());
        }
        db.close();
        return linkedList;
    }


    /**
     * metod for copy database to SDCard of phone or tab
     */
    public void exportDB() {
        File sd = Environment.getExternalStorageDirectory();
        File data = Environment.getDataDirectory();
        FileChannel source = null;
        FileChannel destination = null;
        String currentDBPath = "/data/" + "com.example.fenix.timecounter" + "/databases/" + DATABASE_NAME;
        String backupDBPath = DATABASE_NAME;
        File currentDB = new File(data, currentDBPath);
        File backupDB = new File(sd, backupDBPath);
        try {
            source = new FileInputStream(currentDB).getChannel();
            destination = new FileOutputStream(backupDB).getChannel();
            destination.transferFrom(source, 0, source.size());
            source.close();
            destination.close();
            Toast.makeText(context, "DB Exported!", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
