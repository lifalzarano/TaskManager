package com.task.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.task.Persistence.DatabaseManager;
import com.task.Persistence.Task;
import com.task.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by laurenfalzarano on 7/15/17.
 */

public class TaskAdapter extends BaseAdapter {
    private List<Task> tasks;
    private Context context;

    public TaskAdapter(Context context, List<Task> tasks) {
        this.context = context;
        this.tasks = tasks;
    }

    public void refreshTasks() {
        tasks = DatabaseManager.get().getTasks();
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
            holder = new TaskListViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (TaskListViewHolder) view.getTag();
        }

        holder.title.setText(getItem(position).getName());

        return view;
    }

    public static class TaskListViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.task_title) TextView title;

        public TaskListViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
