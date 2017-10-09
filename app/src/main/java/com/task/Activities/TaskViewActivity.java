package com.task.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.IoniconsIcons;
import com.rey.material.widget.RadioButton;
import com.task.Persistence.DatabaseManager;
import com.task.Persistence.Task;
import com.task.R;
import com.task.Utils.FormUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.task.Utils.FormUtils.CREATED;
import static com.task.Utils.FormUtils.IN_PROGRESS;
import static com.task.Utils.FormUtils.DONE;

/**
 * Created by laurenfalzarano on 10/8/17.
 */

public class TaskViewActivity extends StandardActivity {
    public final static String TASK_ID_KEY = "taskId";

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.title) TextView title;
    @BindView(R.id.due_date) TextView dueDate;
    @BindView(R.id.not_started_radio) RadioButton notStartedRadio;
    @BindView(R.id.progress_radio) RadioButton inProgressRadio;
    @BindView(R.id.completed_radio) RadioButton completedRadio;

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
//        dueDate.setText(task.getFormattedDate());
    }

    public void merp() {

    }

    private void clearRadioButtons() {
        notStartedRadio.setCheckedImmediately(false);
        inProgressRadio.setCheckedImmediately(false);
        completedRadio.setCheckedImmediately(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        clearRadioButtons();

        Date date = new Date(task.getDate());
        SimpleDateFormat formatter = new SimpleDateFormat("MMM dd yyyy");
        dueDate.setText(formatter.format(date));


        switch (task.getState()) {
            case IN_PROGRESS:
                inProgressRadio.setCheckedImmediately(true);
                break;
            case DONE:
                completedRadio.setCheckedImmediately(true);
                break;
            default:
                notStartedRadio.setCheckedImmediately(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.edit_menu, menu);
        menu.findItem(R.id.edit).setIcon(
                new IconDrawable(this, IoniconsIcons.ion_edit)
                        .colorRes(R.color.white)
                        .actionBarSize());
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.edit) {
            Intent intent = new Intent(this, CreateTaskActivity.class);
            intent.putExtra(TASK_ID_KEY, task.getTaskId());
            startActivityForResult(intent, 1);
        }

        return super.onOptionsItemSelected(item);
    }

    @OnClick({R.id.not_started_radio, R.id.progress_radio, R.id.completed_radio})
    public void completedClick(View view) {
        clearRadioButtons();

        switch (view.getId()) {
            case R.id.not_started_radio:
                notStartedRadio.setCheckedImmediately(true);
                task.setState(CREATED);
                break;
            case R.id.progress_radio:
                inProgressRadio.setCheckedImmediately(true);
                task.setState(IN_PROGRESS);
                break;
            case R.id.completed_radio:
                completedRadio.setCheckedImmediately(true);
                task.setState(DONE);
        }

        DatabaseManager.get().saveTask(task);
    }
}
