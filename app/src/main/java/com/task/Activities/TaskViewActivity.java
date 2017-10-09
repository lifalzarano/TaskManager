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
import com.task.Persistence.TaskDO;
import com.task.R;

import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.net.wifi.SupplicantState.COMPLETED;
import static com.task.Persistence.TaskDO.DONE;
import static com.task.Persistence.TaskDO.IN_PROGRESS;

/**
 * Created by laurenfalzarano on 10/8/17.
 */

public class TaskViewActivity extends StandardActivity {
    public final static String TASK_KEY = "taskkey";

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.title) TextView title;
    @BindView(R.id.due_date) TextView dueDate;
    @BindView(R.id.not_started_radio) RadioButton notStartedRadio;
    @BindView(R.id.progress_radio) RadioButton inProgressRadio;
    @BindView(R.id.completed_radio) RadioButton completedRadio;

    private TaskDO task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_task_activity);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        task = getIntent().getParcelableExtra(TASK_KEY);
        Log.d("Pusheen", "task: " + task);

        title.setText(task.getName());
        dueDate.setText(task.getFormattedDate());
    }

    public void merp() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    }

    @Override
    public void onResume() {
        super.onResume();

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
            startActivityForResult(new Intent(this, CreateTaskActivity.class), 1);
        }

        return super.onOptionsItemSelected(item);
    }

    @OnClick({R.id.not_started_radio, R.id.progress_radio, R.id.completed_radio})
    public void completedClick(View view) {
        notStartedRadio.setChecked(false);
        inProgressRadio.setChecked(false);
        completedRadio.setChecked(false);

        switch (view.getId()) {
            case R.id.not_started_radio:
                notStartedRadio.setCheckedImmediately(true);
                break;
            case R.id.progress_radio:
                inProgressRadio.setCheckedImmediately(true);
                break;
            case R.id.completed_radio:
                completedRadio.setCheckedImmediately(true);
        }
    }
}
