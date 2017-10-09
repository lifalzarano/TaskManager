package com.task.Persistence;

import com.task.Application.MyApplication;

import java.util.List;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by laurenfalzarano on 4/9/17.
 */

public class DatabaseManager {
    private static DatabaseManager instance;
    private Realm realm;

    private static final int CURRENT_REALM_VERSION = 1;

    public static DatabaseManager get() {
        if (instance == null) {
            instance = getSync();
        }
        return instance;
    }

    private static synchronized DatabaseManager getSync() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    private DatabaseManager() {
        RealmConfiguration realmConfig = new RealmConfiguration.Builder(MyApplication.getAppContext())
                .schemaVersion(CURRENT_REALM_VERSION)
                .build();
        realm = Realm.getInstance(MyApplication.getAppContext());
        realmConfig.getSchemaVersion();
    }

    public void saveTask(Task task) {
        try {
            realm.beginTransaction();
            realm.copyToRealm(task);
            realm.commitTransaction();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Task> getTasks(String query) {
        return realm.where(Task.class)
                .contains("name", query, Case.INSENSITIVE)
                .findAll();
    }

    public int getNumTasks() {
        return realm.where(Task.class)
                .findAll()
                .size();
    }
}
