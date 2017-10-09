package com.task.Utils;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;

import com.task.Application.MyApplication;
import com.task.R;

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
        Context context = MyApplication.getAppContext();
        int black = context.getResources().getColor(R.color.gray_900);
        showSnackbar(parent, message, black, Color.WHITE);
    }

    public static void showBlackSnackbar(View parent, @StringRes int resId) {
        showBlackSnackbar(parent, MyApplication.getAppContext().getString(resId));
    }
}
