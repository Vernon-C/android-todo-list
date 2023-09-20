package com.example.assignment5task1;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class CompletedTasksActivity extends AppCompatActivity implements MyAdapter.OnItemClickListener {
    private DatabaseHandler databaseHandler;
    private List<Task> taskList;
    private MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed_tasks);

        databaseHandler = new DatabaseHandler(this, null, null, 1);

        populateTaskList();

        // Set the recycler view
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        myAdapter = new MyAdapter(taskList, this, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(myAdapter);
    }

    private void populateTaskList() {
        if (taskList != null)
            taskList.clear();

        taskList = databaseHandler.getAllCompletedTasks();

        // Orders the tasks from soonest to latest
        Collections.sort(taskList, new Comparator<Task>() {
            @Override
            public int compare(Task o1, Task o2) {
                // Puts overdue tasks at the bottom and sorts from closest to furthest tasks at the top
                if (o1.getDate().before(new Date()))
                    return o2.getDate().compareTo(o1.getDate());
                else if (o2.getDate().before(new Date()))
                    return o2.getDate().compareTo(o1.getDate());
                else
                    return o1.getDate().compareTo(o2.getDate());
            }
        });
    }

    // On long click functionality (To delete a task)
    @Override
    public void onItemLongClick(int position) {
        CreateAlertDialog(position);
    }

    // Creates an alert dialog to confirm the delete task operation
    private void CreateAlertDialog(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Task");
        builder.setMessage("Are you sure?");

        // Yes button
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int result = databaseHandler.deleteTask(taskList.get(position));
                System.out.println(result);
                populateTaskList();
                myAdapter.setTaskList(taskList);
                myAdapter.notifyDataSetChanged();
            }
        });

        // No button
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                System.out.println("User cancelled the delete task operation");
            }
        });

        builder.create();
        builder.show();
    }

    // Sub-menu functionality
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sub_menu_completed_tasks, menu);
        return true;
    }

    // Sub-menu functionality
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.pending_tasks:
                // TODO: Change this to open a new activity or fragment
                Toast.makeText(this, "Pending Tasks Selected", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent();
                intent.setClass(this, MainActivity.class);
                startActivity(intent);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}