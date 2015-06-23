package com.example.fenix.timecounter;

/**
 * File used to store time values.
 * Have full set of constructors and getters and setters.
 *
 * Created by fenix on 23.06.2015.
 * autor Volodymyr Felyk
 */
public class TimeData {

    private int id;
    private int number;
    private long lap_time;
    private long all_time;

    public TimeData() {
    }

    public TimeData(int number, long lap_time, long all_time) {
        this.number = number;
        this.lap_time = lap_time;
        this.all_time = all_time;
    }

    public TimeData(int id,int number, long lap_time, long alltime) {
        this(number,lap_time,alltime);
        this.id = id;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public long getLap_time() {
        return lap_time;
    }

    public void setLap_time(long lap_time) {
        this.lap_time = lap_time;
    }

    public long getAll_time() {
        return all_time;
    }

    public void setAll_time(long all_time) {
        this.all_time = all_time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
