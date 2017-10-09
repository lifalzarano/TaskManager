package com.task.Persistence;

import io.realm.RealmObject;

/**
 * Created by laurenfalzarano on 4/9/17.
 */

public class Task extends RealmObject {
    private String name;
    private int state;
    private long date;
    private long time;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
