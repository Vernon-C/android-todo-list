package com.example.assignment5task1;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class EditTaskActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private DatabaseHandler databaseHandler;
    private EditText editText_title;
    private EditText editText_due_date;
    private EditText editText_details;
    private CheckBox checkBox_priority;
    private String displayDate, priority;
    private Date due_date;
    private String TaskID;
    private String completion_status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        initUI();

        // Shows date picker when the edit text field is tapped on
        editText_due_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });

        getPriority();
    }

    // Initialize the variables
    private void initUI() {
        editText_title = findViewById(R.id.editText_title);
        editText_due_date = findViewById(R.id.editText_due_date);
        editText_details = findViewById(R.id.editText_details);
        checkBox_priority = findViewById(R.id.checkBox_priority);

        // Get the task object from the adapter
        if (getIntent() != null) {
            ArrayList<Task> tasks = getIntent().getParcelableArrayListExtra("TASKS");

            for (Task t : tasks) {
                TaskID = String.valueOf(t.getId());
                String title = t.getTitle();
                String due_date = t.getDue_date();
                String details = t.getDetails();
                priority = t.getPriority();
                completion_status = t.getCompletion_status();

                editText_title.setText(title);
                editText_due_date.setText(due_date);
                displayDate = due_date;
                editText_details.setText(details);

                if (priority.equals("High"))
                    checkBox_priority.setChecked(true);
                else
                    checkBox_priority.setChecked(false);
            }
        }
    }

    // Check whether the checkbox has been checked
    private void getPriority() {
        checkBox_priority.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox_priority.isChecked()) {
                    priority = "High";
                } else {
                    priority = "Normal";
                }
            }
        });
    }

    // Date picker functionality
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        due_date = c.getTime();

        // Prevents the user from selecting an invalid date
        if (due_date.before(new Date())) {
            Toast.makeText(this, "Invalid date. Please try again.", Toast.LENGTH_SHORT).show();
        } else {
            // Display the date in a readable way
            SimpleDateFormat sdf = new SimpleDateFormat("d MMM yyyy");
            displayDate = sdf.format(due_date);
            editText_due_date.setText(displayDate);
        }
    }

    // Sub-menu functionality
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_task, menu);
        return true;
    }

    // Sub-menu functionality
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_task:
                validateInput();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Checks for empty fields and returns hints
    private void validateInput() {
        String title = editText_title.getText().toString();
        String details = editText_details.getText().toString();

        if (title.equals("")) {
            editText_title.setHint("Please enter a title");
        } else if (displayDate == null) {
            editText_due_date.setHint("Please enter a date");
        } else {
            databaseHandler = new DatabaseHandler(this,
                    null, null, 1);

            Task task = new Task(title, displayDate, details, priority, completion_status);

            databaseHandler.editTask(task, TaskID);

            Intent intent;

            if (completion_status.equals("Pending")) {
                intent = new Intent(this, MainActivity.class);
            } else {
                intent = new Intent(this, CompletedTasksActivity.class);
            }
            startActivity(intent);
        }
    }
}