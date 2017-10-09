package com.task.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.IoniconsIcons;
import com.task.Adapters.TaskAdapter;
import com.task.Persistence.DatabaseManager;
import com.task.R;
import com.task.Utils.FormUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import butterknife.OnTextChanged;

import static com.task.Activities.TaskViewActivity.TASK_ID_KEY;

public class MainActivity extends StandardActivity {
    @BindView(R.id.parent) View parent;
    @BindView(R.id.fab) FloatingActionButton fab;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.search_input) EditText searchInput;
    @BindView(R.id.clear_search_input) View clearSearch;
    @BindView(R.id.empty_state) View emptyState;
    @BindView(R.id.no_notifiers_text) TextView noTasksText;
    @BindView(R.id.no_notifiers_subtext) TextView addTasksCta;
    @BindView(R.id.num_tasks) TextView numTasks;
    @BindView(R.id.tasks_list) ListView taskListView;

    private TaskAdapter taskAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        taskAdapter = new TaskAdapter(this);
        taskListView.setAdapter(taskAdapter);

        fab.setImageDrawable(new IconDrawable(this, IoniconsIcons.ion_plus)
                .colorRes(R.color.white)
                .actionBarSize());
    }

    @OnTextChanged(value = R.id.search_input, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void afterTextChanged(Editable input) {
        String query = input.toString();
        clearSearch.setVisibility(query.isEmpty() ? View.GONE : View.VISIBLE);
        taskAdapter.refreshTasks(query);
        refreshPage();
    }

    @OnClick(R.id.clear_search_input)
    public void clearSearch() {
        searchInput.setText("");
    }

    private void refreshPage() {
        int taskCount = taskAdapter.getCount();

        if (taskCount == 0) {
            int tasksTrueCount = DatabaseManager.get().getNumTasks();

            // Absolutely 0 tasks in DB
            if (tasksTrueCount == 0) {
                noTasksText.setText(R.string.no_tasks);
                addTasksCta.setVisibility(View.VISIBLE);
            }
            // Some tasks in DB; it's just that none match the user's search input
            else {
                noTasksText.setText(R.string.no_tasks_match);
                addTasksCta.setVisibility(View.GONE);
            }

            emptyState.setVisibility(View.VISIBLE);
            taskListView.setVisibility(View.GONE);
        } else {
            emptyState.setVisibility(View.GONE);
            taskListView.setVisibility(View.VISIBLE);
        }

        if (taskCount == 1) {
            numTasks.setText(R.string.one_task);
        } else {
            numTasks.setText(String.format(getString(R.string.number_of_tasks), taskCount));
        }
    }

    @OnClick(R.id.fab)
    public void onFabClick() {
        startActivityForResult(new Intent(this, TaskFormActivity.class), 1);
    }

    @OnItemClick(R.id.tasks_list)
    public void onTaskClick(int position) {
        Intent intent = new Intent(this, TaskViewActivity.class);
        intent.putExtra(TASK_ID_KEY, taskAdapter.getItem(position).getTaskId());
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshPage();
        taskAdapter.refreshTasks(searchInput.getText().toString());
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
