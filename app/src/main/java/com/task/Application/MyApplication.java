package com.task.Application;

import android.app.Application;
import android.content.Context;

import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.IoniconsModule;

import io.realm.Realm;

/**
 * Created by laurenfalzarano on 4/9/17.
 */

public class MyApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        Iconify.with(new IoniconsModule());
        Realm.init(this);
    }

    public static Context getAppContext() {
        return context;
    }

}
