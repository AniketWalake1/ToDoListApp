package com.example.todotrial.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todotrial.AddNewTasks;
import com.example.todotrial.MainActivity;
import com.example.todotrial.Model.ToDoModel;
import com.example.todotrial.R;
import com.example.todotrial.Utils.DatabaseHandler;

import java.util.List;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ViewHolder> {
    private List<ToDoModel> todolist;
    private MainActivity activity;
    private DatabaseHandler db;
    public ToDoAdapter(DatabaseHandler db, MainActivity activity){
        this.db = db;
        this.activity = activity;

    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_layout, parent,false);
        return new ViewHolder(item);
    }

    public void onBindViewHolder(ViewHolder holder, int position){
        db.openDatabase();
        ToDoModel item = todolist.get(position);
        holder.task.setText(item.getTask());
        holder.task.setChecked(toBoolean(item.getStatus()));
        holder.task.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    db.updateStatus(item.getId(), 1);
                } else {
                    db.updateStatus(item.getId(), 0);
                }
            }
        });

    }
    public int getItemCount(){
        return todolist.size();
    }
    private boolean toBoolean(int n){
        return n!=0;
    }
    public Context getContext() {
        return activity;
    }
    public void setTasks(List<ToDoModel>todolist){
        this.todolist=todolist;
        notifyDataSetChanged();
    }
    public void editItem(int position) {
        ToDoModel item = todolist.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("id", item.getId());
        bundle.putString("task", item.getTask());
        AddNewTasks fragment = new AddNewTasks();
        fragment.setArguments(bundle);
        fragment.show(activity.getSupportFragmentManager(), AddNewTasks.TAG);
    }
    public void deleteItem(int position) {
        ToDoModel item = todolist.get(position);
        db.deleteTask(item.getId());
        todolist.remove(position);
        notifyItemRemoved(position);
    }
    public static class ViewHolder extends RecyclerView.ViewHolder{
        CheckBox task;
        ViewHolder(View view){
            super(view);
            task = view.findViewById(R.id.todoCheckBox);
        }
    }
}
