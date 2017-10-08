package com.task.Utils;

import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;

/**
 * Created by laurenfalzarano on 5/24/17.
 */

public class FormUtils {
    public static void showSnackbar(View parent, String message, int backgroundColor, int textColor) {
        Snackbar snackbar = Snackbar.make(parent, message, Snackbar.LENGTH_LONG);
        View rootView = snackbar.getView();
        rootView.setBackgroundColor(backgroundColor);
        TextView tv = (TextView) rootView.findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(textColor);
        snackbar.show();
    }

    public static void showBlackSnackbar(View parent, String message) {
        Snackbar.make(parent, message, Snackbar.LENGTH_LONG).show();
    }
}
