package com.task.Persistence;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by laurenfalzarano on 9/2/17.
 */

public class TaskDO implements Parcelable {
    private String name;
    private int state;

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

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(name);
        out.writeInt(state);
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
    }

    public TaskDO (Task task) {
        this.name = task.getName();
        this.state = task.getState();
    }
}
