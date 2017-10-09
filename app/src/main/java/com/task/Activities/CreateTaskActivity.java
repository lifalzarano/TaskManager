package com.task.Activities;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.rey.material.app.DatePickerDialog;
import com.rey.material.app.Dialog;
import com.rey.material.app.DialogFragment;
import com.rey.material.app.TimePickerDialog;
import com.task.Persistence.DatabaseManager;
import com.task.Persistence.Task;
import com.task.R;
import com.task.Utils.FormUtils;

import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

/**
 * Created by laurenfalzarano on 4/9/17.
 */

public class CreateTaskActivity extends StandardActivity {

    @BindView(R.id.parent) View parent;
    @BindView(R.id.title_input) EditText titleInput;
    @BindView(R.id.clear_title) View clearTitle;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.due_date_input) TextView dueDate;
    @BindView(R.id.due_time_input) TextView dueTime;

    private Task task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        task = new Task();
    }

    @OnTextChanged(value = R.id.title_input, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void afterTextChanged(Editable input) {
        clearTitle.setVisibility(input.length() > 0 ? View.VISIBLE : View.GONE);
    }

    @OnClick(R.id.clear_title)
    public void clearTitle() {
        titleInput.setText("");
    }

    @OnClick({R.id.due_date_title, R.id.due_date_input})
    public void onDate() {
        Dialog.Builder builder = new DatePickerDialog.Builder(){
            @Override
            public void onPositiveActionClicked(DialogFragment fragment) {
                DatePickerDialog dialog = (DatePickerDialog) fragment.getDialog();
                dueDate.setText(dialog.getFormattedDate(SimpleDateFormat.getDateInstance()));
                task.setDate(dialog.getDate());
                super.onPositiveActionClicked(fragment);
            }
        };
        builder.positiveAction(getString(R.string.done));
        builder.negativeAction(getString(R.string.cancel));

        DialogFragment fragment = DialogFragment.newInstance(builder);
        fragment.show(getSupportFragmentManager(), null);
    }

    @OnClick({R.id.due_time_title, R.id.due_time_input})
    public void onTime() {
        if (dueDate.length() == 0) {
            FormUtils.showBlackSnackbar(parent, R.string.need_date);
            return;
        }

        Dialog.Builder builder = new TimePickerDialog.Builder(){
            @Override
            public void onPositiveActionClicked(DialogFragment fragment) {
                TimePickerDialog dialog = (TimePickerDialog)fragment.getDialog();
                dueTime.setText(dialog.getFormattedTime(SimpleDateFormat.getTimeInstance()));
                task.setTime(dialog.getHour());
                super.onPositiveActionClicked(fragment);
            }
        };
        builder.positiveAction(getString(R.string.done));
        builder.negativeAction(getString(R.string.cancel));

        DialogFragment fragment = DialogFragment.newInstance(builder);
        fragment.show(getSupportFragmentManager(), null);
    }

    @OnClick(R.id.save_button)
    public void onSave() {
        String title = titleInput.getText().toString().trim();

        if (title.isEmpty()) {
            FormUtils.showBlackSnackbar(parent, "You must provide a title!");
            return;
        }

        task.setName(title);
        DatabaseManager.get().saveTask(task);

        setResult(RESULT_OK);
        finish();
    }
}
