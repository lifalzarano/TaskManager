package com.task.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.joanzapata.iconify.widget.IconTextView;
import com.task.Persistence.DatabaseManager;
import com.task.Persistence.Task;
import com.task.R;

import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.task.Utils.FormUtils.DONE;
import static com.task.Utils.FormUtils.IN_PROGRESS;

/**
 * Created by laurenfalzarano on 7/15/17.
 */

public class TaskAdapter extends BaseAdapter {
    private List<Task> tasks;
    private Context context;

    public TaskAdapter(Context context) {
        this.context = context;
        refreshTasks("");
    }

    public void refreshTasks(String query) {
        tasks = DatabaseManager.get().getTasks(query);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return tasks.size();
    }

    @Override
    public Task getItem(int position) {
        return tasks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final TaskListViewHolder holder;
        if (view == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = vi.inflate(R.layout.task_list_cell, parent, false);
            holder = new TaskListViewHolder(view, tasks.get(position));
            view.setTag(holder);
        } else {
            holder = (TaskListViewHolder) view.getTag();
        }

        holder.loadTask(tasks.get(position));

        return view;
    }

    public static class TaskListViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.task_title) TextView title;
        @BindView(R.id.state_icon) IconTextView stateIcon;

        @BindString(R.string.in_progress_icon) String progressIcon;
        @BindString(R.string.check_icon) String checkIcon;

        public TaskListViewHolder(View view, Task task) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void loadTask(Task task) {
            title.setText(task.getName());

            switch (task.getState()) {
                case IN_PROGRESS:
                    stateIcon.setText(progressIcon);
                    break;
                case DONE:
                    stateIcon.setText(checkIcon);
                    break;
                default:
                    stateIcon.setText("");
            }
        }
    }
}
