package com.task.Activities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.IoniconsIcons;
import com.task.Persistence.DatabaseManager;
import com.task.Persistence.Task;
import com.task.R;
import com.task.Utils.FormUtils;
import com.task.Utils.TimeUtils;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.task.Utils.FormUtils.CREATED;
import static com.task.Utils.FormUtils.DONE;
import static com.task.Utils.FormUtils.IN_PROGRESS;

/**
 * Created by laurenfalzarano on 10/8/17.
 */

public class TaskViewActivity extends StandardActivity {
    public final static String TASK_ID_KEY = "taskId";

    @BindView(R.id.parent) View parent;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.title) TextView title;
    @BindView(R.id.due_container) View dueContainer;
    @BindView(R.id.due_date) TextView dueDate;
    @BindView(R.id.not_started_radio) RadioButton notStartedRadio;
    @BindView(R.id.progress_radio) RadioButton inProgressRadio;
    @BindView(R.id.completed_radio) RadioButton completedRadio;

    private String taskId;
    private Task task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_task_activity);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        taskId = getIntent().getStringExtra(TASK_ID_KEY);
    }

    private void clearRadioButtons() {
        notStartedRadio.setChecked(false);
        inProgressRadio.setChecked(false);
        completedRadio.setChecked(false);
    }

    private void deleteTask() {

    }

    @Override
    public void onResume() {
        super.onResume();

        task = DatabaseManager.get().getTask(taskId);
        title.setText(task.getName());
        dueDate.setText(TimeUtils.getDateAndTimeText(task));
        clearRadioButtons();

        if (task.getDate() == 0) {
            dueContainer.setVisibility(View.GONE);
        }

        switch (task.getState()) {
            case IN_PROGRESS:
                inProgressRadio.setChecked(true);
                break;
            case DONE:
                completedRadio.setChecked(true);
                break;
            default:
                notStartedRadio.setChecked(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_menu, menu);

        menu.findItem(R.id.edit).setIcon(
                new IconDrawable(this, IoniconsIcons.ion_edit)
                        .colorRes(R.color.white)
                        .actionBarSize());

//        menu.findItem(R.id.delete).setIcon(
//                new IconDrawable(this, IoniconsIcons.ion_android_delete)
//                        .colorRes(R.color.white)
//                        .actionBarSize());
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit:
                Intent intent = new Intent(this, TaskFormActivity.class);
                intent.putExtra(TASK_ID_KEY, task.getTaskId());
                startActivityForResult(intent, 1);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @OnClick({R.id.not_started_radio, R.id.progress_radio, R.id.completed_radio})
    public void completedClick(View view) {
        clearRadioButtons();

        switch (view.getId()) {
            case R.id.not_started_radio:
                notStartedRadio.setChecked(true);
                task.setState(CREATED);
                break;
            case R.id.progress_radio:
                inProgressRadio.setChecked(true);
                task.setState(IN_PROGRESS);
                break;
            case R.id.completed_radio:
                completedRadio.setChecked(true);
                task.setState(DONE);
        }

        DatabaseManager.get().saveTask(task);
        FormUtils.showBlackSnackbar(parent, R.string.task_updated);
    }
}
