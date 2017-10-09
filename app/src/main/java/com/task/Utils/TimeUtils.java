package com.task.Utils;

import com.task.Persistence.Task;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class TimeUtils {

    private static final String DATE_FORMAT = "EEEE, MMMM d, yyyy";
    private static final String TIME_FORMAT = "h:mm a";

    public static String getDateText(long unixTime) {
        Date date = new Date(unixTime);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
        simpleDateFormat.setTimeZone(TimeZone.getDefault());
        return simpleDateFormat.format(date);
    }

    public static String getTimeText(long unixTime) {
        Date date = new Date(unixTime);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(TIME_FORMAT, Locale.getDefault());
        simpleDateFormat.setTimeZone(TimeZone.getDefault());
        return simpleDateFormat.format(date);
    }

    public static String getDateAndTime(Task task) {
        StringBuilder dueText = new StringBuilder();
        if (task.getDate() > 0L) {
            dueText.append(getDateText(task.getDate()));
        }
        if (task.getTime() > 0L) {
            dueText.append(" - ");
            dueText.append(getTimeText(task.getTime()));
        }
        return dueText.toString();
    }
}
