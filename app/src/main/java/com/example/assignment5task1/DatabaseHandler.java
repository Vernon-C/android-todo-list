package com.example.assignment5task1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final String DATABASE = "TaskDB";
    private static final String TABLE_NAME = "Task_Table";

    // Table fields
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_DUE_DATE = "due_date";
    private static final String KEY_DETAILS = "details";
    private static final String KEY_PRIORITY = "priority";
    private static final String KEY_COMPLETION_STATUS = "completion_status";

    public DatabaseHandler(@Nullable Context context, @Nullable String name,
                           @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create_table = "CREATE TABLE " + TABLE_NAME + "(" +
                KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                KEY_TITLE + " TEXT, " + KEY_DUE_DATE + " TEXT, " +
                KEY_DETAILS + " TEXT, " + KEY_PRIORITY + " TEXT, " +
                KEY_COMPLETION_STATUS + " TEXT)";

        db.execSQL(create_table);
        System.out.println("Table created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
        System.out.println("Table dropped");
    }

    public long addTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Stores variables for you
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_TITLE, task.getTitle());
        contentValues.put(KEY_DUE_DATE, task.getDue_date());
        contentValues.put(KEY_DETAILS, task.getDetails());
        contentValues.put(KEY_PRIORITY, task.getPriority());
        contentValues.put(KEY_COMPLETION_STATUS, task.getCompletion_status());

        // If this return is successful, it will return the ID of the row. Else, it will return -1
        return db.insert(TABLE_NAME, null, contentValues);
    }

    public void editTask(Task task, String TaskID) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_TITLE, task.getTitle());
        contentValues.put(KEY_DUE_DATE, task.getDue_date());
        contentValues.put(KEY_DETAILS, task.getDetails());
        contentValues.put(KEY_PRIORITY, task.getPriority());
        contentValues.put(KEY_COMPLETION_STATUS, task.getCompletion_status());

        db.update(TABLE_NAME, contentValues, KEY_ID + "=?", new String[] {TaskID});
    }

    public Task getTask(int taskID) {
        Task task = null;
        SQLiteDatabase db = this.getReadableDatabase();

        // Retrieve the record
        Cursor cursor = db.query(TABLE_NAME, new String[] {KEY_ID, KEY_TITLE, KEY_DUE_DATE,
                        KEY_DETAILS, KEY_PRIORITY, KEY_COMPLETION_STATUS},
                KEY_ID + "=?", new String[]{String.valueOf(taskID)},
                null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
            task = new Task(cursor.getInt(0), cursor.getString(1),
                    cursor.getString(2), cursor.getString(3),
                    cursor.getString(4), cursor.getString(5));
        }

        return task;
    }

    public List<Task> getAllPendingTasks() {
        List<Task> taskList = new ArrayList<Task>();

        // Sort the table in ascending order based on the date
        String selectQuery = "SELECT * FROM " + TABLE_NAME + " WHERE " + KEY_COMPLETION_STATUS +
                "=?" + " order by " + KEY_DUE_DATE + " DESC";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[] {"Pending"});

        if (cursor.moveToFirst()) {
            do {
                Task task = new Task();
                task.setId(Integer.parseInt(cursor.getString(0)));
                task.setTitle(cursor.getString(1));
                task.setDue_date(cursor.getString(2));
                task.setDetails(cursor.getString(3));
                task.setPriority(cursor.getString(4));
                task.setCompletion_status(cursor.getString(5));

                taskList.add(task);
            } while (cursor.moveToNext());
        }

        return taskList;
    }

    public List<Task> getAllCompletedTasks() {
        List<Task> taskList = new ArrayList<Task>();

        // Sort the table in ascending order based on the date
        String selectQuery = "SELECT * FROM " + TABLE_NAME + " WHERE " + KEY_COMPLETION_STATUS +
                "=?" + " order by " + KEY_DUE_DATE + " DESC";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[] {"Completed"});

        if (cursor.moveToFirst()) {
            do {
                Task task = new Task();
                task.setId(Integer.parseInt(cursor.getString(0)));
                task.setTitle(cursor.getString(1));
                task.setDue_date(cursor.getString(2));
                task.setDetails(cursor.getString(3));
                task.setPriority(cursor.getString(4));
                task.setCompletion_status(cursor.getString(5));

                taskList.add(task);
            } while (cursor.moveToNext());
        }

        return taskList;
    }

    public int deleteTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, KEY_ID + "=?", new String[] {String.valueOf(task.getId())});
    }

    public void completeTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_TITLE, task.getTitle());
        contentValues.put(KEY_DUE_DATE, task.getDue_date());
        contentValues.put(KEY_DETAILS, task.getDetails());
        contentValues.put(KEY_PRIORITY, task.getPriority());
        contentValues.put(KEY_COMPLETION_STATUS, task.getCompletion_status());

        String taskID = String.valueOf(task.getId());
        db.update(TABLE_NAME, contentValues, KEY_ID + "=?", new String[]{taskID});
    }
}
