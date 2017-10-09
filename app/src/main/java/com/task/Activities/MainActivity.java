package com.task.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.IoniconsIcons;
import com.task.Adapters.TaskAdapter;
import com.task.Persistence.DatabaseManager;
import com.task.Persistence.Task;
import com.task.Persistence.TaskDO;
import com.task.R;
import com.task.Utils.FormUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;

import static com.task.Activities.TaskViewActivity.TASK_KEY;

public class MainActivity extends StandardActivity {
    @BindView(R.id.parent) View parent;
    @BindView(R.id.fab) FloatingActionButton fab;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.empty_state) View emptyState;
    @BindView(R.id.content_view) View contentView;
    @BindView(R.id.num_tasks) TextView numTasks;
    @BindView(R.id.tasks_list) ListView taskListView;

    private List<Task> taskList;
    private TaskAdapter taskAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        taskList = DatabaseManager.get().getTasks();
        taskAdapter = new TaskAdapter(this, taskList);
        taskListView.setAdapter(taskAdapter);

        fab.setImageDrawable(new IconDrawable(this, IoniconsIcons.ion_plus)
                .colorRes(R.color.white)
                .actionBarSize());
    }

    private void refreshPage() {
        taskList = DatabaseManager.get().getTasks();

        if (taskList.size() == 0) {
            emptyState.setVisibility(View.VISIBLE);
            contentView.setVisibility(View.GONE);
        } else {
            emptyState.setVisibility(View.GONE);
            contentView.setVisibility(View.VISIBLE);
            numTasks.setText(String.format(getString(R.string.number_of_tasks), taskList.size()));
        }
    }

    @OnClick(R.id.fab)
    public void onFabClick() {
        startActivityForResult(new Intent(this, CreateTaskActivity.class), 1);
    }

    @OnItemClick(R.id.tasks_list)
    public void onTaskClick(int position) {
        Intent intent = new Intent(this, TaskViewActivity.class);
        intent.putExtra(TASK_KEY, new TaskDO(taskList.get(position)));
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshPage();
        taskAdapter.refreshTasks();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.findItem(R.id.action_settings).setIcon(
                new IconDrawable(this, IoniconsIcons.ion_gear_a)
                        .colorRes(R.color.white)
                        .actionBarSize());
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            FormUtils.showBlackSnackbar(parent, getString(R.string.task_created_success));
        }
    }
}
