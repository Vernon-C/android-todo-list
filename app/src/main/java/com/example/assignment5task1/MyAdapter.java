package com.example.assignment5task1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    DatabaseHandler databaseHandler;
    private List<Task> taskList;
    private Context context;
    private OnItemClickListener onItemClickListener;
    private Activity activity;
    private int REQUEST_CODE = 0;


    public MyAdapter(List<Task> taskList, Context context, Activity activity) {
        this.taskList = taskList;
        this.context = context;
        this.activity = activity;
    }

    @NonNull
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.item_view, parent, false);
        onItemClickListener = (OnItemClickListener) activity;

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.MyViewHolder holder, int position) {
        holder.textView_title.setText(taskList.get(position).getTitle());
        holder.textView_due_date.setText(taskList.get(position).getDue_date());

        holder.switch_completion_status.setText(taskList.get(position).getCompletion_status());

        Task task = taskList.get(position);

        if (task.getCompletion_status().equals("Completed"))
            holder.switch_completion_status.setChecked(true);
        else
            holder.switch_completion_status.setChecked(false);

        // Changes the color of the card view to yellow if the task is important
        if (taskList.get(position).getPriority().equals("High"))
            holder.cardView.setCardBackgroundColor(Color.parseColor("#fdfd96"));


        // Changes the color of the card view to red if the task is overdue
        Date date = new Date();

        if (taskList.get(position).getDate().before(date)) {
            holder.imageView_date.setImageResource(R.drawable.red_calendar_icon);
            holder.cardView.setCardBackgroundColor(Color.parseColor("#ff6961"));
        }
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public interface OnItemClickListener {
        void onItemLongClick(int position);
    }

    public void setTaskList(List<Task> taskList) {
        this.taskList = taskList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CardView cardView;
        TextView textView_title;
        ImageView imageView_date;
        TextView textView_due_date;
        Switch switch_completion_status;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.cardView);
            textView_title = itemView.findViewById(R.id.textView_title);
            imageView_date = itemView.findViewById(R.id.imageView_date);
            textView_due_date = itemView.findViewById(R.id.textView_due_date);
            switch_completion_status = itemView.findViewById(R.id.switch_completion_status);
            databaseHandler = new DatabaseHandler(context.getApplicationContext(), null, null, 1);

            // On click
            itemView.setOnClickListener(this);

            // On long click
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    onItemClickListener.onItemLongClick(getAdapterPosition());
                    return true;
                }
            });

            // On switch status change
            switch_completion_status.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Task task = taskList.get(getAdapterPosition());

                    if (switch_completion_status.isChecked()) {
                        switch_completion_status.setText("Completed");
                        task.setCompletion_status("Completed");
                    } else {
                        switch_completion_status.setText("Pending");
                        task.setCompletion_status("Pending");
                    }
                    databaseHandler.completeTask(task);
                    taskList.remove(task);
                    notifyItemRemoved(getAdapterPosition());  // Needed to properly refresh the adapter
                }
            });
        }

        // On click to edit task
        @Override
        public void onClick(View v) {
            Task task = taskList.get(getAdapterPosition());
            ArrayList<Task> tasks = new ArrayList<>();
            tasks.add(task);

            Intent intent = new Intent();
            intent.putParcelableArrayListExtra("TASKS", tasks);
            intent.setClass(context, EditTaskActivity.class);
            activity.startActivityForResult(intent, REQUEST_CODE);
        }
    }
}
