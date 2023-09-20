package com.example.assignment5task1;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Task implements Parcelable {
    private int id;
    private String title;
    private String due_date;
    private Date date;
    private String details;
    private String priority;
    private String completion_status;

    public Task() {
    }

    public Task(String title, String due_date, String details, String priority, String completion_status) {
        this.title = title;
        this.due_date = due_date;
        this.details = details;
        this.priority = priority;
        this.completion_status = completion_status;
    }

    public Task(int id, String title, String due_date, String details, String priority, String completion_status) {
        this.id = id;
        this.title = title;
        this.due_date = due_date;
        this.details = details;
        this.priority = priority;
        this.completion_status = completion_status;
    }

    protected Task(Parcel in) {
        id = in.readInt();
        title = in.readString();
        due_date = in.readString();
        details = in.readString();
        priority = in.readString();
        completion_status = in.readString();
    }

    public static final Creator<Task> CREATOR = new Creator<Task>() {
        @Override
        public Task createFromParcel(Parcel in) {
            return new Task(in);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDue_date() {
        return due_date;
    }

    public void setDue_date(String due_date) {
        this.due_date = due_date;

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("d MMM yyyy");
            date = sdf.parse(due_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public Date getDate() {
        return date;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getCompletion_status() {
        return completion_status;
    }

    public void setCompletion_status(String completion_status) {
        this.completion_status = completion_status;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(due_date);
        dest.writeString(details);
        dest.writeString(priority);
        dest.writeString(completion_status);
    }
}
