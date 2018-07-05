package com.task.Activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.task.Persistence.DatabaseManager;
import com.task.Persistence.Task;
import com.task.R;
import com.task.Utils.FormUtils;
import com.task.Utils.TimeUtils;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

/**
 * Created by laurenfalzarano on 4/9/17.
 */

public class TaskFormActivity extends StandardActivity {

    @BindView(R.id.parent) View parent;
    @BindView(R.id.title_input) EditText titleInput;
    @BindView(R.id.clear_title) View clearTitle;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.due_date_input) TextView dueDate;
    @BindView(R.id.due_time_input) TextView dueTime;
    @BindView(R.id.save_button) Button saveButton;

    private Task task;
    private Calendar dateCalendar;
    private Calendar timeCalendar;
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dateCalendar = Calendar.getInstance();
        timeCalendar = Calendar.getInstance();

        initializeDialogs();

        String taskId = getIntent().getStringExtra(TaskViewActivity.TASK_ID_KEY);
        if (taskId == null) {
            task = new Task();
            setTitle(R.string.create_task);
            saveButton.setText(R.string.create_task);
        } else {
            setTitle(R.string.edit_task);
            saveButton.setText(R.string.edit_task);

            task = DatabaseManager.get().getTask(taskId);
            dateCalendar.setTimeInMillis(task.getDate());
            timeCalendar.setTimeInMillis(task.getTime());
            fillInForm();
        }
    }

    private void fillInForm() {
        titleInput.setText(task.getName());
        titleInput.setSelection(task.getName().length());
        if (dateCalendar.getTimeInMillis() > 0L) {
            dueDate.setText(TimeUtils.getDateText(dateCalendar.getTimeInMillis()));
        }
        if (timeCalendar.getTimeInMillis() > 0L) {
            dueTime.setText(TimeUtils.getTimeText(timeCalendar.getTimeInMillis()));
        }
    }

    private void initializeDialogs() {
        int hourOfDay = timeCalendar.get(Calendar.HOUR_OF_DAY);
        int minute = timeCalendar.get(Calendar.MINUTE);
        int modValue = DateFormat.is24HourFormat(this) ? 24 : 12;

        if (minute > 30) {
            hourOfDay = ++hourOfDay % modValue;
            minute = 0;
        } else {
            minute = 30;
        }

        timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                timeCalendar.set(Calendar.HOUR_OF_DAY, hour);
                timeCalendar.set(Calendar.MINUTE, minute);
                dueTime.setText(TimeUtils.getTimeText(timeCalendar.getTimeInMillis()));
            }
        }, hourOfDay, minute, DateFormat.is24HourFormat(this));

        int year = dateCalendar.get(Calendar.YEAR);
        int month = dateCalendar.get(Calendar.MONTH);
        int dayOfMonth = dateCalendar.get(Calendar.DAY_OF_MONTH);
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                dateCalendar.set(Calendar.YEAR, year);
                dateCalendar.set(Calendar.MONTH, month);
                dateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                dueDate.setText(TimeUtils.getDateText(dateCalendar.getTimeInMillis()));
            }
        }, year, month, dayOfMonth);
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
        datePickerDialog.show();
    }

    @OnClick({R.id.due_time_title, R.id.due_time_input})
    public void onTime() {
        timePickerDialog.show();
    }

    @OnClick(R.id.save_button)
    public void onSave() {
        String title = titleInput.getText().toString().trim();

        if (title.isEmpty()) {
            FormUtils.showBlackSnackbar(parent, "You must provide a title!");
            return;
        }

        task.setName(title);
        if (!dueDate.getText().toString().isEmpty()) {
            task.setDate(dateCalendar.getTimeInMillis());
        }
        if (!dueTime.getText().toString().isEmpty()) {
            task.setTime(timeCalendar.getTimeInMillis());
        }
        DatabaseManager.get().saveTask(task);

        setResult(RESULT_OK);
        finish();
    }
}
