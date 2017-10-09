package com.task.Persistence;

import android.os.Parcel;
import android.os.Parcelable;

import com.task.Application.MyApplication;
import com.task.R;

import java.text.DateFormat;
import java.util.Date;

/**
 * Created by laurenfalzarano on 9/2/17.
 */

public class TaskDO implements Parcelable {
    public final static int CREATED = 0;
    public final static int IN_PROGRESS = 1;
    public final static int DONE = 2;

    private String name;
    private int state;
    private long dateUnix;
    private int timeUnix;

    public TaskDO() {}

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

    public String getStateString() {
        switch (state) {
            case CREATED:
                return MyApplication.getAppContext().getString(R.string.not_started);
            case IN_PROGRESS:
                return MyApplication.getAppContext().getString(R.string.in_progress);
            case DONE:
                return MyApplication.getAppContext().getString(R.string.completed);
            default:
                return MyApplication.getAppContext().getString(R.string.not_started);
        }
    }

    public String getFormattedDate() {
        return new Date(dateUnix).toString();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(name);
        out.writeInt(state);
        out.writeLong(dateUnix);
    }

    public static final Parcelable.Creator<TaskDO> CREATOR
            = new Parcelable.Creator<TaskDO>() {
        public TaskDO createFromParcel(Parcel in) {
            return new TaskDO(in);
        }

        public TaskDO[] newArray(int size) {
            return new TaskDO[size];
        }
    };

    private TaskDO(Parcel in) {
        name = in.readString();
        state = in.readInt();
        dateUnix = in.readLong();
    }

    public TaskDO (Task task) {
        this.name = task.getName();
        this.state = task.getState();
        this.dateUnix = task.getDate();
    }
}
