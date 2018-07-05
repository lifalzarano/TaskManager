package com.task.Persistence;

import java.util.List;
import java.util.UUID;

import io.realm.Case;
import io.realm.Realm;

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
        realm = Realm.getDefaultInstance();
    }

    public void saveTask(Task task) {
        try {
            if (task.getTaskId() == null) {
                task.setTaskId(UUID.randomUUID().toString());
            }

            realm.beginTransaction();
            realm.copyToRealmOrUpdate(task);
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

    public Task getTask(String taskId) {
        Task originalTask = realm.where(Task.class)
                .equalTo("taskId", taskId)
                .findFirst();

        // Deep copy because you can't edit raw Realm objects outside of a realm transaction
        Task newTask = new Task();
        newTask.setTaskId(originalTask.getTaskId());
        newTask.setName(originalTask.getName());
        newTask.setState(originalTask.getState());
        newTask.setDate(originalTask.getDate());
        newTask.setTime(originalTask.getTime());
        return newTask;
    }
}
