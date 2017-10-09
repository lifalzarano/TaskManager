package com.task.Activities;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import com.task.Persistence.DatabaseManager;
import com.task.Persistence.Task;
import com.task.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by laurenfalzarano on 10/8/17.
 */

public class TaskViewActivity extends StandardActivity {
    public final static String TASK_ID_KEY = "taskId";

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.title) TextView title;

    private Task task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_task_activity);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String taskId = getIntent().getStringExtra(TASK_ID_KEY);
        task = DatabaseManager.get().getTask(taskId);
        Log.d("Pusheen", "task: " + task);

        title.setText(task.getName());
    }
}
